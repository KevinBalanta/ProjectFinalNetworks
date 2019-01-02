package nuevoSSL;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioUDPServer extends Thread{
	public final static String IP = "230.8.8.7";
	public final static int PORT = 9100;
	//
	private final byte audioBuffer[] = new byte[10000];
	//representa el dispositivo que es la fuente del sonido
	private TargetDataLine targetDataLine;
	private MulticastSocket multiCastSocket;
	private InetAddress inetAddress;
	
	private DatagramPacket packet;
	private boolean streaming;
	
	//**************
	
	
	public AudioUDPServer(){
		try {
			multiCastSocket = new MulticastSocket();
			inetAddress = InetAddress.getByName(IP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		streaming = true;
		setupAudio();
		
	}
	
	//establece un formato de audio
	//channels 1 el audio es mono, el bonus sería stereo??
	private AudioFormat getAudioFormat(){
		float sampleRate = 16000F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}
	
	private void setupAudio(){
		try{
			AudioFormat audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
			
		}catch(Exception ex){
			System.out.println("HA OCURRIDO UN ERROR A");
		System.exit(0);
		}
	}

	public void run(){
	try{
		  
		
		 
		 
		
		 while (streaming) {
			 
			
				int count = targetDataLine.read(audioBuffer, 0, audioBuffer.length);
				if (count > 0) {
					DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length, inetAddress, PORT);
					multiCastSocket.send(packet);
				}
			}
	
	
	}catch(Exception ex){
		ex.printStackTrace();
	}
	
	}
	
	
	public void stopp(){
		streaming = false;
	}

}
