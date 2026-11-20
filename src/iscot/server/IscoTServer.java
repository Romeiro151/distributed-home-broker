package iscot.server;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class IscoTServer {
    public static final int PORTO = 8080;
    private final int porto;
    private int numeroProxID = 1;
    private final List<ClientHandler> clientesConectados = new ArrayList<>();

    public IscoTServer(int porto){
        this.porto = porto;
    }

    // Inicialização do server
    public void startServing() throws IOException {
        ServerSocket socket = new ServerSocket(PORTO);
        System.out.println("[IscoT Server] - A iniciar no porto " + porto);
        try{
            while(true){
                Socket socketParaCliente = socket.accept();
                System.out.println("[IscoT Server] - Nova ligação recebida e aciete de " + socketParaCliente.getInetAddress());
                new ClientHandler(socketParaCliente, this);
            }
        } finally {
            socket.close();
        }
    }

    // Gera o ID para o cliente conectado e incrementa
    public synchronized String generateDeviceID(String prefix){
        return prefix + "-" + (numeroProxID++);
    }

    // Registra cliente
    public synchronized void registerClient(ClientHandler client){
        clientesConectados.add(client);
        System.out.println("[IscoT Server] Cliente registrado com o ID: " + client.getDeviceId()
                + " do tipo: " + client.getClientType() + " em: " + client.getArea() + ". Estão, neste momento, " + clientesConectados.size() + " cliente conectados.");
    }

    // Remove cliente
    public synchronized void removeClient(ClientHandler client){
        clientesConectados.remove(client);
        System.out.println("[IscoT Server] Cliente desconectado: " + client.getDeviceId());
    }

    public static void main(String[] args){
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : 8080;
        try{
            new IscoTServer(port).startServing();
        } catch (IOException e) {
            System.err.println("[IscoT Server] - Erro no servidor: " + e.getMessage());
        }
    }
}
