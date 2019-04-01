package servidor;

import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import cliente.PaqueteEnvio;

class MarcoServidor extends JFrame implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private	JTextArea areatexto;

	public MarcoServidor(){
		
		setBounds(1200,300,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);
		
		Thread miHilo = new Thread(this);
		miHilo.start();
		
		}
	
	@Override
	public void run() {
		
		try {
			ServerSocket server = new ServerSocket(9999);
			
			String nick, ip, mensaje;
			ArrayList<String> listaIp = new ArrayList<String>();
			
			PaqueteEnvio paqueteRecibido;
			
			while (true) {
				Socket miSocket = server.accept();
				
				ObjectInputStream paqueteDatos = new ObjectInputStream(miSocket.getInputStream());
				paqueteRecibido = (PaqueteEnvio) paqueteDatos.readObject();
				
				nick = paqueteRecibido.getNick();
				ip = paqueteRecibido.getIp();
				mensaje = paqueteRecibido.getMensaje();
				
				if(!mensaje.equals(" Online")) {

					areatexto.append("\n "+nick+": "+mensaje + " para "+ip);
					
					Socket enviaDestinatario = new Socket(ip, 9090);
					
					ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
					paqueteReenvio.writeObject(paqueteRecibido);
					paqueteReenvio.close();
					
					enviaDestinatario.close();
					miSocket.close();
					
//					DataInputStream flujoentrada = new DataInputStream(miSocket.getInputStream());
//					String mensajetexto = flujoentrada.readUTF();
//					areatexto.append("\n "+mensajetexto);
//					miSocket.close();
				}else {
					// Dectecta Online
					
					InetAddress localizacion = miSocket.getInetAddress();
					String IpRemota = localizacion.getHostAddress();
					
					listaIp.add(IpRemota);
					paqueteRecibido.setIps(listaIp);
					
					//Test
					System.out.println("Online "+IpRemota);
					
					//Envio de lista de ips conectadas al server
					for (String string : listaIp) {
						
						Socket enviaDestinatario = new Socket(string, 9090);
						
						ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
						paqueteReenvio.writeObject(paqueteRecibido);
						paqueteReenvio.close();
						
						enviaDestinatario.close();
						miSocket.close();
						
						System.out.println("Array: "+string);
					}
					
					//----------------
				}
				
				
			}
			
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}