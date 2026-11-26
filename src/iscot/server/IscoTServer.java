package iscot.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import iscot.ModifiedCountDownLatch;
import iscot.client.Client;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.SensorEvent;
import iscot.event.HighOrderLevelEvent;

public class IscoTServer {
    public static final int PORTO = 8080;
    public static final int TEMPO_ESPERA_COUNTDOWN = 1000;

    private final int porto;
    private int numeroProxID = 1;
    private List<ClientHandler> clientesConectados = new ArrayList<>();
    private List<ClientHandler> clientesEventoAtual = new ArrayList<>();
    private List<Event> eventos = new ArrayList<>();
    private ModifiedCountDownLatch mcdl;

    public IscoTServer(int porto){
        this.porto = porto;
    }

    public void startServing() throws IOException {

        // Cria-se uma Thread dedicada a tratamento do evento recebido.
        Thread trataEvento = new Thread(() -> {
            try{
                processEvents();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        });
        trataEvento.start();

        // Inicialização do Server.
        ServerSocket socket = new ServerSocket(porto);
        System.out.println("[IscoT Server] - A iniciar no porto " + porto);
        try{
            while(true){
                Socket socketParaCliente = socket.accept();
                System.out.println("[IscoT Server] - Nova ligação recebida e aceite em " + socketParaCliente.getInetAddress());
                
                //Start do servidor dedicado para o cliente
                ClientHandler client = new ClientHandler(socketParaCliente, this);
                client.start();
            }
        } finally {
            socket.close();
        }
    }

    // Função usada pela Thread.
    public void processEvents() throws InterruptedException{

        while(!Thread.currentThread().isInterrupted()) {
            Event evento;
            ModifiedCountDownLatch mLatch;

            synchronized (this){
                while(eventos.isEmpty()){
                    wait();
                }

                evento = eventos.get(0);
                clientesEventoAtual.clear();

                if(evento instanceof SensorEvent){
                    for(ClientHandler c : clientesConectados) {
                        if(c.getClientType() == Type.DECIDER && Objects.equals(c.getArea(), evento.getArea())){
                            clientesEventoAtual.add(c);
                        }
                    }
                }else if(evento instanceof HighOrderLevelEvent){
                    for (ClientHandler c : clientesConectados){
                        if(c.getClientType() == Type.ACTUATOR && Objects.equals(c.getArea(),evento.getArea())){
                            clientesEventoAtual.add(c);
                        }
                    }
                }

                if(clientesEventoAtual.isEmpty()){
                    eventos.remove(0);
                    continue;
                }

                mLatch = new ModifiedCountDownLatch(clientesEventoAtual.size());
                this.mcdl = mLatch;
                notifyAll();
            }

            mLatch.await(TEMPO_ESPERA_COUNTDOWN);

            synchronized (this){
                clientesEventoAtual.clear();
                this.mcdl = null;
                if(!eventos.isEmpty()){
                    eventos.remove(0);
                }
            }
        }
    }

    // Gera o ID para o cliente conectado e incrementa
    public synchronized String generateDeviceID(String prefix){
        return prefix + "-" + (numeroProxID++);
    }

    // Adiciona evento no servidor a ser tratado (Fase 7)
    public synchronized void addEvent(Event event) {
        ClientHandler cliente = null;
        for(ClientHandler c : clientesConectados){
            if(Objects.equals(c.getDeviceId(), event.getDeviceID())){
                cliente = c;
                break;
            }
        }

        if((cliente.getClientType() == Type.SENSOR && event instanceof SensorEvent) || (cliente.getClientType() == Type.DECIDER && event instanceof HighOrderLevelEvent)){
            eventos.add(event);
            notifyAll();
        }else{
            System.err.println("[IscoT Server] - Evento rejeitado, pois o cliente: " + cliente.getDeviceId() + " (" + cliente.getClientType() + ") não pode lançar um evento " + event.getClass().getSimpleName());
        }
    }

    // Consome evento do servidor (Fase 7)
    public synchronized Event consume(ClientHandler client) throws InterruptedException {
        while (eventos.isEmpty() || !clientesEventoAtual.contains(client)){
            wait();
        }
        clientesEventoAtual.remove(client);
        if(mcdl != null){
            mcdl.countdown();
        }
        return eventos.get(0);
    }

    // Registra cliente
    public synchronized void registerClient(ClientHandler client){
        clientesConectados.add(client);
        System.out.println("[IscoT Server] - Cliente registrado com ID: " + client.getDeviceId() + " do tipo " + client.getClientType() + " em " + client.getArea()
        + ". Estão " + clientesConectados.size() + " clientes conectados agora.");
    }

    // Remove cliente
    public synchronized void removeClient(ClientHandler client){
        clientesConectados.remove(client);
        if(clientesEventoAtual.remove(client) && mcdl != null){
            mcdl.countdown();
        }
    }

    // Fase 6
    /*
    public List<ClientHandler> eventHandler(Event event, Type clientType){
        //Estou a adicionar o evento a lista de eventos diretamente, mas posteriormente a inserçao e tratamento de eventos vai ser gerida segundo os principios da concorrencia. Apenas para a Fase 6.
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
    */

    public static void main(String[] args){
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : PORTO;
        try{
            new IscoTServer(port).startServing();
        } catch (IOException e) {
            System.err.println("[IscoT Server] - Erro no servidor: " + e.getMessage());
        }
    }
}
