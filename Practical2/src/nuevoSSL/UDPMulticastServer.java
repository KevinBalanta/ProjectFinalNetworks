package nuevoSSL;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class UDPMulticastServer extends Thread{
	
	public final static String IP = "228.7.6.7";
	public final static int PORT = 9877;
	
	
	private MulticastSocket multicastSocket;
	private InetAddress inetAddress;
	private byte[] data;
	private DatagramPacket packet;
	private boolean streaming;
	private int[] caballoAvance;
	private int caballoChamp;

	public UDPMulticastServer()
	{
		
		try {
			multicastSocket = new MulticastSocket();
			inetAddress = InetAddress.getByName(IP);
			streaming = true;
			caballoAvance =  new int[6];
			caballoChamp = -1;
			for (int i = 0; i < caballoAvance.length; i++) {
				caballoAvance[i]=1;
			}
			//multicastSocket.joinGroup(inetAddress);
			
		
			
		
			
		} catch (IOException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println(
			"Inicio de la carrera");
	}
	
	public void run(){
		
		/** The server application will use an infinite loop to broadcast a new date and time 
		*	every  second. The thread is paused for one second, and then a new date and time
		*   is created using the Data class.  
		**/
		
		caballoChamp = (int)(Math.random()*6)+1;
		
		try{
		while(streaming)
		{
			//para que dure 3 minutos le bajo acá y ya está *******  50 ---> 1000 = 50 seg / como a 3600 
			Thread.sleep(1000);
			String mesage =  Arrays.toString(caballoAvance);
			String message = mesage.substring(1, mesage.length()-1);
			//System.out.println("Sending: " + message );
			data =  message.getBytes();
			packet =  new DatagramPacket(data, message.length(), inetAddress, 9877);
			multicastSocket.send(packet);
			avanzarCarrera();
		}		
		}catch(IOException |InterruptedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//			System.out.println(
//			"UDP Multicast Time Server Terminated");
	}
	
	public void avanzarCarrera(){
		boolean fin = true;
		for (int i = 0; i < caballoAvance.length; i++) {
			if((i+1) == caballoChamp){
				if(caballoAvance[i] <= 16){
					caballoAvance[i] += 2;
				}else{
					caballoAvance[i] += 1;
				}
			}else{
				caballoAvance[i] += (int)(Math.random()*2+1);
			}
			
			if(caballoAvance[i] <= 50 && fin){
				fin = false;
			}
		}
		if(fin){
			BaseData baseData = BaseData.getSingletonBaseData();
			baseData.registrarNuevaCarrera(caballoChamp+"");
			streaming = false;
		}
	}
	
	public void finalizarCarrera(){
		streaming = false;
	}
	
	
//	public static void main(String args[]) 
//	{
//		new UDPMulticastServer();		
//	}
	
	
}