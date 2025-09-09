-- Remover tabelas na ordem correta para evitar problemas de FK
DROP TABLE IF EXISTS cart_items CASCADE;
DROP TABLE IF EXISTS carts CASCADE;
DROP TABLE IF EXISTS products CASCADE;

-- Tabela de produtos
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10,2),
    quantity INT
);

INSERT INTO products (name, price, quantity) VALUES
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

-- Tabela de carrinhos
CREATE TABLE carts (
    id BIGSERIAL PRIMARY KEY
);

-- Insere carrinhos iniciais
INSERT INTO carts DEFAULT VALUES;
INSERT INTO carts DEFAULT VALUES;
INSERT INTO carts DEFAULT VALUES;
INSERT INTO carts DEFAULT VALUES;
INSERT INTO carts DEFAULT VALUES;

-- Itens do carrinho
CREATE TABLE cart_items (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT REFERENCES carts(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id),
    quantity INT
);
