-- Inserir usuário padrão
INSERT INTO usuario (login, password, email, role, created_at)
VALUES (
    'admin',
    '$2a$10$hashdeexemplo1234567890abcdef',
    'admin@meubolso.com',
    'ADMIN',
    CURRENT_TIMESTAMP
);


-- Inserir conta corrente exemplo
INSERT INTO conta_corrente (banco, agencia, usuario_id)
VALUES ('Banco do Brasil', '1234', (SELECT id FROM usuario WHERE login='admin'));

-- Inserir transações exemplo
INSERT INTO transacao (data, descricao, valor, tipo, conta_id)
VALUES 
('2025-10-01', 'Supermercado', 250.00, 'DEBITO', 1),
('2025-10-03', 'Uber para o trabalho', 32.50, 'DEBITO', 1),
('2025-10-05', 'Salário', 5000.00, 'CREDITO', 1),
('2025-10-07', 'Cinema com amigos', 45.00, 'DEBITO', 1);
