package iscot.client.sensor;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.LightSensorEvent;

import iscot.client.Client;

public class LightSensor extends Client{

	/**
	 * 
	 * @param server address and port
	 * @param roomName: string
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 */

	public LightSensor(String ip, int port, Areas area) throws ClassNotFoundException, IOException{
		super(ip, port, Type.SENSOR, area);
	}

	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if(args.length!=3) {
			System.err.println("Invalid arguments!");
		}
		else {
			
			Areas area=Areas.valueOf(args[2]);
			
			// TODO connect to server
			LightSensor client = new LightSensor(args[0], Integer.parseInt(args[1]), area);

			// TODO register with the server
			client.registerWithServer();

			JFrame janela = new JFrame("Luminosidade em "+area);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			
			// Criar os componentes gráficos
			JPanel painel=new JPanel();
			painel.setLayout(new BorderLayout());
			SensorControl sensorControl= new SensorControl( "☀", "Luminosidade - "+area, "lux",
				    0, 200, 1, 50,
				    valor -> {
				    	// TODO handle change in sensor
				    	LightSensorEvent lse = new LightSensorEvent(area, valor);
						System.out.println("HumiditySensorEvent: " + lse.getValue() + "% na área- " + lse.getArea());
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
