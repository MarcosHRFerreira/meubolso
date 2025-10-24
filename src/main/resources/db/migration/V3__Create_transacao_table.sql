CREATE TABLE transacao (
    id BIGSERIAL PRIMARY KEY,
    data DATE NOT NULL,
    descricao VARCHAR(255),
    valor NUMERIC(15,2) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    conta_id BIGINT NOT NULL,
    CONSTRAINT fk_transacao_conta FOREIGN KEY (conta_id) REFERENCES conta_corrente(id)
);
-- Índices para otimizar consultas por data, conta e categoria
CREATE INDEX idx_transacao_data ON transacao(data);
CREATE INDEX idx_transacao_conta ON transacao(conta_id);
