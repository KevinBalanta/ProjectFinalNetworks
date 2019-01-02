package nuevoSSL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Calendar;


import javax.net.ssl.SSLServerSocketFactory;



public class Server {
	public static final String KEYSTORE_LOCATION = "C:/Users/kevin david/keykevin.jks";
	public static final String KEYSTORE_PASSWORD = "ABCD1234";
	static int[] caballos;
	static boolean accept;
	static ThreadGroup grupo;
	static ServerSocket servidor;
	static UDPMulticastServer animacionCarrera;
	static AudioStreamingServer soundServer;
	static AudioUDPServer voiceServer;
	static WebServer webServer;
	static ArrayList<HiloServer> hilos;
	static BaseData baseData;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
		System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);
		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		grupo = new ThreadGroup("grupo");
		baseData = BaseData.getSingletonBaseData();
		webServer = new WebServer();
		webServer.start();
		hilos = new ArrayList<>();
		servidor = null;
		caballos = new int[6];
		int numActual = 0;
//int max = 10;
		accept = true;
		HiloTime time = null;
//Calendar time = Calendar.getInstance();
//long ti = time.getTime().getMinutes()*60+time.getTime().getSeconds();
		try {
			servidor = ssf.createServerSocket(8000);
			//System.out.println(time.getTime().getSeconds());
			
			while ( accept) {
				//System.out.println((Calendar.getInstance().getTime().getSeconds()+Calendar.getInstance().getTime().getMinutes()*60)-ti+"");
				//if(numActual < max){
				Socket c = servidor.accept();
				//System.out.println("ACCEPT");
				if(numActual == 0){
				time = new HiloTime();
				time.start();
				//System.out.println("ACCEPT");
				}
				HiloServer hilo = new HiloServer(c, grupo, "hilo"+numActual);
				hilo.start();
				//System.out.println("ACCEPT");
				hilos.add(hilo);
				numActual++;
			//	}
				
			}
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				//printApuesta();
			//	accept = false;
				//System.out.println("finalizo!");
				servidor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	static synchronized void printApuesta() {
		for (int i = 0; i < caballos.length; i++) {
			System.out.println("caballo "+(i+1)+" apostado:$" + caballos[i]);
		}
	}
	static synchronized void sumarCaballo(int caballo, int apuesta) {
		caballos[caballo-1] += apuesta;
	}
	
	static synchronized void pararHilos(){
		try {
			voiceServer.stopp();
			servidor.close();
			webServer.stopp();
			accept = false;
			for (int i = 0; i < hilos.size(); i++) {
				hilos.get(i).finalizar();
			}
			grupo.interrupt();
			printApuesta();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		//System.exit(0);
	}
	
	static void iniciarAnimacion(){
		//hacer multicast de animación e iniciar streaming de sonido
		
		
		for (int i = 0; i < hilos.size(); i++) {
			hilos.get(i).finalizar();
		}
		animacionCarrera = new UDPMulticastServer();
		voiceServer = new AudioUDPServer();
		soundServer = new AudioStreamingServer();
		
		animacionCarrera.start();
		voiceServer.start();
		soundServer.start();
		try {
			animacionCarrera.join();
			//soundServer.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
static class HiloTime extends Thread{
	Calendar time;
	
	public HiloTime(){
		time =Calendar.getInstance();
		
	}
	
	public void run(){
while((((Calendar.getInstance().getTime().getSeconds()+Calendar.getInstance().getTime().getMinutes()*60)-(time.getTime().getSeconds()+time.getTime().getMinutes()*60)) <= 180 )){
			
		}
try{
	iniciarAnimacion();
	pararHilos();
}catch(Exception e){
	e.printStackTrace();
}
	}
	
	
}


	
static class HiloServer extends Thread{

		Socket client;
		BufferedReader inpu;
		PrintWriter out;
		//boolean ak ;

		public HiloServer(Socket cliente , ThreadGroup grupo, String name) {
			super(grupo, name);
			this.client = cliente;
		//ak = true;
		
		}

		@Override
		public void run() {

			

			try {
				inpu = new BufferedReader(new InputStreamReader(client.getInputStream()));
				out = new PrintWriter(client.getOutputStream(), true);
				out.println("Ingrese su apuesta así:apuesta-#caballo-monto-Cedula ");
				//out.println(" ");
				//System.out.println("llego");
				String apuesta = "";
				
				while (accept) {

					//TODO Manejar mensajes del servidor
//					if (in.ready()) {
//						comando = in.readLine();
//						if (comando.equalsIgnoreCase("close")) {
//							break;
//						} else {
//							cOut.println(comando);
//						}
//					}
					
					if( (apuesta = inpu.readLine())!= null && accept){
						//apuesta = inpu.readLine();
						//System.out.println("llego server");
						System.out.println(apuesta);
						if(apuesta.startsWith("apuesta-")){
							//apuesta-numcaballo-monto-cedula
							String[] i = apuesta.split("-");
							int numC = Integer.parseInt(i[1]);
							int dine = Integer.parseInt(i[2]);
							String cedula = i[3];
							sumarCaballo(numC, dine);
							baseData.crearArchivoCliente(i[2], i[1], i[3]);
							out.println("usted ha apostado: "+"$"+dine+" al caballo "+numC);
						}else if(apuesta.equals("rechazado")){
							out.println(apuesta);
						}
//						if(apuesta.equalsIgnoreCase("finish")){
//							
//								interrupt();
//							
//						}
					}

//					if (cIn.ready()) {
//						String linea = cIn.readLine();
//						String[] mensaje = linea.split(":");
//						
						//TODO verficar existencia de usuario
						
					//}

				}
				System.out.println("finalizó server");
				out.println("finish");
				out.flush();

				inpu.close();
				out.close();
				client.close();
				//System.exit(0);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		
		public void finalizar(){
			out.println("finish");
		}
	
		

	}

	
	
	
	
	}


