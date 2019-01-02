package nuevoSSL;

import java.io.IOException;
import java.net.*;

public class UDPMulticastClient extends Thread{
	public final static String IP = "228.7.6.7";
	public final static int PORT = 9877;
	
	private MulticastSocket multicastSocket;
	private InetAddress inetAddress;
	private byte[] data;
	private DatagramPacket packet;
	private boolean receiving;
	private int horse;
	private boolean entro;
	private boolean winner;
	private boolean inicio;
	

	public UDPMulticastClient(int hors)
	{
		//System.out.println("UDP Multicast Time Client Started");
		try 
		{
			/**
			 * We are going to make an InetAddress instance using the multicast address of 228.5.6.7.
			 * The client then joins the multicast group using the joinGroup method
			 */
			
			multicastSocket = new MulticastSocket(PORT);
			inetAddress = InetAddress.getByName(IP);
			multicastSocket.joinGroup(inetAddress);
			horse = hors;
			entro = false;
			winner = false;
			inicio = false;
			/**
			 * A DatagramPacket instance is needed to receive messages that were sent to the client. 
			 */
			data =  new byte[256];
		    packet =  new DatagramPacket(data, data.length);
		    receiving = true;
			
		
		} catch (IOException ex) 
		{
			ex.printStackTrace();
			
		}
		//System.out.println("UDP Multicast Time Client Terminated");
	}
	
	
	public void run(){
		/**
		 * The client application then enters an infinite loop where it blocks at the receive method
		 * until the server sends a message. Once the message has arrived, the message is displayed
		 */
		
		try{
		
		while(receiving)
		{
			
			multicastSocket.receive(packet);
			if(!inicio){
				inicio = true;
				System.out.println("Carrera iniciada............. Buena Suerte!!");
			}
			String message = new String(packet.getData(), 0, packet.getLength());
			imprimirCarrera(message);
		}
		if(winner){
			System.out.println("Usted ha ganado..... Felicidades !!");
		}else{
			System.out.println("Fin de la carrera - Has perdido");
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void imprimirCarrera(String mensaje){
		String avance[] = mensaje.split(",");
		String s = "-";
		String o = "o";
		boolean fin = true;
		for (int i = 0; i < avance.length; i++) {
			String linea = "";
			int a = Integer.parseInt(avance[i].trim());
			if(a >= 50){
			for (int j = 0; j < 50; j++) {
				if((i+1) == horse){
					if(!entro){
						entro = true;
						winner = true;
					}
					linea+= o;
				}else{
					entro = true;
				linea+=s;
				}
			}
			linea += "[F]";
			}else if(a<50){
				fin = false;
				for (int j = 0; j < a; j++) {
					if((i+1) == horse){
						linea+= o;
					}else{
					linea+=s;
					}
				}
			}
			System.out.println("["+(i+1)+"]"+linea);
		}
		System.out.println("***************////////////////////////////////////////////////////////*********");
		if(fin){
			
			receiving = false;
		}
	}
	
	public boolean getReceiving(){
		return receiving;
	}
//	public static void main(String args[])
//	{
//		new UDPMulticastClient();
//	}
}
