package iscot.client.decider;

import iscot.enums.Areas;
import iscot.event.AreaLightingOrderEvent;
import iscot.event.LightSensorEvent;

public class LightDecider {
    public static void main(String[] args) throws NumberFormatException, InterruptedException {
        if (args.length != 3){
            System.err.println("Invalid arguments!");
        }
        else {
            Areas area = Areas.valueOf(args[2]);

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
