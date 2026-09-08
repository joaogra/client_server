package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket client;
    private ClientState state;
    private String username;

    public ClientHandler(Socket client) {
        this.client = client;
        this.state = ClientState.WAIT_HELLO;
    }

    @Override
    public void run() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

            writer.println("WELCOME");

            String msg;

            while ((msg = reader.readLine()) != null) {

                String[] split = msg.split(" ", 2);

                String command = split[0].toUpperCase();
                String body = split.length > 1 ? split[1] : "";

                System.out.println(
                        "Thread " +
                                Thread.currentThread().threadId() +
                                " | State: " +
                                state +
                                " | Command: " +
                                command
                );

                switch (state) {

                    case WAIT_HELLO:

                        handleWaitHello(command, writer);

                        break;

                    case WAIT_AUTH:

                        handleWaitAuth(command, body, writer);

                        break;

                    case READY:

                        handleReady(command, body, writer);

                        break;

                    case CLOSED:

                        return;
                }

                if (state == ClientState.CLOSED) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Connection issue with client" + (username != null ? " (" + username + ")" : "") + ": " + e.getMessage());

        } finally {

            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Client disconnected" + (username != null ? " (" + username + ")" : ""));
        }
    }

    private void handleWaitHello(
            String command,
            PrintWriter writer
    ) {

        if (command.equals("HELLO")) {
            writer.println("HELLO OK");
            state = ClientState.WAIT_AUTH;

        } else {
            writer.println("EXPECTED HELLO");
        }
    }

    private void handleWaitAuth(
            String command,
            String body,
            PrintWriter writer
    ) {
        if (command.equals("AUTH")) {
            if (body.isEmpty()) {
                writer.println("NAME REQUIRED");

            } else {
                this.username = body;
                System.out.println("User authenticated: " + body);

                writer.println("AUTH OK");
                state = ClientState.READY;
            }

        } else {
            writer.println("AUTH REQUIRED");

        }
    }

    private void handleReady(
            String command,
            String body,
            PrintWriter writer
    ) {

        if (command.equals("ECHO")) {

            writer.println(
                    "Echo: " + body
            );

        } else if (command.equals("QUIT")) {

            writer.println("BYE");
            state = ClientState.CLOSED;

        } else {
            writer.println("UNKNOWN COMMAND");
        }
    }
}