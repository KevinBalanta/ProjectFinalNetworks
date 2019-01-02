package nuevoSSL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

public class AudioStreamingClient extends Thread {
	public final static String IP = "229.4.3.0";
	public final static int PORT = 9907;
	
	private MulticastSocket multicastSocket;
	private InetAddress inetAddress;
	private boolean sigue;
	

    public AudioStreamingClient(){
       
    }

    @Override
    public void run() {
        try {
        	multicastSocket = new MulticastSocket(PORT);
			inetAddress = InetAddress.getByName(IP);
			multicastSocket.joinGroup(inetAddress);

            //PCM_SIGNED 44100.0 Hz, 16 bit, stereo, 4 bytes/frame, little-endian
          
            AudioFormat af = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED,19140.0F, 16, 2,4,19140.0F,false);

            DataLine.Info dataineInfo = new DataLine.Info(SourceDataLine.class, af);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataineInfo);
            sourceDataLine.open();
            sourceDataLine.start();

            byte[] data = new byte[100];
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
            int i = 0;
            sigue = true;
            while (i<= 35200 && sigue) {
                multicastSocket.receive(datagramPacket);
                sourceDataLine.write(data, 0, datagramPacket.getLength());
                i++;
            }
            sourceDataLine.drain();
            sourceDataLine.stop();
            sourceDataLine.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stopp(){
    	sigue = false;
    }
}