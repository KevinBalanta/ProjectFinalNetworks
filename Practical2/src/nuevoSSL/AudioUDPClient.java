package nuevoSSL;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioUDPClient extends Thread{

	public final static String IP = "230.8.8.7";
	public final static int PORT = 9100;
	
	
	AudioInputStream audioInputStream;
	SourceDataLine sourceDataLine;
	
	private MulticastSocket multicastSocket;
	private InetAddress inetAddress;

	private DatagramPacket packet;
	private boolean receiving;
	

	public AudioUDPClient() {
		// TODO Auto-generated constructor stub
		try {
			multicastSocket = new MulticastSocket(PORT);
			inetAddress = InetAddress.getByName(IP);
			multicastSocket.joinGroup(inetAddress);
			receiving = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private AudioFormat getAudioFormat() {
		float sampleRate = 16000F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	private void playAudio() {
		byte[] buffer = new byte[10000];
		try {
			int count;
			while ((count = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
				if (count > 0) {
					sourceDataLine.write(buffer, 0, count);
				}
			}
		} catch (Exception e) {
			// Handle exceptions
		}
	}

	public void run() {
		try {
			//8088
			
			
		
			
			

        
			
            
			
			byte[] audioBuffer = new byte[10000];
			// ...
			while (receiving) {
				
				
				packet = new DatagramPacket(audioBuffer, audioBuffer.length);
				multicastSocket.receive(packet);
				// ...

				try {
					
					
					byte audioData[] = packet.getData();
					InputStream byteInputStream = new ByteArrayInputStream(audioData);
					AudioFormat audioFormat = getAudioFormat();
					audioInputStream = new AudioInputStream(byteInputStream, audioFormat,
							audioData.length / audioFormat.getFrameSize());
					DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

					sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
					sourceDataLine.open(audioFormat);
					sourceDataLine.start();
					playAudio();
				} catch (Exception e) {
					// Handle exceptions
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopp(){
		receiving = false;
	}


}
