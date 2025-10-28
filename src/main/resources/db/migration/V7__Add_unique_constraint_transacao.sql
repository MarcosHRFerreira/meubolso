-- Adiciona constraint única composta para blindar duplicidade de transações
-- Campos: conta_id, data, descricao, valor, tipo, categoria
-- Usa bloco defensivo para evitar erro caso já exista
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uq_transacao_composta'
    ) THEN
        ALTER TABLE transacao
            ADD CONSTRAINT uq_transacao_composta
            UNIQUE (conta_id, data, descricao, valor, tipo, categoria);
    END IF;
END $$;