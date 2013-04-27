package zibase.protocol;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import zibase.protocol.DecoderRadioPaquetBean;

public class UDPServerBean extends Thread implements ApplicationContextAware {
	
	private static DecoderRadioPaquetBean decoderRadioPaquetBean = null; 
	
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		// TODO Auto-generated method stub
		
		System.out.println("Chargement du storageunit dans le UDPServerBean");
		decoderRadioPaquetBean = (DecoderRadioPaquetBean) context.getBean("decoder");
		
	}
	
	String ipZibase;
	String ipServer;
	
	
	public void setIpZibase(String ipZibase) {
		this.ipZibase = ipZibase;
	}

	public void setIpServer(String ipServer) {
		this.ipServer = ipServer;
	}



	/**
	 * @param args
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		System.out.println("Zibase Listener V1");

		//String serverHostname = new String("192.168.2.60");

		try {

			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress;

			IPAddress = InetAddress.getByName(ipZibase);

			byte[] sendData = new byte[70];

			System.out.println("SpyZibaseProtocol -initiateCommunication-");

			int cpt = 0;

			InetAddress address = InetAddress.getByName(ipServer);

			sendData[cpt++] = ((byte) 'Z');
			sendData[cpt++] = ((byte) 'S');
			sendData[cpt++] = ((byte) 'I');
			sendData[cpt++] = ((byte) 'G');

			sendData[cpt++] = ((byte) 0);
			sendData[cpt++] = ((byte) 13);

			for (int i = 0; i < 44; i++) {
				sendData[cpt++] = ((byte) 0);
			}

			byte[] ip = address.getAddress();

			sendData[cpt++] = (ip[0]);
			sendData[cpt++] = (ip[1]);
			sendData[cpt++] = (ip[2]);
			sendData[cpt++] = (ip[3]);

			System.out.println("ip " + ip[0] + "." + ip[1] + "." + ip[2] + "."
					+ ip[3]);

			sendData[cpt++] = ((byte) 0);
			sendData[cpt++] = ((byte) 0);
			sendData[cpt++] = ((byte) 0xC3);
			sendData[cpt++] = ((byte) 0x4F);

			for (int i = 0; i < 12; i++) {
				sendData[cpt++] = ((byte) 0);
			}

			byte[] receiveData = new byte[1024];
			// String sentence = inFromUser.readLine();
			// sendData = sentence.getBytes();

			System.out.println("L=" + sendData.length + " > " + sendData[0]
					+ sendData[1]);
			
			@SuppressWarnings("resource")
			DatagramSocket serverSocket = new DatagramSocket(49999);

			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, 49999);
			clientSocket.send(sendPacket);
			
			clientSocket.close();

			//for (int i = 0; i < 10; i++) 
			for(;;)
			{	System.out.println(">");
			
				DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
				serverSocket.receive(receivePacket);
				String modifiedSentence = new String(receivePacket.getData());
				System.out.println("FROM SERVER:" + modifiedSentence+"\n");
				
				System.out.println(String.format("%040x", new BigInteger(modifiedSentence.substring(70).getBytes(/*YOUR_CHARSET?*/))));
				
				decoderRadioPaquetBean.extraireBalise(modifiedSentence.substring(70));
				
			}

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

}


