# API de Sistema de Chamados

API REST em arquitetura em camadas (Controller, Service, Repository, Entity, DTO e Exception) com Java 21, Spring Boot, Maven, Spring Data JPA e banco H2 em memória.

## Executar

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. O console do H2 fica em `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:chamadosdb`, usuário `sa`, senha em branco).

## Exemplos de consulta de chamados

Todos os filtros são opcionais e podem ser combinados. A resposta é paginada (`content`, `totalElements`, `totalPages`, `number`, `size`).

| Consulta | Exemplo |
|---|---|
| Listar todos | `GET /chamados` |
| Por status | `GET /chamados?status=ABERTO` |
| Por prioridade | `GET /chamados?prioridade=ALTA` |
| Por cliente | `GET /chamados?clienteId=1` |
| Combinando filtros | `GET /chamados?status=ABERTO&prioridade=ALTA&clienteId=1` |
| Paginação | `GET /chamados?page=0&size=5` |
| Ordenação | `GET /chamados?sort=dataAbertura,desc` |
| Chamados de um cliente | `GET /clientes/1/chamados?page=0&size=10&sort=prioridade,asc` |

Valores de `status`: `ABERTO`, `EM_ATENDIMENTO`, `AGUARDANDO_CLIENTE`, `FECHADO`.
Valores de `prioridade`: `BAIXA`, `MEDIA`, `ALTA`, `CRITICA`.

Exemplo com `curl`:

```bash
curl -i "http://localhost:8080/chamados?status=ABERTO&prioridade=ALTA&page=0&size=5&sort=dataAbertura,desc"
```

## Documentação da API (OpenAPI/Swagger)

Com a aplicação rodando:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Especificação OpenAPI (JSON): `http://localhost:8080/v3/api-docs`

## Executar com PostgreSQL (profile `postgres`)

Por padrão a aplicação usa H2 em memória. Para usar PostgreSQL, suba um banco e ative o profile:

```bash
docker run --name chamados-db -e POSTGRES_DB=chamadosdb -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

Valores padrão: `jdbc:postgresql://localhost:5432/chamadosdb`, usuário `postgres`, senha `postgres`.
Para outro banco, defina as variáveis `DB_URL`, `DB_USER` e `DB_PASSWORD`.

## Testes

```bash
./mvnw test
```
