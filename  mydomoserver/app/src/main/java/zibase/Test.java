package zibase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import fr.zapi.ZbResponse;
import fr.zapi.Zibase;
import fr.zapi.utils.XmlSimpleParse;

import zibase.dao.JpaSensorValueDao;
import zibase.sensor.model.SensorValue;

@Path("/test")
public class Test {
	
	
	@GET
	@Produces("text/html")
    public String getMessage(){
        System.out.println("sayHello()");
        return "Hello, world!";
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {

/*
		String filename = "bean-definition.xml";
		@SuppressWarnings({ "unused", "resource" })
		ApplicationContext context = new FileSystemXmlApplicationContext(filename);

		JpaSensorValueDao jpa = context.getBean(JpaSensorValueDao.class);
		
				
		List<SensorValue> resultsTemperature = jpa.findSensorValues("OS439208193");

		for (SensorValue p : resultsTemperature) {

			System.out.println(p.toString());

		}
		
		System.out.println("resultsTemperature=" + resultsTemperature.size());

	}*/
		
		try {
			System.out.println("connexion à la Zibase...");
			Zibase zibase = new Zibase("192.168.2.60");

			System.out.println("register sur la Zibase...");

			String host = "192.168.2.20";
			int localport = 9876;				// choisir un port libre

			System.out.println("zibase registering...");
			zibase.hostRegistering(host, localport);

			// maintenant le système enregistré va recevoir tous les messages émis par la Zibase
			// cf. ListenZibase.java
			
					
			DatagramSocket serverSocket = new DatagramSocket(localport);  // bind

			byte[] receiveData = new byte[470];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			while (true) {

				System.out.println("\n---------------------\nreceive...");
				serverSocket.receive(receivePacket);
			

				ZbResponse zbResponse = new ZbResponse(receivePacket.getData());
				System.out.println("id="+zbResponse.getZbHeader().getZibaseId());
				System.out.println("p1="+zbResponse.getZbHeader().getParam1());
				System.out.println("p2="+zbResponse.getZbHeader().getParam2());
				System.out.println("p3="+zbResponse.getZbHeader().getParam3());
				System.out.println("p4="+zbResponse.getZbHeader().getParam4());
				System.out.println("cmd="+zbResponse.getZbHeader().getCommand());
				System.out.println("mycount="+zbResponse.getZbHeader().getMyCount());
				System.out.println("yourcount="+zbResponse.getZbHeader().getYourCount());

				System.out.println("message="+zbResponse.getMessage());

				String id = XmlSimpleParse.getTagValue("id", zbResponse.getMessage());

				System.out.println("id==" + id);
				System.out.println("rf==" + XmlSimpleParse.getTagValue("rf", zbResponse.getMessage()));
				System.out.println("bat==" + XmlSimpleParse.getTagValue("bat", zbResponse.getMessage()));
				System.out.println("lev==" + XmlSimpleParse.getTagValue("lev", zbResponse.getMessage()));
				System.out.println("noise==" + XmlSimpleParse.getTagValue("noise", zbResponse.getMessage()));
				System.out.println("flag3==" + XmlSimpleParse.getTagValue("flag3", zbResponse.getMessage()));
				System.out.println("tem==" + XmlSimpleParse.getTagValue("tem", zbResponse.getMessage()));
				System.out.println("hum==" + XmlSimpleParse.getTagValue("hum", zbResponse.getMessage()));

				// reset buffer
				for (int i = 0; i < receiveData.length; i++) 
					receiveData[i] = 0;

			}



		} catch (UnknownHostException exc) {
			exc.printStackTrace();

		} catch (IOException exc) {
			exc.printStackTrace();


			// System.out.println("unregister sur la Zibase...");
			// zibase.hostUnregistering(host, port);

			System.out.println("terminé avec succès!");

		} catch (Throwable th) {
			System.out.println("EN ERREUR:");
			th.printStackTrace();
		}
	}

}
