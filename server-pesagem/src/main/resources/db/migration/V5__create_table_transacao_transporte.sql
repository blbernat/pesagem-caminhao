-- Create table transacao_transporte (depends on caminhao, tipo_grao, balanca, filial)
CREATE TABLE IF NOT EXISTS transacao_transporte (
    id BIGSERIAL PRIMARY KEY,
    caminhao_id BIGINT NOT NULL,
    tipo_grao_id BIGINT NOT NULL,
    balanca_id BIGINT NOT NULL,
    filial_id BIGINT NOT NULL,
    peso_bruto DECIMAL(10,2),
    tara DECIMAL(10,2),
    peso_liquido DECIMAL(10,2),
    custo_carga DECIMAL(14,2),
    inicio TIMESTAMP NOT NULL,
    fim TIMESTAMP,
    CONSTRAINT fk_caminhao_transacao FOREIGN KEY (caminhao_id) REFERENCES caminhao(id),
    CONSTRAINT fk_tipo_grao_transacao FOREIGN KEY (tipo_grao_id) REFERENCES tipo_grao(id),
    CONSTRAINT fk_balanca_transacao FOREIGN KEY (balanca_id) REFERENCES balanca(id),
    CONSTRAINT fk_filial_transacao FOREIGN KEY (filial_id) REFERENCES filial(id)
);

-- Insert initial data
INSERT INTO transacao_transporte (
    id, caminhao_id, tipo_grao_id, balanca_id, filial_id, 
    peso_bruto, tara, peso_liquido, custo_carga, inicio, fim
) VALUES (
    1, 1, 1, 1, 1, 
    10500.0, 7000.0, 3500.0, 352975, 
    '2025-11-22 18:45:05.123', '2025-11-22 18:48:08.351'
);

INSERT INTO transacao_transporte (
    id, caminhao_id, tipo_grao_id, balanca_id, filial_id,
    peso_bruto, tara, peso_liquido, custo_carga, inicio, fim
) VALUES (
    2, 2, 1, 1, 1,
    9500.0, 6500.0, 3000.0, 297863,
    '2025-11-24 09:17:45.123', '2025-11-24 9:18:08.351'
);

-- Reset sequence
SELECT setval('transacao_transporte_id_seq', (SELECT MAX(id) FROM transacao_transporte));
