package iscot.client.decider;

import java.io.IOException;

import iscot.client.Client;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.AreaLightingOrderEvent;
import iscot.event.LightSensorEvent;

public class LightDecider extends Client{

    public LightDecider(String ip, int port, Areas area) throws ClassNotFoundException, IOException{
		super(ip, port, Type.DECIDER, area);
	}

    public static void main(String[] args) throws NumberFormatException, InterruptedException, ClassNotFoundException, IOException {
        if (args.length != 3){
            System.err.println("Invalid arguments!");
        }
        else {
            Areas area=Areas.valueOf(args[2]);
			
            // connect to server
			LightDecider client = new LightDecider(args[0], Integer.parseInt(args[1]), area);

            // register with the server
            client.registerWithServer();

            // Receção da leitura do  sensor
            Thread.sleep(3000);
            LightSensorEvent lse = new LightSensorEvent(area, 10.5);
            System.out.println("Recebi a leitura de " + lse);

            boolean on = lse.getValue() < 30;
            AreaLightingOrderEvent ordemParaAtuador = new AreaLightingOrderEvent(area, on);
            System.out.println("Emitiu-se a ordem para  " + ordemParaAtuador);

        }
    }
}
