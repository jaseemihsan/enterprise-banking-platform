CREATE TABLE customers (

    id INT AUTO_INCREMENT PRIMARY KEY,

    first_name VARCHAR(50),

    last_name VARCHAR(50),

    email VARCHAR(100),

    phone VARCHAR(20),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);
