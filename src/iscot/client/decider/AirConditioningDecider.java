package iscot.client.decider;

import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.event.AreaLightingOrderEvent;
import iscot.event.AreaTemperatureOrderEvent;
import iscot.event.LightSensorEvent;
import iscot.event.TemperatureSensorEvent;

public class AirConditioningDecider {
    public static void main(String[] args) throws NumberFormatException, InterruptedException {
        if (args.length != 3){
            System.err.println("Invalid arguments!");
        }
        else {
            Areas area = Areas.valueOf(args[2]);

            // Receção da leitura do  sensor
            Thread.sleep(3000);
            TemperatureSensorEvent tse = new TemperatureSensorEvent(area, 30.0);
            System.out.println("Recebi a leitura de " + tse);

            boolean frio = tse.getValue() > 25;
            if(frio){
                AreaTemperatureOrderEvent ordemParaAtuador = new AreaTemperatureOrderEvent(area, AirConditioningMode.Frio);
                System.out.println("Emitiu-se a ordem de refrescar para  " + ordemParaAtuador);
            }
        }
    }
}
