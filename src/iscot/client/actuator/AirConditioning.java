package iscot.client.actuator;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.ACIcon;
import iscot.enums.Areas;
import iscot.event.AreaTemperatureOrderEvent;
import iscot.enums.AirConditioningMode;

public class AirConditioning {

	/**
	 * 
	 * @param server address and port
	 * @param roomName: string
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if(args.length!=3) {
			System.err.println("Invalid arguments!");
		}
		else {
			// TODO connect to server
			
			// TODO register with the server
			Areas area=Areas.valueOf(args[2]);
			
			JFrame janela = new JFrame("AC em "+area);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		
			
			// Criar os componentes gráficos
			JPanel painel=new JPanel();
			painel.setLayout(new BorderLayout());
			ACIcon ac = new ACIcon();
			painel.add(ac,BorderLayout.CENTER);
			janela.setContentPane(painel);
			janela.setLocation(600, 400);
			janela.setMinimumSize(janela.getSize());
			janela.pack();
			janela.setVisible(true);
			
			Thread.sleep(5000);
			ac.setAtivo(true);
            ac.setModo(AirConditioningMode.Aquecimento);

			// TODO: receive and handle messages from server
			
			// TODO: for first phase: create an appropriate event hardcoded locally and change the GUI accordingly

			// Exemplo desligado
			Thread.sleep(3000);
			AreaTemperatureOrderEvent ordemOff = new AreaTemperatureOrderEvent(area, AirConditioningMode.Desligado);
			System.out.println("Ordem recebida - " + ordemOff);
			ac.setAtivo(false);

			// Exemplo ligado em qualquer modo
			Thread.sleep(3000);
			AreaTemperatureOrderEvent ordemOn = new AreaTemperatureOrderEvent(area, AirConditioningMode.Frio); // Escolhemos frio aqui, mas pode qualquer outro exceto "Desligado"
			System.out.println("Ordem recebida - " + ordemOn);
			ac.setAtivo(true);
		}
	}
}