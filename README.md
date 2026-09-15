# Cliente-Servidor com Sockets (TCP/IP)

Aplicação multithread em Java que implementa a arquitetura cliente-servidor via Sockets TCP/IP, utilizando Virtual Threads e uma máquina de estados para controlar a sessão de cada cliente.

## Requisitos

- Java 21 ou superior
- Maven 3.8 ou superior

## Como Executar

### 1. Compilar o projeto
```bash
mvn compile
```

### 2. Iniciar o servidor
```bash
java -cp target/classes org.example.Server
```

### 3. Iniciar o cliente
Em outro terminal (pode abrir vários para múltiplos clientes):
```bash
java -cp target/classes org.example.Client
```

> Nota: Você também pode rodar executando a classe `Server` e depois a classe `Client` diretamente pela sua IDE.

## Protocolo de Comandos

A conexão segue a seguinte transição de estados e troca de mensagens:

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

1. **HELLO**: Saudação inicial (após receber a mensagem de boas-vindas do servidor).
2. **AUTH <nome>**: Autentica o cliente com o nome especificado.
3. **ECHO <mensagem>**: Retorna a mensagem enviada.
4. **QUIT**: Encerra a conexão com o servidor.
