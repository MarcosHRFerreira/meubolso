# Estudo: Importação de Transações via CSV

Este estudo descreve como importar transações financeiras a partir de arquivos CSV enviados pelo endpoint `POST /cargacontacorrente` do `ContaCorrenteController`. A importação é encaminhada ao `ImportaArquivoServiceImpl`, que interpreta o conteúdo conforme a categoria financeira e persiste usando o `TransacaoRepository`.

## Objetivos
- Receber um arquivo CSV e a categoria financeira via controller.
- Interpretar formatos distintos por categoria (conta corrente e cartão de crédito).
- Converter datas e valores com variações de separador (`;` e `,`) e decimal com vírgula.
- Persistir `TransacaoEntity` vinculado à `ContaCorrenteEntity` informada.
- Registrar a categoria financeira na transação.

## Endpoint
- `POST /importacao-arquivo/carga`
  - Parâmetros:
    - `file`: `multipart/form-data` (CSV)
    - `tipo`: `enum` (`CONTACORRENTE` ou `CARTAOCREDITO`)
    - `contaId`: `long` (identificador da conta corrente destino)
    - `anomesref`: `string` (formato `YYYY-MM`, obrigatório quando `tipo=CARTAOCREDITO`)
  - Validações específicas:
    - Se `tipo=CARTAOCREDITO` e `anomesref` não for enviado, responde `400 Bad Request`.
    - Se `anomesref` não seguir `YYYY-MM` com mês entre `01..12`, responde `400 Bad Request` com mensagem: `anomesref deve estar no formato YYYY-MM (mês 01..12)`.
  - Fluxo:
    1. Controller valida parâmetros e delega para `ImportaArquivoService.importar(...)`.
    2. Service resolve `ContaCorrenteEntity` via `ContaCorrenteRepository`.
    3. Faz o parsing linha a linha, cria e salva `TransacaoEntity` via `TransacaoRepository`.
  - Retorno:
    - `ImportacaoResultadoDto` com duas listas:
      - `inseridos`: lista de `TransacaoResumoDto` (id, contaId, data, descricao, valor, tipo, categoria, anomesref)
      - `erros`: lista de `LinhaErroDto` (linhaNumero, linhaConteudo, motivo)

## Formatos de Arquivo
- Categoria `CONTACORRENTE`:
  - Exemplo de linha: `09/10/2025;PIX ORIGEM CARTAO 5636;17,00`
  - Delimitador: `;`
  - Ordem: `data;descricao;valor`
  - Data: `dd/MM/yyyy`
  - Valor: decimal com vírgula (ex.: `17,00` → `17.00`)

- Categoria `CARTAO_CREDITO`:
  - Exemplo de linha: `2025-10-25,JAU SERVE LJ,32,98`
  - Delimitador: `,`
  - Ordem: `data,descricao,valor` (se houver mais vírgulas, considera a primeira como data e a última como valor; o meio é a descrição)
  - Data: `yyyy-MM-dd`
  - Valor: decimal com vírgula (ex.: `32,98` → `32.98`)
  - Campo adicional: `anomesref` fornecido via parâmetro do endpoint (ex.: `2025-10`) e persistido em `Transacao.anomesref`.

Obs.: Há divergências nos exemplos originais. O serviço é tolerante: para cartão de crédito, se a linha tiver mais de 3 tokens, usa o primeiro como data, o último como valor e o restante como descrição (reunidos por vírgula). Para conta corrente, assume exatamente 3 tokens separados por `;`.

## Modelo de Dados
- `TransacaoEntity` passa a incluir `CategoriaFinanceira` (enum) persistido como `STRING` na coluna `categoria`.
- Continua contendo `data`, `valor`, `descricao`, `tipo` (mantido para compatibilidade) e relação obrigatória com `ContaCorrenteEntity`.

## Parsing e Normalização
- Data:
  - `CONTACORRENTE`: `DateTimeFormatter.ofPattern("dd/MM/yyyy")`
  - `CARTAO_CREDITO`: `DateTimeFormatter.ISO_LOCAL_DATE`
- Valor:
  - Remove separadores de milhar (`.`) e troca vírgula por ponto.
  - Ex.: `1.234,56` → `1234.56`.

## Persistência
- Para cada linha válida:
  - Cria `TransacaoEntity` com `data`, `valor`, `descricao`, `tipo` (ex.: `DESPESA`) e `categoria`.
  - Quando `categoria=CARTAOCREDITO`, define `anomesref` recebido do controller.
  - Associa `contaCorrente` obtida por `contaId`.
  - Persiste via `TransacaoRepository.save(entity)`.

## Erros e Validações
- Linhas inválidas (tokens insuficientes, datas inválidas, valores não numéricos) são ignoradas ou registradas em log.
- Se `contaId` não existir, lança `EntityNotFoundException`.
- Se `categoria` não for reconhecida, retorna `400 Bad Request`.

## Próximos Passos
- Ajustar testes para cobrir ambos formatos.
- (Opcional) Suportar arquivos grandes com streaming.
- (Opcional) Retornar relatório de importação (linhas processadas, ignoradas, erros).