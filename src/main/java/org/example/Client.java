package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 8080);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        Scanner sc = new Scanner(System.in);

        String response = reader.readLine();
        if (response == null) {
            System.out.println("Não foi possível conectar ou o servidor fechou a conexão.");
            socket.close();
            sc.close();
            return;
        }

        System.out.println("SERVER: " + response);

        boolean flag = true;

        while (flag) {

            System.out.print("> ");

            String input = sc.nextLine();

            writer.println(input);

            response = reader.readLine();

            if (response == null) {
                System.out.println("Conexão encerrada pelo servidor.");
                break;
            }

            System.out.println("SERVER: " + response);

            if (response.equalsIgnoreCase("BYE")) {
                flag = false;
            }
        }

        socket.close();
        sc.close();
    }
}
