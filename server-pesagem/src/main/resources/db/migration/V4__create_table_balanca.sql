-- Create table balanca (depends on filial)
CREATE TABLE IF NOT EXISTS balanca (
    id BIGSERIAL PRIMARY KEY,
    codigo_hardware VARCHAR(50) NOT NULL UNIQUE,
    filial_id BIGINT NOT NULL,
    CONSTRAINT fk_filial_balanca FOREIGN KEY (filial_id) REFERENCES filial(id)
);

-- Insert initial data
INSERT INTO balanca (id, codigo_hardware, filial_id) VALUES
(1, 'BAL001', 1),
(2, 'BAL002', 1),
(3, 'BAL003', 2),
(4, 'BAL004', 2),
(5, 'BAL005', 3),
(6, 'BAL006', 3);

-- Reset sequence
SELECT setval('balanca_id_seq', (SELECT MAX(id) FROM balanca));
