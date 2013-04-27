package zibase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import zibase.log.Log;
import zibase.sensor.model.SensorEvent;
import zibase.sensor.model.SensorType;
import zibase.sensor.model.SensorValue;

import fr.zapi.ZbResponse;
import fr.zapi.Zibase;
import fr.zapi.utils.XmlSimpleParse;

public class ZibaseBean extends Thread implements ApplicationContextAware {

	String hostIp = "192.168.2.20";
	String zibaseIp = "192.168.2.60";
	int localport = 9876; // choisir un port libre

	private static StorageUnitBean storageUnitBean = null;

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		// TODO Auto-generated method stub
		System.out.println("Chargement du storageunit dans le ZibaseBean");
		storageUnitBean = (StorageUnitBean) context.getBean("storageunit");

	}

	@Override
	public void run() {

		Zibase zibase = new Zibase(zibaseIp);

		while (true) {

			System.out.println("zibase registering...");
			try {

				zibase.hostRegistering(hostIp, localport);

				@SuppressWarnings("resource")
				DatagramSocket serverSocket = new DatagramSocket(localport); // bind

				byte[] receiveData = new byte[470];
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);

				while (true) {

					System.out.println("\n---------------------\nreceive...");
					serverSocket.receive(receivePacket);

					ZbResponse zbResponse = new ZbResponse(
							receivePacket.getData());

					String id = XmlSimpleParse.getTagValue("id",
							zbResponse.getMessage());

					if (id != null) {

						/*
						 * System.out.println("id=" +
						 * zbResponse.getZbHeader().getZibaseId());
						 * System.out.println("p1=" +
						 * zbResponse.getZbHeader().getParam1());
						 * System.out.println("p2=" +
						 * zbResponse.getZbHeader().getParam2());
						 * System.out.println("p3=" +
						 * zbResponse.getZbHeader().getParam3());
						 * System.out.println("p4=" +
						 * zbResponse.getZbHeader().getParam4());
						 * System.out.println("cmd=" +
						 * zbResponse.getZbHeader().getCommand());
						 * System.out.println("mycount=" +
						 * zbResponse.getZbHeader().getMyCount());
						 * System.out.println("yourcount=" +
						 * zbResponse.getZbHeader().getYourCount());
						 */

						System.out
								.println("message=" + zbResponse.getMessage());

						/*
						 * System.out.println("id==" + id);
						 * System.out.println("rf==" +
						 * XmlSimpleParse.getTagValue("rf",
						 * zbResponse.getMessage())); System.out.println("bat=="
						 * + XmlSimpleParse.getTagValue("bat",
						 * zbResponse.getMessage())); System.out.println("lev=="
						 * + XmlSimpleParse.getTagValue("lev",
						 * zbResponse.getMessage()));
						 * System.out.println("noise==" +
						 * XmlSimpleParse.getTagValue("noise",
						 * zbResponse.getMessage()));
						 * System.out.println("flag3==" +
						 * XmlSimpleParse.getTagValue("flag3",
						 * zbResponse.getMessage())); System.out.println("tem=="
						 * + XmlSimpleParse.getTagValue("tem",
						 * zbResponse.getMessage())); System.out.println("hum=="
						 * + XmlSimpleParse.getTagValue("hum",
						 * zbResponse.getMessage()));
						 */
						
						String temTag = XmlSimpleParse.getTagValue("tem",
								zbResponse.getMessage());

						if (temTag != null) {
							Float valueTem = (float) Float.parseFloat(temTag);
							SensorValue value = new SensorValue(id, valueTem,
									SensorType.TEMP);

							if (storageUnitBean.storeObject(value) == false)
								Log.info("Temperature Store Fail");

							Log.info(value.toString());
						}

						String humTag = XmlSimpleParse.getTagValue("hum",
								zbResponse.getMessage());

						if (temTag != null) {
							Float valueHum = (float) Float.parseFloat(humTag);
							SensorValue value = new SensorValue(id, valueHum,
									SensorType.HYDRO);

							if (storageUnitBean.storeObject(value) == false)
								Log.info("Humidity Store Fail");

							Log.info(value.toString());
						}

						String kwhTag = XmlSimpleParse.getTagValue("kwh",
								zbResponse.getMessage());

						if (kwhTag != null) {
							Float valuekwh = (float) Float.parseFloat(kwhTag);
							SensorValue value = new SensorValue(id, valuekwh,
									SensorType.POWERTOTAL);

							if (storageUnitBean.storeObject(value) == false)
								Log.info("kwh Store Fail");

							Log.info(value.toString());
						}

						String wTag = XmlSimpleParse.getTagValue("w",
								zbResponse.getMessage());

						if (wTag != null) {
							Float valuew = (float) Float.parseFloat(wTag);
							SensorValue value = new SensorValue(id, valuew,
									SensorType.POWER);

							if (storageUnitBean.storeObject(value) == false)
								Log.info("w Store Fail");

							Log.info(value.toString());
						}

						String devTag = XmlSimpleParse.getTagValue("dev",
								zbResponse.getMessage());

						if (devTag.contains("Visonic")) {
							String flag1Tag = XmlSimpleParse.getTagValue(
									"flag1", zbResponse.getMessage());

							if (flag1Tag != null && flag1Tag.contains("Alarm")) {
								SensorEvent value = new SensorEvent(id,
										flag1Tag);

								if (storageUnitBean.storeObject(value) == false)
									Log.info("w Store Fail");

								Log.info(value.toString());
							}
						}

					}

					// reset buffer
					for (int i = 0; i < receiveData.length; i++)
						receiveData[i] = 0;

				}

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
