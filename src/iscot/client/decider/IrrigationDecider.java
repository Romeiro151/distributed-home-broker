package iscot.client.decider;

import java.io.IOException;

import iscot.client.Client;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.AreaIrrigationOrderEvent;
import iscot.event.GroundMoistureSensorEvent;

public class IrrigationDecider extends Client{

    public IrrigationDecider(String ip, int port, Areas area) throws ClassNotFoundException, IOException{
		super(ip, port, Type.DECIDER, area);
	}

    public static void main(String[] args) throws NumberFormatException, InterruptedException, ClassNotFoundException, IOException {
        if (args.length != 3){
            System.err.println("Invalid arguments!");
        }
        else {
            Areas area=Areas.valueOf(args[2]);
			
            // connect to server
			IrrigationDecider client = new IrrigationDecider(args[0], Integer.parseInt(args[1]), area);

            // register with the server
            client.registerWithServer();

            // Receção da leitura do sensor
            Thread.sleep(3000);
            GroundMoistureSensorEvent gmse = new GroundMoistureSensorEvent(area, 10.5);
            System.out.println("Recebi a leitura de " + gmse);

            // Envio da ordem para atuador
            boolean on = gmse.getValue() < 40;
            AreaIrrigationOrderEvent ordemParaAtuador = new AreaIrrigationOrderEvent(area, on);
            System.out.println("Imitiu-se a ordem para" + ordemParaAtuador );

        }
    }

}