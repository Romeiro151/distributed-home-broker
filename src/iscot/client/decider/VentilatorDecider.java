package iscot.client.decider;

import iscot.enums.Areas;
import iscot.event.HumiditySensorEvent;
import iscot.event.AreaVentilationOrderEvent;

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
            AreaVentilationOrderEvent ordemParaAtuador = new AreaVentilationOrderEvent(area, on);
            System.out.println("Emitiu-se a ordem para  " + ordemParaAtuador);

        }
    }
}