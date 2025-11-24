-- Create table filial
CREATE TABLE IF NOT EXISTS filial (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- Insert initial data
INSERT INTO filial (id, nome) VALUES
(1, 'Filial Matriz'),
(2, 'Filial Norte'),
(3, 'Filial Sul');

-- Reset sequence
SELECT setval('filial_id_seq', (SELECT MAX(id) FROM filial));
