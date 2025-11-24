-- Create table caminhao
CREATE TABLE IF NOT EXISTS caminhao (
    id BIGSERIAL PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE,
    tara DECIMAL(10,2) NOT NULL
);

-- Insert initial data
INSERT INTO caminhao (id, placa, tara) VALUES
(1, 'ABC1D23', 7000.0),
(2, 'XYZ9E88', 6500.0),
(3, 'JHK4F55', 8000.0),
(4, 'BRA2A19', 7200.0);

-- Reset sequence
SELECT setval('caminhao_id_seq', (SELECT MAX(id) FROM caminhao));
