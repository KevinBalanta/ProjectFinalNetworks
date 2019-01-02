package nuevoSSL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

public class Client {

	public final static String TRUSTTORE_LOCATION = "C:/Users/kevin david/keykevin.jks";
	
	static UDPMulticastClient avanceCaballo;
	static AudioUDPClient voiceClient;
	static AudioStreamingClient soundClient;
	static int apuestaNum;
	static boolean aposto;
	
	
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// System.out.println("SSLClientSocket Started");
		 System.setProperty("javax.net.ssl.trustStore", TRUSTTORE_LOCATION);
			SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			
			Socket cliente;
		try {
			 cliente = sf.createSocket("localHost", 8000);
			

	   BufferedReader inpu = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
	   PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
	   BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			

		apuestaNum =  -1;
		aposto = false;
		
		boolean entro = false;
	   String apuesta = "";
	   String chao = "";
	  // System.out.println("llego cliente");
			if((chao = inpu.readLine())!= null){
		//		System.out.println("llego cliente");
			
			System.out.println(chao);
			}
			
			while (!chao.equalsIgnoreCase("finish")) {
//				System.out.println("llego cliente");
//				System.out.println("entro aqui perro");
				
				
				
				//apuesta-numcaballo-monto-cedula
				if(entro){
				if ((chao = inpu.readLine())!= null ) {
					entro = false;
					System.out.println(chao);
					System.out.println("La carrera comenzará en breve .....");
					chao = "finish";
				}
				}
				//System.out.println("entro aqui perro 2");
				//if(chao.equals("carrera iniciada")){
					
				//}
				
				
				
				if (!chao.equalsIgnoreCase("finish")) {
					//apuesta = in.readLine();
					if((apuesta = in.readLine())!= null ){
						
						if(!aposto){
							if(apuesta.startsWith("apuesta-")){
							String[] i = apuesta.split("-");
							int numC = Integer.parseInt(i[1]);
							if(numC >= 1 && numC <= 6){
							if(apuestaNum == -1){
								apuestaNum = numC;
								aposto = true;
								entro = true;
								voiceClient = new AudioUDPClient();
								avanceCaballo = new UDPMulticastClient(apuestaNum);
								soundClient = new AudioStreamingClient();
								voiceClient.start();
								avanceCaballo.start();
								soundClient.start();
								out.println(apuesta);
							}
							}else{
								System.out.println("Número de caballo incorrecto");

							}
							}else{
								System.out.println("Formato de apuesta incorrecto");
							}
							
							
						
							
						}
					}
				}
				
			}
			boolean a = true;
			while(a){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!avanceCaballo.getReceiving()){
					System.out.println("Finish");
					a = false;
					voiceClient.stopp();
					soundClient.stopp();
				}
			}
			
			System.out.println("Gracias por apostar con turf !!");
		
			inpu.close();
			out.close();
			cliente.close();
			System.exit(0);

		} catch (IOException e) {
			
			System.out.println("se ha finalizado la sesión");
			//System.exit(0);
		}

	}
	
	
	}

