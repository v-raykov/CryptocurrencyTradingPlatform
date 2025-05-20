-- Users Table
CREATE TABLE User
(
    user_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password CHAR(60)           NOT NULL,
    balance  DECIMAL(20, 8) DEFAULT 1000.0
);

-- Crypto Table
CREATE TABLE Crypto
(
    crypto_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol    VARCHAR(50) UNIQUE NOT NULL,
    bid       DOUBLE PRECISION,
    ask       DOUBLE PRECISION,
    last      DOUBLE PRECISION,
    volume    DOUBLE PRECISION,
    low       DOUBLE PRECISION,
    high      DOUBLE PRECISION
);

-- Portfolio Table
CREATE TABLE Portfolio
(
    portfolio_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    crypto_id    BIGINT NOT NULL,
    amount       DECIMAL(20, 8) DEFAULT 0.0,
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (crypto_id) REFERENCES Crypto (crypto_id)
);

-- Transaction Table
CREATE TABLE Transaction
(
    transaction_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id              BIGINT         NOT NULL,
    crypto_id            BIGINT         NOT NULL,
    amount               DECIMAL(20, 8) NOT NULL,
    price_at_transaction DECIMAL(20, 8),
    transaction_type     ENUM('BUY', 'SELL') NOT NULL,
    transaction_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (crypto_id) REFERENCES Crypto (crypto_id)
);

-- Triggers for automatic balance updates

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
        IF EXISTS (SELECT 1 FROM Portfolio WHERE user_id = NEW.user_id AND crypto_id = NEW.crypto_id) THEN
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
        IF EXISTS (SELECT 1 FROM Portfolio WHERE user_id = NEW.user_id AND crypto_id = NEW.crypto_id AND amount >= NEW.amount) THEN
    UPDATE Portfolio
    SET amount = amount - NEW.amount
    WHERE user_id = NEW.user_id
      AND crypto_id = NEW.crypto_id;
    ELSE
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient funds for the transaction';
END IF;
END IF;
END
//

DELIMITER ;
