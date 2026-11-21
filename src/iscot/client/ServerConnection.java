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

public class ServerConnection {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private String deviceID;

        public void connectToServer(String ip, int port, Type type, Areas area) throws IOException, ClassNotFoundException {
            InetAddress ipCliente = InetAddress.getByName(ip);
            socket = new Socket(ipCliente, port);

            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            sendEvent(new RegistrationEvent(area, type));
            Object resposta = in.readObject();
            if(resposta instanceof RegistrationEvent){
                this.deviceID = ((RegistrationEvent) resposta).getAssignedDeviceID();
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
}
