package nuevoSSL;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WebServer extends Thread{
	
	
	private boolean parar;
	private ArrayList<ClientHandler> clientesWeb;
	
	public WebServer()
	{
		parar = false;
		clientesWeb = new ArrayList<>();
		
		
	}
	
	public void cerrarClientes(){
		for (int i = 0; i < clientesWeb.size(); i++) {
			clientesWeb.get(i).parar();
		}
	}
	
	public void stopp(){
		parar = false;
	}
	
	public void run(){
		System.out.println("Webserver Started");
		try {
			ServerSocket serverSocket =  new ServerSocket(80);
			while(!parar) 
			{
				System.out.println("Waiting for the client request");
				Socket remote = serverSocket.accept();
				System.out.println("Connection made");
				new Thread(new ClientHandler(remote)).start();;
				//client.start();
				
			}
			
			cerrarClientes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		new WebServer();
//	}
}
