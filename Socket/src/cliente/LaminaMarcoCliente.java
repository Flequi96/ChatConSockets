package cliente;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class LaminaMarcoCliente extends JPanel implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField campo1;
	private JComboBox ip;
	private JLabel nick;
	private JButton miboton;
	private JTextArea campoChat;
	
	public LaminaMarcoCliente(){
		
		String nickUsuario = JOptionPane.showInputDialog("Nick: ");
	
		JLabel n_nick = new JLabel("Nick: ");
		add(n_nick);
		
		nick = new JLabel();
		nick.setText(nickUsuario);
		add(nick);
		
		JLabel texto=new JLabel("  Online: ");
		add(texto);
		
		ip = new JComboBox();
		add(ip);
		
		campoChat = new JTextArea(12,20);
		
		add(campoChat);
		
		
		
	
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");
		
		EnviaTexto miEvento = new EnviaTexto();
		
		miboton.addActionListener(miEvento);
		
		add(miboton);	
		
		Thread miHilo = new Thread(this);
		miHilo.start();
		
	}
	
	private class EnviaTexto implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println(campo1.getText());
			
			campoChat.append("\n"+campo1.getText());
			
			try {
				Socket miSocket = new Socket("192.168.8.102", 9999);
				
				PaqueteEnvio datos = new PaqueteEnvio();
				datos.setNick(nick.getText());
				datos.setIp(ip.getSelectedItem().toString());
				datos.setMensaje(campo1.getText());
				
				ObjectOutputStream paqueteDatos = new ObjectOutputStream(miSocket.getOutputStream());
				
				paqueteDatos.writeObject(datos);
				
				miSocket.close();
				
//				DataOutputStream flujoSalida = new DataOutputStream(miSocket.getOutputStream());
//				flujoSalida.writeUTF(campo1.getText());
//				flujoSalida.close();
				
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}
			
		}


		
	}

	@Override
	public void run() {
		
		try {
			
			ServerSocket servidorCliente  = new ServerSocket(9090);
			
			Socket cliente;
			PaqueteEnvio paqueteRecibido;
			
			while(true) {
				
				cliente = servidorCliente.accept();
				
				ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());
				paqueteRecibido = (PaqueteEnvio) flujoEntrada.readObject();
				
				if(!paqueteRecibido.getMensaje().equals(" Online")) {
					campoChat.append("\n"+paqueteRecibido.getNick() + ": "+paqueteRecibido.getMensaje());
				}else {
					ArrayList<String> ipsMenu = new ArrayList<String>();
					ipsMenu = paqueteRecibido.getIps();
					ip.removeAllItems();
					for (String string : ipsMenu) {
						ip.addItem(string);
					}
				}
				
				
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
		

	
}