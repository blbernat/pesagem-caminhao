-- Create table tipo_grao
CREATE TABLE IF NOT EXISTS tipo_grao (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco_ton DECIMAL(10,2) NOT NULL
);

-- Insert initial data
INSERT INTO tipo_grao (id, nome, preco_ton) VALUES
(1, 'Soja', 100.85),
(2, 'Milho', 158.20),
(3, 'Trigo', 179.65),
(4, 'Cevada', 209.10);

-- Reset sequence
SELECT setval('tipo_grao_id_seq', (SELECT MAX(id) FROM tipo_grao));
