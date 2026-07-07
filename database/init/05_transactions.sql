CREATE TABLE transactions (

    id INT AUTO_INCREMENT PRIMARY KEY,

    account_id INT,

    transaction_type VARCHAR(30),

    amount DECIMAL(12,2),

    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(account_id)
        REFERENCES accounts(id)

);
