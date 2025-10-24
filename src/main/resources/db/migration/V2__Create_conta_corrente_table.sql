CREATE TABLE conta_corrente (
    id BIGSERIAL PRIMARY KEY,
    banco VARCHAR(100) NOT NULL,
    agencia VARCHAR(20),
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_conta_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Índice para otimizar consultas por usuário
CREATE INDEX idx_conta_usuario ON conta_corrente(usuario_id);