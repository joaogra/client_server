# Aplicação Cliente-Servidor com Sockets (TCP/IP)

Este projeto é uma aplicação multithread em **Java** que implementa a arquitetura **Cliente-Servidor** utilizando **Sockets TCP/IP**. O servidor gerencia múltiplos clientes simultâneos através de threads dedicadas e implementa um protocolo com controle de máquina de estados.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: [Java](https://www.oracle.com/java/) (JDK 21 ou superior)
- **Gerenciador de Dependências e Build**: [Apache Maven](https://maven.apache.org/)
- **Comunicação em Rede**: API padrão de Sockets do Java (`java.net.ServerSocket` e `java.net.Socket`)
- **Concorrência / Multithreading**: `java.lang.Thread` e `java.lang.Runnable`
- **I/O**: `java.io.BufferedReader`, `java.io.InputStreamReader` e `java.io.PrintWriter`

---

## 🏛️ Arquitetura e Funcionamento

### Estrutura dos Componentes

- **`Server.java`**: Ponto de entrada do servidor. Fica em loop contínuo escutando conexões na porta `8080`. Para cada cliente conectado, cria e dispara uma nova thread com uma instância de `ClientHandler`.
- **`ClientHandler.java`**: Gerencia a conexão de um cliente específico de forma assíncrona, controlando o fluxo através de uma máquina de estados (`ClientState`).
- **`ClientState.java`**: Enum que define as fases da sessão do cliente:
  - `WAIT_HELLO`: Aguardando saudação inicial (`HELLO`).
  - `WAIT_AUTH`: Aguardando comando de autenticação (`AUTH <nome>`).
  - `READY`: Autenticado e pronto para comandos gerais (`ECHO`, `QUIT`).
  - `CLOSED`: Sessão encerrada.
- **`Client.java`**: Cliente de console simples para teste e interação com o servidor via terminal.

### Protocolo de Comunicação

```text
[Cliente]                                     [Servidor]
    |                                              |
    | ------------ Conecta na porta 8080 --------> |
    | <-------------- "WELCOME" ------------------ | (Estado: WAIT_HELLO)
    |                                              |
    | ---------------- "HELLO" ------------------> |
    | <------------- "HELLO OK" ------------------ | (Estado: WAIT_AUTH)
    |                                              |
    | ------------ "AUTH <seu_nome>" ------------> |
    | <-------------- "AUTH OK" ------------------ | (Estado: READY)
    |                                              |
    | ------------ "ECHO <mensagem>" ------------> |
    | <--------- "Echo: <mensagem>" -------------- |
    |                                              |
    | ----------------- "QUIT" ------------------> |
    | <--------------- "BYE" --------------------- | (Estado: CLOSED)
    |                                              |
    x (Conexão encerrada)                          x
```

#### Comandos Reconhecidos:

| Estado | Comando Esperado | Descrição / Resposta |
| :--- | :--- | :--- |
| **Inicial** | *(Automático)* | O servidor envia `WELCOME` logo após a conexão ser estabelecida. |
| **WAIT_HELLO** | `HELLO` | Responde com `HELLO OK` e avança para `WAIT_AUTH`. |
| **WAIT_AUTH** | `AUTH <nome>` | Autentica o usuário com o nome informado, responde `AUTH OK` e avança para `READY`. Se não informar nome, retorna `NAME REQUIRED`. |
| **READY** | `ECHO <texto>` | Retorna `Echo: <texto>`. |
| **READY** | `QUIT` | Retorna `BYE`, encerra a conexão e fecha o socket. |

---

## 📋 Pré-requisitos

Antes de iniciar, certifique-se de possuir instalado em sua máquina:

- **JDK 21** ou superior: [Download Java](https://www.oracle.com/java/technologies/downloads/)
- **Apache Maven 3.8+**: [Download Maven](https://maven.apache.org/download.cgi)

Para verificar as versões instaladas:
```bash
java -version
mvn -version
```

---

## 🚀 Como Executar

### 1. Compilar o Projeto

Na raiz do projeto (`client_server`), compile o código-fonte através do Maven:

```bash
mvn compile
```

Isso criará os arquivos compilados (`.class`) na pasta `target/classes`.

---

### 2. Iniciar o Servidor

Abra um terminal na raiz do projeto e execute:

```bash
java -cp target/classes org.example.Server
```

Você verá a seguinte mensagem indicando que o servidor está ativo:
```text
Waiting for connection...
```

---

### 3. Conectar um ou mais Clientes

Com o servidor rodando, abra **outro terminal** para conectar um cliente:

```bash
java -cp target/classes org.example.Client
```

> [!TIP]
> Como o servidor é **multithread**, você pode abrir quantos terminais quiser e executar o comando acima em cada um. Cada terminal será um cliente conectado simultaneamente!

---

### 4. Alternativa: Execução através de IDE (IntelliJ IDEA / VS Code / Eclipse)

1. Abra o projeto na sua IDE de preferência.
2. Execute primeiro o método `main` da classe [Server.java](src/main/java/org/example/Server.java).
3. Em seguida, execute o método `main` da classe [Client.java](src/main/java/org/example/Client.java). Se desejar testar mais clientes, basta iniciar novas execuções de `Client.java`.

---

## 💻 Exemplo Prático de Sessão

Ao executar o cliente, você pode interagir com o servidor seguindo o fluxo de comandos:

```text
SERVER: WELCOME
> HELLO
SERVER: HELLO OK
> AUTH Joao
SERVER: AUTH OK
> ECHO Teste de comunicacao via Socket
SERVER: Echo: Teste de comunicacao via Socket
> QUIT
SERVER: BYE
```

No terminal do servidor, os logs de acompanhamento das threads e estados serão exibidos:
```text
Waiting for connection...
Client connected: 127.0.0.1
Thread 22 | State: WAIT_HELLO | Command: HELLO
User authenticated: Joao
Thread 22 | State: WAIT_AUTH | Command: AUTH
Thread 22 | State: READY | Command: ECHO
Thread 22 | State: READY | Command: QUIT
Client disconnected
```

---

## 📂 Estrutura de Arquivos

```text
client_server/
├── pom.xml                                   # Configuração do Maven
├── README.md                                 # Documentação do projeto
└── src/
    └── main/
        └── java/
            └── org/
                └── example/
                    ├── Server.java           # Servidor TCP e dispatcher de threads
                    ├── ClientHandler.java    # Lógica de atendimento ao cliente (Runnable)
                    ├── ClientState.java      # Enum com estados da sessão
                    └── Client.java           # Cliente de console interativo
```
