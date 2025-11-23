# Desafio técnico backend

## Sobre o Projeto
Uma empresa de transporte de grãos, com diversas filiais pelo Brasil, possui um parque
de balanças para pesagem de caminhões carregados com grãos. Como parte da
digitalização e otimização de seus processos, a empresa implementou um sistema baseado
em ESP32, integrados a cada balança e com câmera LPR, responsáveis por enviar leituras
de peso automaticamente para um servidor central da empresa.

Neste contexto, a empresa precisa receber, processar e armazenar os dados das balanças
de modo eficiente e confiável, possibilitando calcular custos e identificar
oportunidades de lucro no transporte de grãos.

O objetivo deste projeto é criar uma solução robusta para ingestão, estabilização e armazenamento
das leituras de peso, conforme os requisitos descritos no desafio.

## Desafio
### 1. Cadastros
Cadastros para armazenar:
- Caminhão, Tipo de grão, Filial, Balança, Transação de transporte
- Transação de transporte é a transação de compra e pesagem de grãos de um tipo para um caminhão, inicio e fim.

### 2. Recepção dos Dados das Balanças
Implemente um endpoint HTTP capaz de receber as requisições do ESP32 conforme o JSON
acima (`plate`, `weight`). Considere que todas as balanças podem enviar dados
simultaneamente.

### 3. Persistência com Estabilização
Salve os dados de pesagem **apenas quando o peso estiver estabilizado**. Elabore e
descreva uma estratégia para identificar automaticamente o momento em que a balança
está estabilizada.

## Como Configurar
### Pré-requisitos
- Java JDK 24
- Maven
- MySQL
- Docker
- Postman

### Instalação
1. Clone o repositório: `git clone https://github.com/blbernat/desafio-tecnico-backend.git`
2. Instale as dependências e rodar o projeto com o docker, conforme passo a passo do próximo tópico:
3. Iniciar a aplicação manualmente via IDE rodando a classe main do Java `TransporteApplication`
4. Acesse a aplicação em: http://localhost:8080/
5. Acesse o banco de dados da aplicação em: http://localhost:8080/ com usuário e senha definidos no arquivo `application.properties`

### Docker
* #### Para utilizar a aplicação via docker é necessário gerar o `.jar` da aplicação. Para isso faça os seguintes passos:
1. Acessar o diretório `desafio-tecnico-backend/docker`
2. Rodar o comando `docker-compose up` (caso queira acompanhar os logs) ou `docker-compose up -d`
3. A aplicação estará rodando na porta http://localhost:8080

* ####  Caso queira rodar a aplicação local e utilizar o banco via docker:
1. Acessar o diretório `desafio-tecnico-backend/docker`
2. Rodar o comando `docker compose -f docker-compose-mysql.yml up` (caso queira acompanhar os logs) ou `docker compose -f docker-compose-mysql.yml up -d`
3. Iniciar a aplicação manualmente ou via IDE

## Descrição da arquitetura
### Padrão MVC 

#### Model
Contém as entidades, DTOs e mapeamentos do banco. Representa os dados e regras básicas de validação.
#### Repository
Responsável por toda comunicação com o MySQL, utilizando Spring Data JPA ou JDBC Template. Neste projeto foi utilizado o JDBC.
#### Service
Centraliza as regras de negócio, como por exemplo os cálculos de tara, peso líquido e custo; estabilização de pesagem e validações das transações
#### Controller
Ela é responsável por mapear endpoints (REST), validar a entrada do usuário, acionar métodos da camada Service e retornar respostas padronizadas (ProblemDetail, DTOs, ResponseEntity)

### Banco de Dados – MySQL
O MySQL armazena todas as informações do sistema, incluindo caminhões, tipos de grão, filiais, balanças e transações de pesagem.
A conexão usa variáveis de ambiente via Docker Compose.

### Execução com Docker
A aplicação roda totalmente em containers Docker:

- App (Spring Boot): construído via Dockerfile com multi-stage build para gerar o JAR e executar na porta 8080.
- MySQL: subido como container separado, com volume persistente para armazenar os dados.
- Docker-compose: orquestra os serviços e garante que o Spring Boot só inicie após o MySQL estar disponível.

## Documentação da API (Swagger)
- Após iniciar o projeto, acesse a documentação Swagger em: http://localhost:8080/swagger-ui/index.html

## Testes com Collections do Postman

Para verificar se a aplicação está rodando corretamente e ter acesso aos endpoints, utilize as Collections no Postman, conforme os seguintes passos:
1. Salve num arquivo local o conteúdo do arquivo `collections`
2. Abra o Postman via desktop ou pela web (https://www.postman.com/)
3. Na aba "Collections", clique na opção "Import" e selecione o arquivo `.json` salvo no primeiro passo
4. Cada endpoint possui testes válidos e inválidos que já estão prontos para serem executados!

## Sugestão de melhoria
- Fazer o controle de versionamento. Este pode ser visualizado pela documentação no Swagger. Para melhor entendimento, leia o exemplo: https://dzone.com/articles/versioning-rest-api-with-spring-boot-and-swagger
- Testes unitários
- Deploy em nuvem
- Fazer tratamentos de exception específicos (`ControllerExceptionHandler`)

## Como Contribuir
Contribuições são sempre bem-vindas! Veja como:

1. Fork o projeto
2. Crie sua Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a Branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença
Distribuído sob a licença MIT. Veja `LICENSE` para mais informações.