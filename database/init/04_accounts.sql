CREATE TABLE accounts (

    id INT AUTO_INCREMENT PRIMARY KEY,

    customer_id INT,

    account_number VARCHAR(30),

    account_type VARCHAR(20),

    balance DECIMAL(12,2),

    FOREIGN KEY(customer_id)
        REFERENCES customers(id)

);
