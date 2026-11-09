package iscot.client.decider;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.AreaIrrigationOrderEvent;
import iscot.event.GroundMoistureSensorEvent;
import iscot.event.RegistrationEvent;

public class IrrigationDecider {

    public static void main(String[] args) throws NumberFormatException, InterruptedException {
        if (args.length != 3){
            System.err.println("Invalid arguments!");

        }
        else {
            Areas area = Areas.valueOf(args[2]);

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