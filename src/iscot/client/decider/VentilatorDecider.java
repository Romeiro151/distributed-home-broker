package iscot.client.decider;

import java.io.IOException;

import iscot.client.Client;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.HumiditySensorEvent;
import iscot.event.AreaVentilationOrderEvent;

public class VentilatorDecider extends Client{

    public VentilatorDecider(String ip, int port, Areas area) throws ClassNotFoundException, IOException{
		super(ip, port, Type.DECIDER, area);
	}

    public static void main(String[] args) throws NumberFormatException, InterruptedException, ClassNotFoundException, IOException {
        if (args.length != 3){
            System.err.println("Invalid arguments!");
        }
        else {
            Areas area=Areas.valueOf(args[2]);
			
            // connect to server
			VentilatorDecider client = new VentilatorDecider(args[0], Integer.parseInt(args[1]), area);

            // register with the server
            client.registerWithServer();

            // Receção da leitura do  sensor
            Thread.sleep(3000);
            HumiditySensorEvent hse = new HumiditySensorEvent(area, 80.3);
            System.out.println("Recebi a leitura de " + hse);

            boolean on = hse.getValue() > 75;
            AreaVentilationOrderEvent ordemParaAtuador = new AreaVentilationOrderEvent(area, on);
            System.out.println("Emitiu-se a ordem para  " + ordemParaAtuador);

        }
    }
}