-- Adiciona coluna para referência de fatura (ano+mes) em transacao
ALTER TABLE transacao
    ADD COLUMN IF NOT EXISTS anomesref VARCHAR(10);

-- Opcional: índice para consultas por ano/mês de fatura
-- CREATE INDEX IF NOT EXISTS idx_transacao_anomesref ON transacao(anomesref);