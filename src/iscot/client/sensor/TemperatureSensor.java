package iscot.client.sensor;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.TemperatureSensorEvent;

import iscot.client.Client;

public class TemperatureSensor extends Client{

	/**
	 * 
	 * @param server address and port
	 * @param roomName: string
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 */

	public TemperatureSensor(String ip, int port, Areas area) throws ClassNotFoundException, IOException{
		super(ip, port, Type.SENSOR, area);
	}

	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if(args.length!=3) {
			System.err.println("Invalid arguments!");
		}
		else {
			
			Areas area=Areas.valueOf(args[2]);
			
			// TODO connect to server
			TemperatureSensor client = new TemperatureSensor(args[0], Integer.parseInt(args[1]), area);

			// TODO register with the server
			client.registerWithServer();

			JFrame janela = new JFrame("Temperatura em "+area);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			
			// Criar os componentes gráficos
			JPanel painel=new JPanel();
			painel.setLayout(new BorderLayout());
			SensorControl sensorControl= new SensorControl( "🌡", "Temperatura - "+area, "ºC",
				    0, 50, 1, 25,
				    valor -> {
				    	// TODO handle change in sensor
				    	TemperatureSensorEvent tse = new TemperatureSensorEvent(area, valor);
						System.out.println("TemperatureSensorEvent: " + tse.getValue() + "ºC na área- " + tse.getArea());
						
						// Fase 6
						try {
							client.sendEvent(tse);
						} catch (IOException e) {
							e.printStackTrace();
						}
				    });
			painel.add(sensorControl,BorderLayout.CENTER);
			janela.setContentPane(painel);
			janela.setLocation(600, 400);
			janela.setMinimumSize(janela.getSize());
			janela.pack();
			janela.setVisible(true);
			
		}
	}

}
