package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner sc = new Scanner(System.in)) {

            String response = reader.readLine();
            if (response == null) {
                System.out.println("Não foi possível conectar ou o servidor fechou a conexão.");
                return;
            }

            System.out.println("SERVER: " + response);

            while (true) {

                System.out.print("> ");

                String input = sc.nextLine();

                if (input.trim().isEmpty()) {
                    continue;
                }

                writer.println(input);

                response = reader.readLine();

                if (response == null) {
                    System.out.println("Conexão encerrada pelo servidor.");
                    break;
                }

                System.out.println("SERVER: " + response);

                if (response.equalsIgnoreCase("BYE")) {
                    break;
                }
            }

        } catch (ConnectException e) {
            System.out.println("Não foi possível conectar ao servidor em " + HOST + ":" + PORT + ". Verifique se o servidor está ativo.");
        } catch (IOException e) {
            System.out.println("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }
}
