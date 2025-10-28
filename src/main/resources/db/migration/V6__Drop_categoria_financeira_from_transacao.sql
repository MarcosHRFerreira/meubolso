-- Remove coluna obsoleta 'categoria_financeira' e seu índice
DROP INDEX IF EXISTS idx_transacao_categoria;
ALTER TABLE transacao
    DROP COLUMN IF EXISTS categoria_financeira;