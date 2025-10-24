-- Criação da tabela de usuários
CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    role VARCHAR(255) CHECK (role IN ('ADMIN','USER')),
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    password_changed_at TIMESTAMP(6),
    password_changed_by VARCHAR(255)
);


-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_usuario_login ON usuario(login);
CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario(email);
CREATE INDEX IF NOT EXISTS idx_usuario_created_at ON usuario(created_at);
CREATE INDEX IF NOT EXISTS idx_usuario_password_changed_at ON usuario(password_changed_at);