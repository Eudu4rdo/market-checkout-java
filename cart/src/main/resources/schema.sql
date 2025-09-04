DROP TABLE IF EXISTS products;

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE,
    quantity INT
);

INSERT INTO products (name, price, qtd) VALUES
('Camiseta Básica Branca', 49.90, 100),
('Camiseta Básica Preta', 49.90, 80),
('Calça Jeans Slim Azul', 129.90, 50),
('Calça Jeans Skinny Preta', 139.90, 40),
('Jaqueta Jeans Oversized', 199.90, 30),
('Jaqueta Corta-Vento', 179.90, 25),
('Moletom Cinza Estampado', 159.90, 35),
('Moletom Preto Liso', 149.90, 40),
('Bermuda Sarja Bege', 89.90, 60),
('Bermuda Jeans Azul Claro', 99.90, 55),
('Camisa Polo Azul Marinho', 79.90, 70),
('Camisa Polo Vermelha', 79.90, 65),
('Camisa Social Branca Slim', 119.90, 45),
('Camisa Social Azul Claro', 119.90, 40),
('Vestido Floral Curto', 149.90, 35),
('Vestido Longo Preto', 179.90, 25),
('Saia Jeans Média', 99.90, 50),
('Saia Midi Plissada', 129.90, 20),
('Tênis Casual Branco', 199.90, 30),
('Tênis Esportivo Preto', 249.90, 25);

DROP TABLE IF EXISTS carts;
CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY
);

INSERT INTO carts DEFAULT VALUES (1),(2),(3),(4),(5);

DROP TABLE IF EXISTS cart_items;
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT,
    product_id BIGINT,
    quantity INT,
    FOREIGN KEY (cart_id) REFERENCES carts(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);