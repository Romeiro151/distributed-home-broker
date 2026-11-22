package iscot.server;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import iscot.enums.Type;
import iscot.event.Event;

public class IscoTServer {
    public static final int PORTO = 8080;
    private final int porto;
    private int numeroProxID = 1;
    private List<ClientHandler> clientesConectados = new ArrayList<>();
    private List<Event> eventos = new ArrayList<>();

    public IscoTServer(int porto){
        this.porto = porto;
    }

    // Inicialização do server
    public void startServing() throws IOException {
        ServerSocket socket = new ServerSocket(porto);
        System.out.println("[IscoT Server] - A iniciar no porto " + porto);
        try{
            while(true){
                Socket socketParaCliente = socket.accept();
                System.out.println("[IscoT Server] - Nova ligação recebida e aciete de " + socketParaCliente.getInetAddress());
                
                //Start do servidor dedicado para o cliente
                ClientHandler client = new ClientHandler(socketParaCliente, this);
                client.start();
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

    // Fase 6
    public List<ClientHandler> eventHandler(Event event, Type clientType){
        //Estou a adicionar o evento a lista de eventos diretamente mas posteriormente a inserçao e tratamento de eventos vai ser gerida segundo os principios da concorrencia. Apenas para a Fase 6.
        eventos.add(event);
        List<ClientHandler> clients = new ArrayList<>();
        if(clientType == Type.SENSOR){
            for(ClientHandler c : clientesConectados){
                if(c.getClientType() == Type.DECIDER){
                    clients.add(c);
                }
            }
        }
        else if(clientType == Type.DECIDER){
            for(ClientHandler c : clientesConectados){
                if(c.getClientType() == Type.ACTUATOR){
                    clients.add(c);
                }
            }
        }

        return clients;
        
    }

    public static void main(String[] args){
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : PORTO;
        try{
            new IscoTServer(port).startServing();
        } catch (IOException e) {
            System.err.println("[IscoT Server] - Erro no servidor: " + e.getMessage());
        }
    }
}
