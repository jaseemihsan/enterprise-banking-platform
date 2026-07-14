CREATE TABLE IF NOT EXISTS accounts (

    id INT AUTO_INCREMENT PRIMARY KEY,

    customer_id INT NOT NULL,

    account_number VARCHAR(20) UNIQUE NOT NULL,

    account_type ENUM('SAVINGS','CURRENT') NOT NULL,

    balance DECIMAL(15,2) DEFAULT 0,

    status ENUM('ACTIVE','BLOCKED','CLOSED') DEFAULT 'ACTIVE',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_customer
        FOREIGN KEY(customer_id)
        REFERENCES customers(id)

);
