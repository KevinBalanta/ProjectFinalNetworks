package nuevoSSL;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable{


	private final Socket socket;
	private boolean parar;
	
	public ClientHandler(Socket socket)
	{
		this.socket =  socket;
		parar = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		System.out.println("\nClientHandler Started for " + this.socket);
		handleRequest(this.socket);
		System.out.println("ClientHanlder Terminated for "+  this.socket + "\n");
	
	}
	
	public void parar(){
		parar = true;
	}
	
	public void handleRequest(Socket socket)
	{
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String headerLine = in.readLine();
			// A tokenizer is a process that splits text into a series of tokens
			StringTokenizer tokenizer =  new StringTokenizer(headerLine);
			//The nextToken method will return the next available token
			String httpMethod = tokenizer.nextToken();
			// The next code sequence handles the GET method. A message is displayed on the
			// server side to indicate that a GET method is being processed
			if(httpMethod.equals("GET"))
			{
				System.out.println("Get method processed");
				String httpQueryString = tokenizer.nextToken();
				System.out.println(httpQueryString);
				StringBuilder responseBuffer =  new StringBuilder();
				responseBuffer
				.append("<html>")
				.append("<head>")
				.append("<link href= 'https://fonts.googleapis.com/css?family=Alegreya' rel= 'stylesheet' >")
				.append("<title>TURF Apuestas</title>")
				.append("</head>")
				.append("<script>")
				.append("function bigImg(x) { x.style.height = '190px'; x.style.width = '470px';} ")
				
				.append("function normalImg(x) { x.style.height = '140px'; x.style.width = '450px'; }")
			
				.append("</script>")
				.append("<style type='text/css'>")
				.append("body {background-color: #126ED0; font-family: 'Alegreya', serif;  }")
				.append("h1   {color: #FFC300 ; }")
				.append("</style>")
				.append("</head>")
				.append("<body>")
				.append("<h1 onmouseover= style.color='red' onmouseout= style.color= #FFC300 >Bienvenido a Turf</h1>")
				.append("<img  onmouseover= bigImg(this) onmouseout= normalImg(this) border='0' src = 'http://tienespoker.com//imagenes/deportes/carreras-caballos.jpg' alt = 'TURF' width = 400 height = 120>")
				.append("<form action = 'http://localhost' method = 'POST' enctype= 'text/plain' >")
				.append("<b>Por favor introduzca su ID:</b> <input type = 'text' name = 'id' >")
				.append("<p>")
				.append("<input type= 'submit' name = 'Submit' value= 'Enviar' >")
				.append("</form>")
				.append("</body>")
				.append("</html>");
				sendResponse(socket, 200, responseBuffer.toString());				
			}else if(httpMethod.equals("POST")){
				while(!headerLine.equals("")){
					headerLine = in.readLine();
					System.out.println(headerLine);
//					tokenizer =  new StringTokenizer(headerLine);
//					httpMethod = tokenizer.nextToken();
				}
				headerLine = in.readLine();
				System.out.println(headerLine);
				String[] data = headerLine.split("=");
				//System.out.println(data[1]+"sss");
				//tokenizer.nextToken();
				String id =  data[1];
				BaseData baseData = BaseData.getSingletonBaseData();
				boolean existe = baseData.verificarClienteExiste(id);
				//ver que se hace si existe o no
				//abrir archivo y proceso archivo para ver si ganó
				
				StringBuilder responseBuffer =  new StringBuilder();
				System.out.println("Post method processed");
				if(existe){
					//idcarrera-monto-fecha-numCaballo
					System.out.println("llego");
					String carreras = baseData.darApuestasCliente(id);
					String[] carre = carreras.split("/");
					System.out.println(carreras);
					responseBuffer
					.append("<html>")
					.append("<head>")
					.append("<link href= 'https://fonts.googleapis.com/css?family=Alegreya' rel= 'stylesheet' >")
					.append("<title>TURF Apuestas</title>")
					.append("<style type='text/css'>")
					.append("body {background-color: #2ECC71  ; font-family: 'Alegreya', serif;  }")
					.append("h1   {color: #FFC300 ; }")
					.append("</style>")
					.append("</head>")
					.append("<body>")
					.append("<h1>Turf Apuestas - Resultado para : "+id+"</h1>")
					.append("<img src = 'http://tienespoker.com//imagenes/deportes/carreras-caballos.jpg' width = 400 height = 120>")
					.append("<h3>Apuestas: </h3>")
					.append("<table cellspacing= '3' border = '1'>")
					.append("<tr>")
					.append("<th>ID Carrera</th>")
					.append("<th>monto</th>")
					.append("<th>fecha</th>")
					.append("<th>número Caballo</th>")
					.append("<th>Resultado Apuesta</th>")
					.append("</tr>");
					for (int i = 0; i < carre.length; i++) {
						String[] list = carre[i].split("-");
						responseBuffer.append("<tr>");
						for (int j = 0; j < list.length; j++) {
							
							responseBuffer.append("<td>"+list[j]+"</td>");
						}
						
						responseBuffer.append("<td>"+baseData.ganoCaballo(list[0], list[list.length-1])+"</td>");
						responseBuffer.append("</tr>");
					}
					responseBuffer.append("</table>")
					.append("</body>")
					.append("</html>");
					System.out.println("llego");
					sendResponse(socket, 200, responseBuffer.toString());
				}else{
					responseBuffer
					.append("<html>")
					.append("<head>")
					.append("<title>TURF Apuestas</title>")
					.append("</head>")
					.append("<body>")
					.append("<h1>USUARIO NO ENCONTRADO!</h1>")
					.append("<img src = 'http://tienespoker.com//imagenes/deportes/carreras-caballos.jpg' width = 400 height = 120>")
					.append("</body>")
					.append("</html>");
					sendResponse(socket, 200, responseBuffer.toString());	
				}
				
//				System.out.println("Post method processed");
//				
//				StringBuilder responseBuffer =  new StringBuilder();
			
//				.append("<html>")
//				.append("<head>")
//				.append("<title>Turf Apuestas - Resultado para : "+id+"</title>")
//				.append("</head>")
//				.append("<body>")
//				//si ganó imagen de ganador - si perdió imagen de intentalo otra vez
//				.append("<img src = 'http://tienespoker.com//imagenes/deportes/carreras-caballos.jpg' width = 400 height = 120>")
//				.append("<h3>Apuesta: </h3>")
//				//revisar si el cliente existe o no
//				//en una lista los valores de los resultados
//				.append("<form action = 'http://localhost' method = 'POST' enctype= 'text/plain' >")
//				.append("Por favor introduzca su ID: <input type = 'text' name = 'id' >")
//				.append("<p>")
//				.append("<input type= 'submit' name = 'Submit' value= 'Enviar' >")
//				.append("</form>")
				
//				responseBuffer.append("</body>")
//				.append("</html>");
//				sendResponse(socket, 200, responseBuffer.toString());	
			}
			else
			{
				System.out.println("The HTTP method is not recognized");
				sendResponse(socket, 405, "Method Not Allowed");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void sendResponse(Socket socket, int statusCode, String responseString)
	{
		String statusLine;
		String serverHeader = "Server: WebServer\r\n";
		String contentTypeHeader = "Content-Type: text/html\r\n";
		
		try {
			DataOutputStream out =  new DataOutputStream(socket.getOutputStream());
			if (statusCode == 200) 
			{
				statusLine = "HTTP/1.0 200 OK" + "\r\n";
				String contentLengthHeader = "Content-Length: "
				+ responseString.length() + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes(serverHeader);
				out.writeBytes(contentTypeHeader);
				out.writeBytes(contentLengthHeader);
				out.writeBytes("\r\n");
				out.writeBytes(responseString);
				} 
			else if (statusCode == 405) 
			{
				statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			} 
			else 
			{
				statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
