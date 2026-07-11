# Documentação Completa - Sistema de Gerenciamento de EPIs

## ⚡ GUIA RÁPIDO - PARA EXECUTAR AGORA

### 1. Abrir no VS Code
- Abra o **Visual Studio Code**
- **File > Open Folder >** Selecione: `C:\Users\lucas\Downloads\Henriquedelegrego\trabalho-sa`
- Clique em **"Selecionar Pasta"**

### 2. Instalar extensões necessárias
No VS Code, vá na aba **Extensões** (Ctrl+Shift+X) e instale:
- **Extension Pack for Java** (Microsoft) - Clique em Install

### 3. Esperar VS Code carregar
O VS Code vai reconhecer o projeto automaticamente e baixar as dependências. Espere a barra de progresso terminar (pode levar 1-2 minutos na primeira vez).

### 4. Executar o projeto
**Opção A - Pelo TERMINAL (mais seguro):**
- Abra o terminal integrado: **Ctrl + '**
- Digite: `.\mvnw.cmd spring-boot:run`
- Pressione Enter

**Opção B - Pelo Spring Boot Dashboard:**
- Ctrl+Shift+P, digite "Spring Boot Dashboard"
- Clique no play ▶️ ao lado do projeto

### 5. Acessar
Abra o navegador: **http://localhost:8080**

---

## Sumário

1. [Instalação do Java](#1-instalação-do-java)
2. [Instalação do Maven](#2-instalação-do-maven)
3. [Configuração do MySQL](#3-configuração-do-mysql)
4. [Criação do Banco de Dados](#4-criação-do-banco-de-dados)
5. [Abrir o Projeto no VS Code](#5-abrir-o-projeto-no-vs-code)
6. [Executar o Projeto](#6-executar-o-projeto)
7. [Acessar pelo Navegador](#7-acessar-pelo-navegador)
8. [Testar as Funcionalidades](#8-testar-as-funcionalidades)
9. [Verificar Dados no MySQL](#9-verificar-dados-no-mysql)
10. [Solucionar Erros Comuns](#10-solucionar-erros-comuns)

---

## 1. Instalação do Java

### Verificar se o Java está instalado

Abra o terminal (PowerShell) e digite:

```
java -version
```

Se aparecer algo como `java version "25.0.2"`, o Java já está instalado.

### Instalar o Java (caso não tenha)

1. Acesse: https://www.oracle.com/java/technologies/downloads/
2. Baixe o JDK 25 para Windows (x64 Installer)
3. Execute o instalador e siga os passos
4. Após instalar, configure a variável JAVA_HOME:

   **Pelo PowerShell (como administrador):**
   ```
   [Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-25.0.2", "User")
   ```

5. Feche e reabra o terminal
6. Verifique:
   ```
   java -version
   ```

---

## 2. Instalação do Maven

### Verificar se o Maven está instalado

```
mvn -version
```

### Se não estiver instalado

Este projeto já vem com **Maven Wrapper** (arquivos `mvnw.cmd` e `.mvn/wrapper/`).

Você NÃO precisa instalar o Maven globalmente. Use `.\mvnw.cmd` no lugar de `mvn`.

O Maven Wrapper baixará automaticamente a versão correta na primeira execução.

---

## 3. Configuração do MySQL

### Verificar se o MySQL está instalado

1. Abra o **MySQL Command Line Client** ou o **MySQL Workbench**
2. Ou verifique no terminal:
   ```
   Get-Service -Name "MySQL*"
   ```
   Deve aparecer o serviço com status "Running"

### Instalar o MySQL (caso não tenha)

1. Acesse: https://dev.mysql.com/downloads/installer/
2. Baixe o MySQL Installer para Windows
3. Execute e escolha "Developer Default"
4. Durante a instalação:
   - Defina a senha do usuário root como `1234` (ou a senha que você escolheu)
   - Anote a senha para usar na configuração

---

## 4. Criação do Banco de Dados

### Opção 1: Usando o script SQL

1. Abra o terminal na pasta do projeto:
   ```
   cd C:\Users\lucas\Downloads\Henriquedelegrego\trabalho-sa
   ```

2. Execute o script SQL:
   ```
   "C:\Users\lucas\Downloads\mysql-enterprise-9.7.1_winx64_bundle\mysql-commercial-9.7.1-winx64\mysql-commercial-9.7.1-winx64\bin\mysql.exe" -u root -p1234 < database\script.sql
   ```

3. Para verificar se o banco foi criado:
   ```
   "C:\Users\lucas\Downloads\mysql-enterprise-9.7.1_winx64_bundle\mysql-commercial-9.7.1-winx64\mysql-commercial-9.7.1-winx64\bin\mysql.exe" -u root -p1234 -e "USE trabalho_sa; SHOW TABLES;"
   ```

### Opção 2: Pelo MySQL Workbench

1. Abra o MySQL Workbench
2. Conecte com usuário `root` e senha `1234`
3. Abra o arquivo `database/script.sql` (File > Open SQL Script)
4. Execute o script (botão ⚡ ou Ctrl+Shift+Enter)
5. Verifique se as tabelas aparecem no painel "Schemas"

### Opção 3: Deixe o Spring Boot criar (automático)

O `application.properties` já está configurado com:
```
spring.jpa.hibernate.ddl-auto=update
```

Isso faz o Hibernate criar/atualizar as tabelas automaticamente.

**Apenas garanta que o banco `trabalho_sa` exista:**

```
mysql.exe -u root -p1234 -e "CREATE DATABASE IF NOT EXISTS trabalho_sa;"
```

### Configuração do application.properties

O arquivo `src/main/resources/application.properties` já está configurado:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/trabalho_sa?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

**Altere a senha** se necessário (troque `1234` pela sua senha).

---

## 5. Abrir o Projeto no VS Code

### Passo a Passo

1. **Abra o Visual Studio Code**
2. **File > Open Folder...** (ou Ctrl+K Ctrl+O)
3. Navegue até: `C:\Users\lucas\Downloads\Henriquedelegrego\trabalho-sa`
4. Clique em **"Selecionar Pasta"**

### O que acontece depois de abrir

- O VS Code vai detectar que é um projeto Maven (pelo pom.xml)
- Uma notificação vai aparecer no canto inferior direito: "A Maven project has been detected. Do you want to open this project?"
- Clique em **"Yes"** ou **"Always"**
- O VS Code vai começar a baixar as dependências automaticamente
- Espere a barra de progresso no canto inferior direito terminar

### Se aparecerem erros nos arquivos (sublinados em vermelho)

**Não se preocupe!** Isso é normal enquanto o VS Code ainda está baixando as dependências. Aguarde alguns minutos.

Se os erros persistirem:
1. Pressione **Ctrl+Shift+P**
2. Digite: **"Java: Clean Java Language Server Workspace"**
3. Selecione **"Reload and Delete"**
4. Aguarde o VS Code recarregar

### Extensões Obrigatórias (instalar antes de começar)

Abra a aba de **Extensões** (Ctrl+Shift+X) e instale:

1. **Extension Pack for Java** (Microsoft) - ESSENCIAL
   - Pesquise: "Java Extension Pack"
   - Clique em Install
   
2. **Spring Boot Extension Pack** (VMware) - OPCIONAL mas recomendado
   - Pesquise: "Spring Boot Extension Pack"  
   - Clique em Install

Depois de instalar as extensões, feche e reabra o VS Code.

---

## 6. Executar o Projeto

### MÉTODO RECOMENDADO: Pelo Terminal do VS Code (mais seguro)

1. No VS Code, abra o **Terminal Integrado**: **Ctrl + ' (acento grave)**
2. Digite exatamente:
   ```
   .\mvnw.cmd spring-boot:run
   ```
3. Pressione **Enter**
4. A primeira execução baixa as dependências automaticamente (pode levar 1-2 minutos)
5. Quando aparecer **"Started EpiApplication"**, o servidor está rodando ✅

### Pelo Spring Boot Dashboard (alternativa)

1. Pressione **Ctrl+Shift+P**
2. Digite "Spring Boot Dashboard" e selecione
3. No painel que abrir, clique no **play ▶️** ao lado do projeto
4. Veja os logs no terminal

### Pelo PowerShell (fora do VS Code)

```powershell
cd C:\Users\lucas\Downloads\Henriquedelegrego\trabalho-sa
.\mvnw.cmd spring-boot:run
```

---

## 7. Acessar pelo Navegador

Depois que o projeto iniciar (veja a mensagem "Started EpiApplication" no terminal), abra:

**http://localhost:8080**

### Páginas disponíveis:

| Página | URL |
|--------|-----|
| Início | http://localhost:8080/ |
| Colaboradores | http://localhost:8080/pages/colaboradores.html |
| EPIs | http://localhost:8080/pages/epis.html |
| Empréstimos | http://localhost:8080/pages/emprestimos.html |

### Endpoints da API REST:

| Método | URL | Descrição |
|--------|-----|-----------|
| GET | /api/colaboradores | Listar colaboradores ativos |
| GET | /api/colaboradores/{id} | Buscar colaborador por ID |
| GET | /api/colaboradores/buscar?nome= | Buscar por nome |
| POST | /api/colaboradores | Cadastrar colaborador |
| PUT | /api/colaboradores/{id} | Atualizar colaborador |
| DELETE | /api/colaboradores/{id} | Desativar colaborador |
| GET | /api/epis | Listar EPIs ativos |
| GET | /api/epis/{id} | Buscar EPI por ID |
| GET | /api/epis/buscar?nome= | Buscar por nome |
| POST | /api/epis | Cadastrar EPI |
| PUT | /api/epis/{id} | Atualizar EPI |
| DELETE | /api/epis/{id} | Desativar EPI |
| GET | /api/emprestimos | Listar todos empréstimos |
| GET | /api/emprestimos/{id} | Buscar empréstimo por ID |
| POST | /api/emprestimos | Realizar novo empréstimo |
| PUT | /api/emprestimos/{id} | Atualizar empréstimo |
| PUT | /api/emprestimos/{id}/devolver | Registrar devolução |
| DELETE | /api/emprestimos/{id} | Excluir empréstimo |

---

## 8. Testar as Funcionalidades

### Fluxo completo de teste:

1. **Cadastrar Colaboradores**
   - Acesse "Colaboradores" no menu
   - Clique em "+ Novo Colaborador"
   - Preencha nome (ex: "João Silva"), CPF (ex: "12345678901"), cargo e setor
   - Clique em "Salvar"

2. **Cadastrar EPIs**
   - Acesse "EPIs" no menu
   - Clique em "+ Novo EPI"
   - Preencha nome (ex: "Capacete de Segurança"), quantidade em estoque (ex: 10)
   - Clique em "Salvar"

3. **Realizar um Empréstimo**
   - Acesse "Empréstimos" no menu
   - Clique em "+ Novo Empréstimo"
   - Selecione um colaborador e um EPI com estoque
   - Defina a data prevista de devolução
   - Clique em "Salvar"
   - Verifique que o estoque do EPI diminuiu

4. **Realizar uma Devolução**
   - Na lista de empréstimos, clique em "Devolver" ao lado de um empréstimo ativo
   - Confirme a devolução
   - Verifique que o status mudou para "Devolvido" e o estoque voltou

5. **Testar Validações**
   - Tente cadastrar colaborador sem nome
   - Tente emprestar EPI com estoque zerado
   - Verifique as mensagens de erro

---

## 9. Verificar os Dados dentro do MySQL

### Pelo terminal:

```powershell
"C:\Users\lucas\Downloads\mysql-enterprise-9.7.1_winx64_bundle\mysql-commercial-9.7.1-winx64\mysql-commercial-9.7.1-winx64\bin\mysql.exe" -u root -p1234
```

Dentro do MySQL execute:

```sql
USE trabalho_sa;
SELECT * FROM colaboradores;
SELECT * FROM epis;
SELECT * FROM emprestimos;
```

### Pelo MySQL Workbench:

1. Abra o MySQL Workbench
2. Conecte ao servidor
3. No painel "Navigator", clique em "trabalho_sa"
4. Clique com botão direito em uma tabela > "Select Rows - Limit 1000"

---

## 10. Solucionar Erros Comuns

### "JAVA_HOME não está definido"

**Solução:** Configure o JAVA_HOME:

```powershell
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-25.0.2", "User")
```
Depois feche e reabra o terminal.

### "Access denied for user 'root'@'localhost'"

**Solução:** A senha no `application.properties` está errada. Edite o arquivo e corrija:

```properties
spring.datasource.password=SUA_SENHA_AQUI
```

### "Unknown database 'trabalho_sa'"

**Solução:** Crie o banco manualmente:

```powershell
mysql.exe -u root -p1234 -e "CREATE DATABASE trabalho_sa;"
```

### "Port 8080 already in use"

**Solução:** Altere a porta no `application.properties`:

```properties
server.port=8081
```

### "Table 'colaboradores' already exists"

Não é um erro. Apenas ignore - o Hibernate gerencia isso automaticamente.

### O projeto não compila / erros de dependência

**Solução:** Limpe o cache e baixe as dependências novamente:

```powershell
.\mvnw.cmd clean
.\mvnw.cmd dependency:purge-local-repository
.\mvnw.cmd spring-boot:run
```

### "Failed to bind properties under 'spring.datasource.password'"

**Solução:** Verifique se não há caracteres especiais ou aspas extras no `application.properties`.

### VS Code mostra erros (sublinhado vermelho) nos arquivos Java

**Causa:** O Java Language Server ainda está carregando ou as dependências não foram baixadas.

**Solução 1 - Aguardar:** Espere o VS Code terminar de baixar as dependências (veja a barra de progresso no canto inferior direito).

**Solução 2 - Limpar cache do Java:**
1. Pressione **Ctrl+Shift+P**
2. Digite: **"Java: Clean Java Language Server Workspace"**
3. Clique em **"Reload and Delete"**
4. VS Code vai recarregar e reimportar o projeto

**Solução 3 - Verificar extensões:**
1. Abra Extensões (Ctrl+Shift+X)
2. Certifique-se de que **"Extension Pack for Java"** está instalado
3. Se não estiver, instale e reinicie o VS Code

### VS Code não reconhece o projeto como Maven

**Solução:**
1. Clique com botão direito no `pom.xml` no Explorer
2. Selecione **"Update Project Configuration"**
3. Ou reinicie o VS Code

### "O termo '.\mvnw.cmd' não é reconhecido"

**Solução:** Você precisa estar na pasta correta do projeto no terminal:
```
cd C:\Users\lucas\Downloads\Henriquedelegrego\trabalho-sa
```
Depois execute:
```
.\mvnw.cmd spring-boot:run
```

---

## Comandos Rápidos

```powershell
# Executar o projeto
.\mvnw.cmd spring-boot:run

# Compilar sem executar
.\mvnw.cmd clean compile

# Criar o banco de dados
"C:\Users\lucas\Downloads\mysql-enterprise-9.7.1_winx64_bundle\mysql-commercial-9.7.1-winx64\mysql-commercial-9.7.1-winx64\bin\mysql.exe" -u root -p1234 -e "CREATE DATABASE IF NOT EXISTS trabalho_sa;"

# Conectar ao MySQL
"C:\Users\lucas\Downloads\mysql-enterprise-9.7.1_winx64_bundle\mysql-commercial-9.7.1-winx64\mysql-commercial-9.7.1-winx64\bin\mysql.exe" -u root -p1234 trabalho_sa
```
