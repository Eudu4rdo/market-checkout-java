-- Produtos
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10,2),
    quantity INT
);

INSERT INTO products (name, price, quantity) VALUES
('Camiseta Básica Branca', 49.90, 100),
('Camiseta Básica Preta', 49.90, 80),
('Calça Jeans Slim Azul', 129.90, 50),
('Calça Jeans Skinny Preta', 139.90, 40);

-- Carrinhos
CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY
);

INSERT INTO carts DEFAULT VALUES;
INSERT INTO carts DEFAULT VALUES;

-- Itens do carrinho
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT,
    product_id BIGINT,
    quantity INT
);