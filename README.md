# MeuBolso

Aplicação Spring Boot para controle de despesas com importação de transações de Conta Corrente e Cartão de Crédito via arquivos CSV. Inclui deduplicação simples, validações de upload e documentação via Swagger UI.

## Visão Geral

- Framework: Spring Boot 3 (Java 17)
- Banco: PostgreSQL (via Flyway para migrações)
- Documentação: Springdoc OpenAPI (Swagger UI)
- Build: Maven Wrapper

## Requisitos

- Java 17
- PostgreSQL 15+ (local ou container)
- Maven (opcional, usamos o Maven Wrapper incluído no projeto)

## Configuração do Banco de Dados

Por padrão, a aplicação usa as seguintes configurações (em `src/main/resources/application.yaml`):

```
spring.datasource.url=jdbc:postgresql://localhost:5432/meubolso
spring.datasource.username=postgres
spring.datasource.password=admin
```

Você pode sobrescrever via variáveis de ambiente:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

Exemplo com Docker (PostgreSQL):

```
docker run -d --name pg-meubolso -e POSTGRES_PASSWORD=admin -e POSTGRES_USER=postgres -e POSTGRES_DB=meubolso -p 5432:5432 postgres:15
```

As migrações Flyway são aplicadas automaticamente na inicialização.

## Execução Local

Compilar:

- Windows (PowerShell): `./mvnw -DskipTests package`
- Linux/macOS: `./mvnw -DskipTests package`

Executar (porta 8083):

```
java -jar target/meubolso-0.0.1-SNAPSHOT.jar --server.port=8083
```

Sem parâmetro, a porta padrão é `8080`.

## Documentação (Swagger)

- Swagger UI: `http://localhost:8083/swagger-ui/index.html`

## Endpoints Principais

- `POST /importacao-arquivo/carga` — Importa um arquivo CSV com transações.
  - Formato: `multipart/form-data`
  - Campos:
    - `file`: arquivo CSV
    - `tipo`: `CONTACORRENTE` ou `CARTAOCREDITO`
    - `contaId`: ID da conta corrente destino
    - `anomesref` (opcional; obrigatório para `CARTAOCREDITO`): `YYYY-MM`

Exemplos de requisição (PowerShell/curl):

```
curl -X POST "http://localhost:8083/importacao-arquivo/carga" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@conta.csv;type=text/csv" \
  -F "tipo=CONTACORRENTE" \
  -F "contaId=1"

curl -X POST "http://localhost:8083/importacao-arquivo/carga" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@cartao.csv;type=text/csv" \
  -F "tipo=CARTAOCREDITO" \
  -F "contaId=1" \
  -F "anomesref=2025-01"
```

## Formatos de CSV

Conta Corrente (`;` como delimitador):

```
31/08/2024;TED RECEBIDA;1.234,56
01/09/2024;DEBITO AUTOMATICO;-42,90
```

- Data: `dd/MM/yyyy`
- Valor: aceita vírgula como decimal e ponto como milhar (`1.234,56` → `1234.56`).

Cartão de Crédito (`,` como delimitador):

```
2024-08-31,Amazon Marketplace,123.45
2024-09-01,Uber, Viagem 123,45.67
```

- Data: `YYYY-MM-DD`
- Descrição pode conter vírgulas internas; o último campo é sempre o valor.

Cabeçalho: linhas de cabeçalho comuns são ignoradas automaticamente (ex.: `data,lançamento,valor` ou `data;lançamento;valor`).
Encoding: recomendado `UTF-8`. BOM (UTF-8) é tratado.

## Validações de Upload

- Arquivo não vazio e tamanho máximo de 10MB.
- `content-type` aceito: `text/csv`, `application/vnd.ms-excel`, `application/octet-stream`.
- Encoding: leitura em UTF-8, ignora BOM.
- Limite de linhas: 50.000 (linhas vazias são ignoradas).
- `anomesref` obrigatório para `CARTAOCREDITO` (`YYYY-MM`).
- Sanitização de descrição (remove BOM/controle/espacos duplicados).
- Deduplicação: evita inserir transações idênticas (conta, data, descrição, valor, tipo, categoria).

Erros retornam `400` com mensagens claras (ex.: formato inválido, arquivo excede limite de linhas, etc.).

## Migrações (Flyway)

- Local das migrações: `classpath:db/migration`
- Convenção: `V<versao>__<descricao>.sql` (ex.: `V9__adiciona_coluna_anomesref.sql`)
- Baseline: versão `0` com `baseline-on-migrate: true`

## Desenvolvimento

- Rodar testes: `./mvnw test`
- Ajustar porta rapidamente: `--server.port=8083`
- Logs de SQL: habilitados (`spring.jpa.show-sql=true`, `hibernate.format_sql=true`).

## Segurança

Em desenvolvimento, o Spring Security pode exibir uma senha gerada no startup. Para ambientes produtivos, configure autenticação adequada (usuários, perfis e políticas) e desative credenciais padrão.

## Troubleshooting

- Erro de conexão ao banco: verifique se o PostgreSQL está rodando e as credenciais/URL.
- `Bad Request` ao importar: confira formato do CSV, categoria, `anomesref` (para cartão) e tamanho/linhas.
- Swagger 500: evite customizações de `@RequestBody` que conflitem com o Springdoc starter.

## Licença

Uso pessoal/educacional. Ajuste conforme necessário para seu contexto.