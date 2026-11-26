package iscot.server;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import iscot.ModifiedCountDownLatch;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.SensorEvent;

public class IscoTServer {
    public static final int PORTO = 8080;
    public static final int CAPACITY_EVENTOS = 30;
    private final int porto;
    private int numeroProxID = 1;
    private List<ClientHandler> clientesConectados = new ArrayList<>();
    private List<ClientHandler> clientesEventoAtual = new ArrayList<>();
    private List<Event> eventos = new ArrayList<>(20);
    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
    private Condition eventoDisponivel = lock.newCondition(); 
    private ModifiedCountDownLatch mcdl = new ModifiedCountDownLatch(10);

    public IscoTServer(int porto){
        this.porto = porto;
    }

    // Inicialização do server
    public void startServing() throws IOException {
        
        // Fase 7 do:
        Thread eventManager = new Thread(() -> {
            try {
                processEvents();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        eventManager.start();
        // done;
        
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

    // Fase 7
    public void processEvents() throws InterruptedException{
            
        while(!Thread.currentThread().isInterrupted()){
            Event evento;

            lock.lock();
            try{
                while(eventos.isEmpty()){
                    notEmpty.await();
                }
                evento = eventos.get(0);
                clientesEventoAtual.clear();

                //Se o evento for <algo>Event, criar lista clientes interessados e criar CDL
                if(evento instanceof SensorEvent){
                    for(ClientHandler c : clientesConectados){
                        if(c.getClientType() == Type.DECIDER){
                            clientesEventoAtual.add(c);
                        }
                    }
                } else {
                    for(ClientHandler c : clientesConectados){
                        if(c.getClientType() == Type.ACTUATOR){
                            clientesEventoAtual.add(c);
                        }
                    }
                }


                mcdl = new ModifiedCountDownLatch(clientesEventoAtual.size());

                //Se não existir clientes destinatarios, descarta logo o evento e parte para o proximo
                if (clientesEventoAtual.isEmpty()) {
                    if(!eventos.isEmpty()){
                        eventos.remove(0);
                    }
                    notFull.signalAll();
                    continue;
                }

                eventoDisponivel.signalAll();
                
            } finally {
                lock.unlock();
            }

            mcdl.await(100000);

            lock.lock();
            try {
                if(!eventos.isEmpty()){    
                    eventos.remove(0);

                }
                notFull.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    // Gera o ID para o cliente conectado e incrementa
    public synchronized String generateDeviceID(String prefix){
        return prefix + "-" + (numeroProxID++);
    }

    // Adiciona evento no servidor a ser tratado (Fase 7)
    public void addEvent(Event event) throws InterruptedException {
        lock.lock();
        try {
            ClientHandler possibleUser = null;
            for (ClientHandler c : clientesConectados) {
                if (Objects.equals(c.getDeviceId(), event.getDeviceID())) {
                    possibleUser = c;
                    break;
                }
            }

            if(possibleUser != null){
                
                while(eventos.size() >= CAPACITY_EVENTOS){
                    notFull.await();
                }
                eventos.add(event);
                notEmpty.signalAll();  
            
            }
        } finally {
            lock.unlock();
        }
        
    }

    // Consome evento do servidor (Fase 7)
    public Event consume(ClientHandler client) throws InterruptedException {
        
        lock.lock();
        try{
            while(eventos.isEmpty() || !clientesEventoAtual.contains(client)){
                eventoDisponivel.await();
            }
            clientesEventoAtual.remove(client);
            mcdl.countdown();
            return eventos.get(0);

        } finally {
            lock.unlock();
        }
    }

    // Registra cliente
    public void registerClient(ClientHandler client){
        lock.lock();
        try {
            clientesConectados.add(client);
            System.out.println("[IscoT Server] Cliente registrado com o ID: " + client.getDeviceId()
                + " do tipo: " + client.getClientType() + " em: " + client.getArea() + ". Estão, neste momento, " + clientesConectados.size() + " cliente conectados.");
        } finally {
            lock.unlock();
        }
        
    }

    // Remove cliente
    public void removeClient(ClientHandler client){
        lock.lock();
        try {
            clientesConectados.remove(client);
            System.out.println("[IscoT Server] Cliente desconectado: " + client.getDeviceId());
        } finally {
            lock.unlock();
        }
        
    }

    // Fase 6
    /*
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
