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
cd weigh-hub
./gradlew bootRun
```

Ou no Windows:
```cmd
cd weigh-hub
gradlew.bat bootRun
```

O Testcontainers irá iniciar automaticamente um container PostgreSQL. 
Ao iniciar, a API fica disponível por padrão em http://localhost:8080 (verifique `application.properties` se a porta foi alterada).

### Build:

```bash
cd weigh-hub
./gradlew build
```

### Endpoints

descrever endpoint !!

### Executar o Simulador da Balança:
```bash
cd duo-weigh
./gradlew run
```

Ou no Windows:
```cmd
cd duo-weigh
gradlew.bat run
```

### Criar JAR executável:

```bash
cd duo-weigh
./gradlew fatJar
```

JAR criado em: `build/libs/duo-weigh-1.0.0-all.jar`

Executar:
```bash
java -jar build/libs/duo-weigh-1.0.0-all.jar
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
cd weigh-hub
./gradlew bootRun

# 3. Terminal 2: Execute o client-pesagem_balanca (simulador EPS32)
cd duo-weigh
./gradlew run
```

Preencha os campos na interface e clique em "Iniciar Pesagem"!

### Opção 2: Teste via API (curl)

```bash
# 1. Execute o Weigh Hub
cd weigh-hub
./gradlew bootRun

# 2. Teste a API
curl -X POST http://localhost:8080/api/v1/weighing/records \
  -H "Content-Type: application/json" \
  -d '{"scaleId":"Balance_1","plate":"ABC1D34","weight":15000}'
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

##Licença
Projeto distribuído sob licença MIT (ver arquivo `LICENSE` se presente).
