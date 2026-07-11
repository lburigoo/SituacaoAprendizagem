# AGENTS.md - Sistema de Gerenciamento de EPIs

## Objetivo do Projeto

Sistema web para controle de Equipamentos de Proteção Individual (EPIs) em empresas de construção civil. Permite cadastrar colaboradores, gerenciar EPIs e controlar empréstimos/devoluções com atualização automática de estoque.

## Tecnologias Utilizadas

- **Java 25** - Linguagem principal
- **Spring Boot 3.4.4** - Framework backend
- **Spring Web** - API REST
- **Spring Data JPA** - Persistência e relacionamentos
- **MySQL 9.7** - Banco de dados relacional
- **Maven** - Gerenciamento de dependências
- **HTML5, CSS3, JavaScript Vanilla** - Frontend (sem frameworks)
- **Maven Wrapper** - Para build sem Maven instalado globalmente

## Estrutura das Pastas

```
trabalho-sa/
├── pom.xml                    # Configuração Maven
├── mvnw.cmd                   # Maven Wrapper (Windows)
├── .mvn/wrapper/              # Arquivos do Maven Wrapper
├── database/
│   └── script.sql             # Script de criação do banco MySQL
├── src/main/java/com/empresa/epi/
│   ├── EpiApplication.java    # Classe principal Spring Boot
│   ├── config/
│   │   └── CorsConfig.java    # Configuração CORS
│   ├── entity/
│   │   ├── Colaborador.java   # Entidade Colaborador
│   │   ├── Epi.java           # Entidade EPI
│   │   └── Emprestimo.java    # Entidade Empréstimo
│   ├── repository/
│   │   ├── ColaboradorRepository.java
│   │   ├── EpiRepository.java
│   │   └── EmprestimoRepository.java
│   ├── service/
│   │   ├── ColaboradorService.java
│   │   ├── EpiService.java
│   │   └── EmprestimoService.java
│   └── controller/
│       ├── ColaboradorController.java
│       ├── EpiController.java
│       └── EmprestimoController.java
└── src/main/resources/
    ├── application.properties  # Configurações do banco/servidor
    └── static/
        ├── index.html          # Página inicial
        ├── css/
        │   └── style.css       # Estilos CSS
        ├── js/
        │   ├── colaboradores.js
        │   ├── epis.js
        │   └── emprestimos.js
        └── pages/
            ├── colaboradores.html
            ├── epis.html
            └── emprestimos.html
```

## Como Executar

1. Abrir o terminal na pasta `trabalho-sa`
2. Executar: `.\mvnw.cmd spring-boot:run`
3. Aguardar o build e inicialização
4. Acessar: http://localhost:8080

## Pré-requisitos

- Java JDK 25 (JAVA_HOME configurado)
- MySQL rodando (usuário: root, senha: 1234)
- Banco `trabalho_sa` criado

## Como Adicionar Novas Funcionalidades

1. **Nova entidade:** Criar classe em `entity/`, repository em `repository/`, service em `service/` e controller em `controller/`
2. **Nova página:** Criar HTML em `static/pages/`, JS em `static/js/` e adicionar link no menu
3. **Novo campo:** Adicionar na entidade, atualizar formulário HTML e JS

## Boas Práticas Utilizadas

- Separação em camadas (entity, repository, service, controller)
- Validação com Jakarta Validation
- Tratamento de erros com respostas JSON
- Exclusão lógica (inativação) para colaboradores e EPIs
- Atualização automática de estoque nos empréstimos
- CORS configurado para desenvolvimento
- Código em português com nomenclatura clara

## Como Continuar Evoluindo o Projeto

- Adicionar autenticação/login
- Implementar relatórios (EPIs mais emprestados, colaboradores com pendências)
- Adicionar paginação nas listagens
- Melhorar validações no frontend
- Implementar backup automático do banco
- Adicionar testes unitários com JUnit
