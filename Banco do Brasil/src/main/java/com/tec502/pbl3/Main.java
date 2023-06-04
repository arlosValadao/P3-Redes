package com.tec502.pbl3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final Integer port = 8017;
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(port);
        System.out.println("Porta " + port + " aberta!");

        while (true) {
            Socket cliente = servidor.accept();
            //Cria uma nova thread para cada requisição que chega no servidor
            new AtendimentoThread(cliente).start();

        }
    }
}