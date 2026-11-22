package iscot.client.decider;

import java.io.IOException;

import iscot.client.Client;
import iscot.client.actuator.Ventilator;
import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.AreaTemperatureOrderEvent;
import iscot.event.TemperatureSensorEvent;

public class AirConditioningDecider extends Client{

    public AirConditioningDecider(String ip, int port, Areas area) throws ClassNotFoundException, IOException{
		super(ip, port, Type.DECIDER, area);
	}

    public static void main(String[] args) throws NumberFormatException, InterruptedException, ClassNotFoundException, IOException {
        if (args.length != 3){
            System.err.println("Invalid arguments!");
        }
        else {
            Areas area=Areas.valueOf(args[2]);
			
            // connect to server
			AirConditioningDecider client = new AirConditioningDecider(args[0], Integer.parseInt(args[1]), area);

            // register with the server
            client.registerWithServer();

            /*
            // Receção da leitura do  sensor
            Thread.sleep(3000);
            TemperatureSensorEvent tse = new TemperatureSensorEvent(area, 30.0);
            System.out.println("Recebi a leitura de " + tse);

            // Exemplo de ordem a dar ao atuador para usar o modo "Frio" do AC
            boolean quente = tse.getValue() > 25;
            if(quente){
                AreaTemperatureOrderEvent ordemParaAtuador = new AreaTemperatureOrderEvent(area, AirConditioningMode.Frio);
                System.out.println("Emitiu-se a ordem de refrescar para  " + ordemParaAtuador);
            }

            // Exemplo de ordem a dar ao atuador para usar o modo "Aquecimento" do AC
            boolean frio = tse.getValue() < 20;
            if(frio){
                AreaTemperatureOrderEvent ordemParaAtuador = new AreaTemperatureOrderEvent(area, AirConditioningMode.Aquecimento);
                System.out.println("Emitiu-se a ordem de aquecer para  " + ordemParaAtuador);
            }

            // Exemplo de ordem a dar ao atuador para usar o modo "Ventilação" do AC
            boolean boaTemperatura = tse.getValue() > 20 && tse.getValue() < 25;
            if(boaTemperatura){
                AreaTemperatureOrderEvent ordemParaAtuador = new AreaTemperatureOrderEvent(area, AirConditioningMode.Aquecimento);
                System.out.println("Emitiu-se a ordem de ventilar para  " + ordemParaAtuador);
            }*/

            // Fase 6
            while(true){
				Object event = client.receiveEvent();
				if(event instanceof TemperatureSensorEvent && ((TemperatureSensorEvent) event).getType() == Type.SENSOR && ((TemperatureSensorEvent) event).getArea() == client.getArea()){
					if(((TemperatureSensorEvent) event).getValue() > 30){
                        AreaTemperatureOrderEvent order = new AreaTemperatureOrderEvent(area, AirConditioningMode.Frio);
						client.sendEvent(order);
					}
				}
			}
        }
    }
}
