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
