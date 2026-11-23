package iscot.server;

import iscot.enums.Areas;
import iscot.event.Event;
import iscot.event.RegistrationEvent;
import iscot.enums.Type;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final IscoTServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private String deviceId;
    private Type clientType;
    private Areas area;


    // Inicializador precisa de uma socket e um server
    public ClientHandler(Socket socket, IscoTServer server) {
        this.socket = socket;
        this.server = server;
    }

    // Inicialmente conecta-se à socket e comuninca. Verifica-se se deu o cliente fechou a ligação normalmente ou se deu erro, removendo-o
    // caso necessário.
    @Override
    public void run() {
        try{
            doConnections(socket);
            serve();
        } catch (EOFException e){

        } catch (IOException | ClassNotFoundException e){
            System.err.println("[Cliente Handler" + (deviceId != null ? " - " + deviceId : "") + "] Erro na ligação: " + e.getMessage());
        } finally {
            if(deviceId != null){
                server.removeClient(this);
            }
            closeResources();
        }
    }

    // Cria-se primeiro o ObjectOutputStream para a ordem ser inversa à do cliente, de modo a evitar bloqueio na inicialização das sockets
    private void doConnections(Socket socket) throws IOException{
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    // Função que trata da comunicação. Começa-se por tratar de receber o primeiro evento cliente. De seguid valida-se se o cliente é válido.
    // Cria-se um ID único para esse cliente e regista-se. Devolve-se o RegistrationEvente registado. Por fim, fica com a escuta ativa.
    private void serve() throws IOException, ClassNotFoundException{
        Object primeiraMsg = in.readObject();
        if(!(primeiraMsg instanceof RegistrationEvent)){
            System.err.println("[Client Handler] - Ligação recusada: O objeto não é um RegistrationEvent.");
        }

        RegistrationEvent eventoCliente = (RegistrationEvent) primeiraMsg;

        if (eventoCliente.getType() == null || eventoCliente.getArea() == null){
            System.err.println("[Client Handler] - Ligação recusada: O cliente não tem Type ou Area.");
            out.writeObject(null);
            out.flush();
        }

        this.clientType = eventoCliente.getType();
        this.area = eventoCliente.getArea();
        this.deviceId = server.generateDeviceID(clientType.name());

        eventoCliente.setAssignedDeviceID(this.deviceId);
        server.registerClient(this);

        out.writeObject(eventoCliente);
        out.flush();

        // Fase 6
        while (true) {
            Object objeto = in.readObject();
            System.out.println("[" + deviceId + "] Evento Recebido: " + objeto);
            ArrayList<ClientHandler> dests = new ArrayList<>();
            if(objeto instanceof Event){
                dests = (ArrayList<ClientHandler>) server.eventHandler((Event) objeto, clientType);
            }
            if(dests != null){
                for(ClientHandler c : dests){
                    c.out.reset();
                    c.out.writeObject(objeto);
                }
            }
        }
    }

    // Método resposável por fechar os recursos
    private void closeResources() {
        try{
            if(out != null) out.close();
            if(in != null) in.close();
            if(socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("[Client Handler] - Erro ao fechar os Recursos: " + e.getMessage());
        }
    }

    public String getDeviceId() { return deviceId; }
    public Type getClientType() { return clientType; }
    public Areas getArea() { return area; }
}
