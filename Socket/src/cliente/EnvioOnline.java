package cliente;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * Envio señal online.
 */

public class EnvioOnline extends WindowAdapter{

	@Override
	public void windowOpened(WindowEvent e) {
		
		try {
			
			//IP/Puerto ubicacion del servidor
			Socket miSocket = new Socket("192.168.8.102", 9999);
			
			PaqueteEnvio datos = new PaqueteEnvio();
			datos.setMensaje(" Online");
			
			ObjectOutputStream paqueteDatos = new ObjectOutputStream(miSocket.getOutputStream());
			paqueteDatos.writeObject(datos);
			miSocket.close();
			
		} catch (Exception e2) {
			
		}
		
	}
	
	
	
}
