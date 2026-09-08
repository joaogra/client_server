package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

        ServerSocket server =  new ServerSocket(PORT);

        System.out.println("Waiting for connection...");

        while(true){

            Socket client = server.accept();

            System.out.println("Client connected: " +  client.getInetAddress().getHostAddress());

            ClientHandler handler = new ClientHandler(client);

            Thread thread = new Thread(handler);

            thread.start();

        }


    }
}