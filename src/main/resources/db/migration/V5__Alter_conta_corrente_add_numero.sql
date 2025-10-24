-- Adiciona coluna numero à conta_corrente
ALTER TABLE conta_corrente ADD COLUMN IF NOT EXISTS numero VARCHAR(20);

-- Índice opcional para buscas por número
CREATE INDEX IF NOT EXISTS idx_conta_numero ON conta_corrente(numero);