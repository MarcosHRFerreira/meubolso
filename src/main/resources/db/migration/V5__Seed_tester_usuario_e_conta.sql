-- Seed de usuário e conta corrente para testes
-- Cria o usuário 'tester' e uma conta corrente vinculada, caso não existam

WITH inserted_user AS (
    INSERT INTO usuario (login, password, email, role, created_at)
    SELECT 'tester', '{noop}senha123', 'tester@example.com', 'USER', CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE login = 'tester')
    RETURNING id
),
existing_user AS (
    SELECT id FROM usuario WHERE login = 'tester'
),
u AS (
    SELECT COALESCE((SELECT id FROM inserted_user), (SELECT id FROM existing_user)) AS id
)
INSERT INTO conta_corrente (banco, agencia, usuario_id, numero)
SELECT 'Banco Teste', '0001', u.id, '12345-6'
FROM u
WHERE NOT EXISTS (
    SELECT 1 FROM conta_corrente WHERE usuario_id = u.id AND numero = '12345-6'
);

-- Opcional: avançar a sequência para evitar conflitos se inserimos IDs manualmente (não necessário aqui)