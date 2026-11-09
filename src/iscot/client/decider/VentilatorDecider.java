package iscot.client.decider;

import iscot.client.sensor.HumiditySensor;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.AreaLightingOrderEvent;
import iscot.event.HumiditySensorEvent;
import iscot.event.LightSensorEvent;
import iscot.event.RegistrationEvent;

public class VentilatorDecider {
    public static void main(String[] args) throws NumberFormatException, InterruptedException {
        if (args.length != 3){
            System.err.println("Invalid arguments!");
        }
        else {
            Areas area = Areas.valueOf(args[2]);

            // Receção da leitura do  sensor
            Thread.sleep(3000);
            HumiditySensorEvent hse = new HumiditySensorEvent(area, 80.3);
            System.out.println("Recebi a leitura de " + hse);

            boolean on = hse.getValue() > 75;
            AreaLightingOrderEvent ordemParaAtuador = new AreaLightingOrderEvent(area, on);
            System.out.println("Emitiu-se a ordem para  " + ordemParaAtuador);

        }
    }
}