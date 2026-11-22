package iscot.client.actuator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.Client;
import iscot.client.gui.LightIcon;
import iscot.client.gui.VentilatorIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.AreaVentilationOrderEvent;

public class Ventilator extends Client{

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

	public Ventilator(String ip, int port, Areas area) throws ClassNotFoundException, IOException{
		super(ip, port, Type.ACTUATOR, area);
	}

	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if(args.length!=3) {
			System.err.println("Invalid arguments!");
		}
		else {
			Areas area=Areas.valueOf(args[2]);
			
			// TODO connect to server
			Ventilator client = new Ventilator(args[0], Integer.parseInt(args[1]), area);
			
			// TODO register with the server
			client.registerWithServer();

			JFrame janela = new JFrame("Ventilador em "+area);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		
			
			// Criar os componentes gráficos
			JPanel painel=new JPanel();
			painel.setLayout(new BorderLayout());
			VentilatorIcon ventilator = new VentilatorIcon();
			painel.add(ventilator,BorderLayout.CENTER);
			janela.setContentPane(painel);
			janela.setLocation(600, 400);
			janela.setMinimumSize(janela.getSize());
			janela.pack();
			janela.setVisible(true);
			
			Thread.sleep(5000);
			ventilator.setAtivo(true);

			// TODO: receive and handle messages from server
			
			// TODO: for first phase: create an appropriate event hardcoded locally and change the GUI accordingly

			//Exemplo para desligar
			Thread.sleep(3000);
			AreaVentilationOrderEvent ordemOff = new AreaVentilationOrderEvent(area, false);
			System.out.println("Ordem recebida - " + ordemOff);
			ventilator.setAtivo(ordemOff.isActivate());

			//Exemplo para ligar
			Thread.sleep(3000);
			AreaVentilationOrderEvent ordemOn = new AreaVentilationOrderEvent(area, true);
			System.out.println("Ordem recebida - " + ordemOn);
			ventilator.setAtivo(ordemOn.isActivate());
		}
	}
}
