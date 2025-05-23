-- Users Table
CREATE TABLE User
(
    user_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password CHAR(60)           NOT NULL,
    balance  DECIMAL(20, 8) DEFAULT 10000.0 CHECK (balance >= 0)
);

-- Crypto Table
CREATE TABLE Crypto
(
    crypto_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(50) UNIQUE NOT NULL,
    symbol    VARCHAR(50) UNIQUE NOT NULL,
    bid       DOUBLE PRECISION CHECK (bid >= 0),
    ask       DOUBLE PRECISION CHECK (ask >= 0),
    last      DOUBLE PRECISION CHECK (last >= 0
) ,
    volume    DOUBLE PRECISION CHECK (volume >= 0),
    low       DOUBLE PRECISION CHECK (low >= 0),
    high      DOUBLE PRECISION CHECK (high >= 0)
);

-- Portfolio Table
CREATE TABLE Portfolio
(
    portfolio_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    crypto_id    BIGINT NOT NULL,
    amount       DECIMAL(20, 8) DEFAULT 0.0 CHECK (amount >= 0),
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (crypto_id) REFERENCES Crypto (crypto_id)
);

-- Transaction Table
CREATE TABLE Transaction
(
    transaction_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id              BIGINT         NOT NULL,
    crypto_id            BIGINT         NOT NULL,
    amount               DECIMAL(20, 8) NOT NULL CHECK (amount > 0),
    price_at_transaction DECIMAL(20, 8) NOT NULL CHECK (price_at_transaction > 0),
    profit_loss          DECIMAL(20, 8) DEFAULT NULL,
    transaction_type     ENUM('BUY', 'SELL') NOT NULL,
    transaction_date     DATETIME       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (crypto_id) REFERENCES Crypto (crypto_id)
);

-- Triggers for automatic balance and portfolio updates

DELIMITER //

-- Trigger to update bank_balance after a BUY transaction
CREATE TRIGGER update_bank_balance_after_buy
    AFTER INSERT
    ON Transaction
    FOR EACH ROW
BEGIN
    IF NEW.transaction_type = 'BUY' THEN
    UPDATE User
    SET balance = balance - (NEW.amount * NEW.price_at_transaction)
    WHERE user_id = NEW.user_id;
END IF;
END
//

-- Trigger to update bank_balance after a SELL transaction
CREATE TRIGGER update_bank_balance_after_sell
    AFTER INSERT
    ON Transaction
    FOR EACH ROW
BEGIN
    IF NEW.transaction_type = 'SELL' THEN
    UPDATE User
    SET balance = balance + (NEW.amount * NEW.price_at_transaction)
    WHERE user_id = NEW.user_id;
END IF;
END
//

-- Trigger to update Portfolio after a BUY transaction
CREATE TRIGGER update_portfolio_after_buy
    AFTER INSERT
    ON Transaction
    FOR EACH ROW
BEGIN
    IF NEW.transaction_type = 'BUY' THEN
        IF EXISTS (
            SELECT 1 FROM Portfolio
            WHERE user_id = NEW.user_id AND crypto_id = NEW.crypto_id
        ) THEN
    UPDATE Portfolio
    SET amount = amount + NEW.amount
    WHERE user_id = NEW.user_id
      AND crypto_id = NEW.crypto_id;
    ELSE
            INSERT INTO Portfolio (user_id, crypto_id, amount)
            VALUES (NEW.user_id, NEW.crypto_id, NEW.amount);
END IF;
END IF;
END
//

-- Trigger to update Portfolio after a SELL transaction
CREATE TRIGGER update_portfolio_after_sell
    AFTER INSERT
    ON Transaction
    FOR EACH ROW
BEGIN
    IF NEW.transaction_type = 'SELL' THEN
        -- Check if user has enough crypto to sell
        IF EXISTS (
            SELECT 1 FROM Portfolio
            WHERE user_id = NEW.user_id
              AND crypto_id = NEW.crypto_id
              AND amount >= NEW.amount
        ) THEN
            -- Update the portfolio amount
    UPDATE Portfolio
    SET amount = amount - NEW.amount
    WHERE user_id = NEW.user_id
      AND crypto_id = NEW.crypto_id;

    -- Delete portfolio entry if amount becomes 0
    DELETE
    FROM Portfolio
    WHERE user_id = NEW.user_id
      AND crypto_id = NEW.crypto_id
      AND amount = 0;
    ELSE
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Insufficient funds for the transaction';
END IF;
END IF;
END
//

CREATE PROCEDURE reset_user_data(IN p_user_id BIGINT)
BEGIN
START TRANSACTION;

DELETE FROM Portfolio WHERE user_id = p_user_id;
DELETE FROM Transaction WHERE user_id = p_user_id;
UPDATE User SET balance = 10000.0 WHERE user_id = p_user_id;

COMMIT;
END
//

CREATE FUNCTION get_transaction_cost(transaction_id BIGINT)
    RETURNS DECIMAL(20, 8)
    DETERMINISTIC
    READS SQL DATA
BEGIN
    DECLARE result DECIMAL(20, 8);

SELECT amount * price_at_transaction
INTO result
FROM Transaction
WHERE transaction_id = transaction_id;

RETURN result;
END
//

CREATE FUNCTION get_average_transaction_cost(p_user_id BIGINT, p_crypto_id BIGINT)
    RETURNS DECIMAL(20, 8)
    DETERMINISTIC
    READS SQL DATA
BEGIN
    DECLARE avg_cost DECIMAL(20, 8);

SELECT SUM(amount * price_at_transaction) / SUM(amount)
INTO avg_cost
FROM Transaction
WHERE user_id = p_user_id
  AND crypto_id = p_crypto_id
  AND transaction_type = 'BUY';

RETURN avg_cost;
END
//

CREATE PROCEDURE reset_user(IN p_user_id BIGINT)
BEGIN
START TRANSACTION;

DELETE FROM Portfolio WHERE user_id = p_user_id;
DELETE FROM Transaction WHERE user_id = p_user_id;
UPDATE User SET balance = 10000.0 WHERE user_id = p_user_id;

COMMIT;
END
//

DELIMITER ;
