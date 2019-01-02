package nuevoSSL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class AudioStreamingServer extends Thread {
	public final static String IP = "229.4.3.0";
	public final static int PORT = 9907;

	private MulticastSocket multicastSocket;
	private InetAddress inetAddress;
    private final String audioFilePath = "./source/SoundCarrera.wav";
    private File audioFile;
    private boolean sigue;

    public AudioStreamingServer() {
    	try {
			multicastSocket = new MulticastSocket();
			inetAddress = InetAddress.getByName(IP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        audioFile = new File(audioFilePath);
    }

    @Override
    public void run() {
        try {
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

            byte[] data = new byte[100];
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, PORT);
         sigue = true;
            while (sigue) {
                int size = audioInputStream.read(data);
                if (size == -1) {
                   sigue = false;
                } else {
                    datagramPacket.setLength(size);
                  
                    Thread.sleep(1);
                    multicastSocket.send(datagramPacket);
                   // Thread.sleep(1);
                }
            }
            audioInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    
    public void stopp(){
    	sigue = false;
    }
}