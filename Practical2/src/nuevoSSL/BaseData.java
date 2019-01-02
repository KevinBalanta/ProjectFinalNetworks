package nuevoSSL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
/**
 * Clase que representa un gestor de la Base de Datos, sólo maneja archivo .txt
 * @author kevin david
 *
 */
public class BaseData {
	/**
	 * ruta de la carpeta donde están todos los archivos objetivo
	 */
	private final static String RutaInicial = "./source/";
	/**
	 * lista de todos los clientes que están registrados en la aplicación
	 */
	private ArrayList<String> clientes;
	/**
	 * el id de la carrera actual
	 */
	private int idCarreraActual;
	// key : id carrera  , value : caballo ganador
	/**
	 * hashmap donde se guardan las parejas (Idcarrera,NumeroCaballoGanador)
	 */
	private HashMap<String, String> carreras;
	/**
	 * Objeto Base Data --> Singleton
	 */
	private static BaseData baseData;
	/**
	 * constructor del gestor de la base de datos BaseData
	 * cuando se ejecuta el servidor principal, se cargan los archivos, está carga se refiere a leer los
	 * datos y agregarlos a las correspondientes estructuras de datos
	 */
	private BaseData(){
		//llenar todos los atributos
		clientes = new ArrayList<>();
		carreras = new HashMap<>();
		
		cargarArchivos();
	}
	/**
	 * devuelve una referencia unica de la base de datos
	 * @return clase BaseData
	 */
	public static BaseData getSingletonBaseData(){
		if(baseData == null){
			baseData = new BaseData();
		}
		
		return baseData;
	}
	/**
	 * método donde se cargan los archivos
	 */
	public void cargarArchivos(){
		FileReader lector;
		try {
			lector= new FileReader(RutaInicial+"Clientes.txt");
			BufferedReader clientes2 = new BufferedReader(lector);
			String cad ="";
			
				while((cad = clientes2.readLine())!= null){
					this.clientes.add(cad);
		} 
			lector = new FileReader(RutaInicial+"Carrera.txt");
			clientes2 = new BufferedReader(lector);
			idCarreraActual = 0;
			while((cad = clientes2.readLine())!= null){
				String[] cadena = cad.split("-");
				carreras.put(cadena[0], cadena[1]);
				idCarreraActual++;
	}
				
				clientes2.close();
				
				
				}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	//pre: el archivo de la carrera ya está creado
	/**
	 * se guarda una nueva carrera
	 * @param caballoGanador. el número de caballo que ganó en la carrera
	 */
	public void registrarNuevaCarrera(String caballoGanador){
		//ya se sabe el id carrera asi que no entra por parametro --DONE--
		//IDCARRERA - CABALLOGANADOR
		
		
		try {
			FileWriter escritor = new FileWriter(RutaInicial+"Carrera.txt", true);
			BufferedWriter escritor2 = new BufferedWriter(escritor);
			escritor2.write(idCarreraActual+"-"+caballoGanador);
			escritor2.newLine();
			escritor2.close();
			carreras.put(idCarreraActual+"", caballoGanador);
			idCarreraActual++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * método para verificar que el cliente existe
	 * @param idCliente. el id del cliente
	 * @return un booleano. true si el cliente existe, false de lo contrario
	 */
	public boolean verificarClienteExiste(String idCliente){
		
		return clientes.contains(idCliente);
	}
	
	/**
	 * método que me agrega un cliente al correspo archivo
	 * @param idCliente. el id del cliente
	 */
	public void agregarCliente(String idCliente){
		try {
			FileWriter escritor = new FileWriter(RutaInicial+"Clientes.txt", true);
			BufferedWriter escritor2 = new BufferedWriter(escritor);
			escritor2.write(idCliente);
			escritor2.newLine();
			escritor2.close();
			clientes.add(idCliente);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	//se mira si el archivo del cliente existe o no
	//no existe se crea uno nuevo y se registra el cliente, si existe se registra en su archivo
	//mirar lo del id de la carrera
	/**
	 * método que me crea un archivo del cliente si es la primera vez que el cliente apuesta, si no es la
	 * primera vez agrega los datos al archivo del cliente
	 * @param monto. monto de apuesta
	 * @param numCaballo. 
	 * @param idCliente
	 */
	public void crearArchivoCliente(String monto, String numCaballo, String idCliente){//--DONE--
		//ya se sabe el id carrera asi que no entra por parametro, calcular aquí la fecha
		//SE BUSCA EL ARCHIVO DEL CLIENTE SI EXISTE O SINO CREAR UNO NUEVO
		//idcarrera-monto-fecha-numCaballo
		
		Date fecha = new Date();
		
		if(verificarClienteExiste(idCliente)){
			try {
				FileWriter escritor = new FileWriter(RutaInicial+idCliente+".txt", true);
				BufferedWriter escritor2 = new BufferedWriter(escritor);
				escritor2.write(idCarreraActual+"-"+monto+"-"+fecha.toString()+"-"+numCaballo);
				escritor2.newLine();
				escritor2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			
			File archivo = new File(RutaInicial+idCliente+".txt");
			agregarCliente(idCliente);
			try {
				FileWriter escritor = new FileWriter(archivo, true);
				BufferedWriter escritor2 = new BufferedWriter(escritor);
				escritor2.write(idCarreraActual+"-"+monto+"-"+fecha.toString()+"-"+numCaballo);
				escritor2.newLine();
				escritor2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
	}
	
	public String ganoCaballo(String idCarrera, String numC){
		if(carreras.containsKey(idCarrera)){
			if (carreras.get(idCarrera)== numC) {
				return "Ganó";
			}else{
				return "Perdió";
			}
		}
		return "Carrera No Encontrada";
	}
	
	//se verificó que el clliente existia
	public String darApuestasCliente(String idCliente){
		//idcarrera-monto-fecha-numCaballo
		String apuestas = "";
		String a = "";
		FileReader lector;
		try {
			lector = new FileReader(RutaInicial+idCliente+".txt");
			BufferedReader lect = new BufferedReader(lector);
			while((apuestas = lect.readLine())!=null){
				a+=apuestas+"/";
				
			}
			lect.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return a;
	}
	
	
	
}
