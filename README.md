# Pesagem de caminhões

Sobre o projeto
----------------
Projeto backend em Java Spring Boot para receber leituras de balanças ESP32, estabilizar as leituras e registrar transações de transporte.

Estrutura do projeto
-------------------------------------------------------

```
pesagem-caminhao/
├─ README.md                          # Arquivo que você está lendo
├─ settings.gradle
├─ build/                              # Artefatos de build do workspace (reports, etc.)
├─ gradle/                             # Gradle wrapper (configs)
├─ client-pesagem-balanca/             # Cliente/CLI relacionado ao fluxo das balanças
│  ├─ build.gradle
│  ├─ gradlew
│  ├─ gradlew.bat
│  ├─ src/
│  │  ├─ main/java/com/desafio/balanca/  # Código cliente (ex.: Simulador da balança)
│  │  └─ resources/
│  └─ build/                           # Artefatos gerados (jar, distribuições)
└─ server-pesagem/                     # API e lógica do servidor (principal)
	├─ build.gradle
	├─ gradlew
	├─ gradlew.bat
	├─ src/
	│  ├─ main/
	│  │  ├─ java/com/desafio/pesagem/
	│  │  │  ├─ entities/                # Entidades (Caminhao, Balanca, Filial, TipoGrao, ...)
	│  │  │  ├─ repositories/            # Implementações JDBC (JdbcClient)
	│  │  │  ├─ service/                 # Regras de negócio (PesagemService, etc.)
	│  │  │  └─ controller/              # Controllers REST (endpoints)
	│  │  └─ resources/
	│  │     ├─ application.properties   # Configurações (DB, porta, etc.)
	│  │     ├─ data.sql                 # Dados de exemplo (filial, balança, caminhao...)
	│  │     └─ db/migration/            # Scripts de migração/seed (V1__, V2__, ...)
	│  └─ test/                          # (se houver) testes unitários/integracao
	└─ build/                            # Artefatos de build do módulo server

```

## Como Configurar
### Requisitos
- Java 17 ou superior
- Docker Desktop (para PostgreSQL via Testcontainers)
- Gradle 8.5 (incluído via Gradle Wrapper)

### Executar o servidor:

**Pré-requisito:** Docker Desktop rodando

```bash
cd server-pesagem
./gradlew bootRun
```

Ou no Windows:
```cmd
cd server-pesagem
gradlew.bat bootRun
```

O Testcontainers irá iniciar automaticamente um container PostgreSQL. 
Ao iniciar, a API fica disponível por padrão em http://localhost:8080 (verifique `application.properties` se a porta foi alterada).

### Build:

```bash
cd server-pesagem
./gradlew build
```

### Endpoints
**Registrar Pesagem:**
```http
POST http://localhost:8080/api/v1/pesagens
Content-Type: application/json

{
    "id": "BAL001",
    "plate": "ABC1D34",
    "weight": 7000.0
}
```

**Listar todas as pesagens:**
```http
GET http://localhost:8080/api/v1/pesagens
```

**Buscar pesagem por filial, caminhão ou tipo de grão:**
```http
GET http://localhost:8080/api/v1/pesagens?filial=filial matriz
GET http://localhost:8080/api/v1/pesagens?caminhao=ABC1D23
GET http://localhost:8080/api/v1/pesagens?grao=soja
```

**Buscar custo por filial, caminhão ou tipo de grão:**
```http
GET http://localhost:8080/api/v1/pesagens/custo?filial=filial matriz
GET http://localhost:8080/api/v1/pesagens/custo?caminhao=ABC1D23
GET http://localhost:8080/api/v1/pesagens/custo?grao=soja
```

### Executar o Simulador da Balança:
```bash
cd client-pesagem-balanca
./gradlew run
```

Ou no Windows:
```cmd
cd client-pesagem-balanca
gradlew.bat run
```

### Criar JAR executável:

```bash
cd client-pesagem-balanca
./gradlew fatJar
```

JAR criado em: `build/libs/client-pesagem-balanca-1.0.0-all.jar`

Executar:
```bash
java -jar build/libs/client-pesagem-balanca-1.0.0-all.jar
```

### Lógica do simulador
- `SimuladorBalanca` inicia com um `currentWeight` aleatório entre 70% e 130% do `targetWeight`.
- A cada 500 ms (`MainController` agenda leituras a cada 500 ms), `simulateNextReading()` calcula um ajuste na diferença entre peso atual e alvo, aplicando um fator aleatório entre 0.3 e 0.7 para reduzir essa diferença, e adiciona um ruído aleatório (variável `VARIATION_FACTOR`).
- Se o `currentWeight` estiver dentro de 1% do `targetWeight` por `STABILIZATION_THRESHOLD` leituras consecutivas (atualmente 3), a leitura é marcada como estabilizada.
- Ao detectar estabilização, o `MainController` envia um JSON ao servidor via `BalancaClient` com os campos `id`, `plate` e `weight`, para o endpoint `POST /api/v1/pesagens`.

#### Formato enviado
```json
{
	"id": "BAL001",
	"plate": "ABC1D23",
	"weight": 7000.00
}
```

## Build de todos os projetos

Da raiz do projeto:

```bash
./gradlew build
```

Ou no Windows:
```cmd
gradlew.bat build
```

## Como usar

### Opção 1: Teste completo com interface Gráfica

```bash
# 1. Garantir que o Docker está rodando

# 2. Terminal 1: Execute o server-pesagem (servidor)
cd server-pesagem
./gradlew bootRun

# 3. Terminal 2: Execute o client-pesagem_balanca (simulador EPS32)
cd client-pesagem-balanca
./gradlew run
```

Preencha os campos na interface e clique em "Iniciar Pesagem"!

### Opção 2: Teste via API (curl)

```bash
# 1. Execute o Weigh Hub
cd server-pesagem
./gradlew bootRun

# 2. Teste a API
curl -X POST http://localhost:8080/api/v1/pesagens \
  -H "Content-Type: application/json" \
  -d '{"id":"BAL001","plate":"ABC1D34","weight":7000}'
```

## API e documentação
- O projeto expõe endpoints REST para ingestão de leituras das balanças e consultas de transações.
- Utlize o Swagger/OpenAPI para verificar a documentação e versionamento da API em : `http://localhost:8080/swagger-ui/index.html`.

## Como contribuir

1. Fork e clone o repositório.
2. Crie uma branch de feature: `git checkout -b feature/minha-melhoria`.
3. Faça commits pequenos e claros.
4. Abra um Pull Request detalhando as mudanças.

## Testes e qualidade

Utilize o Collections do Postman para verificar os endpoints da aplicação (arquivo `collections.json`)

## Sugestões de melhorias
- Consolidar carregamento de relacionamentos para evitar múltiplas queries por entidade (usar JOIN ou JPA com fetch joins).
- Cobrir a lógica de estabilização de peso com testes automatizados.
- Utilizar o conceito de Arquitetura em Camadas (application, domain e infrastructure) para melhor organização e separação de código.
- Fazer tratamentos de exception específicos (`ControllerExceptionHandler`). 
- Cobrir mais regras de negócio e tratamento de erros.
- Criar mais endpoints de CRUD para as entidades (adicionar balança, deletar caminhão, visualizar filiais)
- Criar endpoints de lucro.
- Corrigir o retorno do método GET de busca de transações para retornar um DTO, e não uma entidaed.

## Licença
Projeto distribuído sob licença MIT (ver arquivo `LICENSE` se presente).
