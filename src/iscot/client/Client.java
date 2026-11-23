package iscot.client;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.RegistrationEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String deviceID;
    private Type type;
    private Areas area;

    private final LocalDateTime timestamp;
    private final List<String> historicoAcoes = new ArrayList<>();

    public Client(String ip, int port, Type type, Areas area) throws IOException, ClassNotFoundException {
        this.type = type;
        this.area = area;
        this.timestamp = LocalDateTime.now(ZoneId.systemDefault());
        InetAddress ipCliente = InetAddress.getByName(ip);
        socket = new Socket(ipCliente, port);

        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
    }

    public Type getType(){
        return type;
    }

    public Areas getArea(){
        return area;
    }

    public synchronized void registerWithServer() throws IOException, ClassNotFoundException {
        sendEvent(new RegistrationEvent(this.area, this.type));
        RegistrationEvent resposta = (RegistrationEvent) receiveEvent();
        if(resposta != null){
            this.deviceID = resposta.getAssignedDeviceID();
        }else{
            throw new IOException("Registo recusado pelo servidor.");
        }
    }

    public synchronized void sendEvent(Event event) throws IOException{
        if(deviceID != null){
            event.setDeviceID(deviceID);
        }
        out.reset();
        out.writeObject(event);
        out.flush();
    }

    public Event receiveEvent() throws IOException, ClassNotFoundException{
        Object objeto = in.readObject();
        return (objeto instanceof Event) ? (Event) objeto : null;
    }

    public void closeResources(){
        try{
            if(out != null) out.close();
            if(in != null) in.close();
            if(socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar a ligação: " + e.getMessage());
        }
    }

    private String getDeviceID(){
        return deviceID;
    }
    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    public synchronized void registarAcao(String acao){
        String hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String registo = acao + " em " + hora;

        historicoAcoes.add(registo);
        System.out.println("[" +(deviceID != null ? deviceID : type) + "] " + registo);
    }

    public synchronized List<String> getHistoricoAcoes(){
        return historicoAcoes;
    }

    @Override
    public String toString(){
        return "Registo - " + timestamp.toLocalTime();
    }
}
