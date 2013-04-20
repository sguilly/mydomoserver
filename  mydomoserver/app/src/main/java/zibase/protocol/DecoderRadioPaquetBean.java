package zibase.protocol;

import java.util.Date;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import zibase.StorageUnitBean;
import zibase.log.Log;
import zibase.sensor.model.SensorEvent;
import zibase.sensor.model.SensorType;
import zibase.sensor.model.SensorValue;

public class DecoderRadioPaquetBean implements ApplicationContextAware {

	private static StorageUnitBean storageUnitBean = null;

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		// TODO Auto-generated method stub

		System.out.println("Chargement du storageunit dans le DecoderRadioPaquetBean");
		storageUnitBean = (StorageUnitBean) context.getBean("storageunit");

	}

	@SuppressWarnings("unchecked")
	public void extraireBalise(String texte) {

		// SensorsStorage sensorsStorage = new SensorsStorage();

		System.out.println("extraireBalise>" + texte);

		System.out.println("---------------------------");

		try {

			@SuppressWarnings("rawtypes")
			Hashtable cleValeur = new Hashtable();

			Pattern p = Pattern.compile("<(.*?)>(.*?)</(.*?)>");
			Matcher m = p.matcher(texte);
			while (m.find()) {

				if (cleValeur.containsKey(m.group(1)) == false) {
					cleValeur.put(m.group(1), m.group(2));
					System.out.println(m.group(1) + "=" + m.group(2));
				}

			}

			System.out.println("-- Match with known device --");

			if (cleValeur.get("rf").equals("433Mhz")) {

				String dev = (String) cleValeur.get("dev");

				if (dev.contains("THGR228N") == true) {
					System.out.println("Oregon THGR228N");

					try {

						String tem = (String) cleValeur.get("tem");
						Float valueTem = (float) Float.parseFloat(tem);

						String hum = (String) cleValeur.get("hum");
						Float valueHum = (float) Float.parseFloat(hum);

						String idSensor = (String) cleValeur.get("id");

						SensorValue temperature = new SensorValue(idSensor,
								valueTem, SensorType.TEMP);

						if (storageUnitBean.storeObject(temperature) == false)
							Log.info("Temperature Store Fail");

						Log.info(temperature.toString());

						SensorValue humidity = new SensorValue(idSensor,
								valueHum, SensorType.HYDRO);

						if (storageUnitBean.storeObject(humidity) == false)
							Log.info("Humidity Store Fail");

						Log.info(humidity.toString());

					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("DecoderRadioPaquetBean>" + e);
					}

				}

				if (dev.equals("OWL CM119/130") == true) {
					// System.out.println("OWL CM119/130");

					try {

						String kw = (String) cleValeur.get("w");
						Float value = (float) Float.parseFloat(kw);

						String kwh = (String) cleValeur.get("kwh");
						Float totalValue = (float) Float.parseFloat(kwh);

						String idSensor = (String) cleValeur.get("id");

						SensorValue power = new SensorValue(idSensor, value,
								SensorType.POWER);
						SensorValue powerTotal = new SensorValue(idSensor,
								totalValue, SensorType.POWERTOTAL);

						if (storageUnitBean.storeObject(power) == false)
							Log.info("Power Store Fail");
						if (storageUnitBean.storeObject(powerTotal) == false)
							Log.info("PowerTotal Store Fail");

						Log.info(power.toString());
						Log.info(powerTotal.toString());

					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("DecoderRadioPaquetBean>" + e);
					}
				}
			} else if (cleValeur.get("rf").equals("868Mhz")) {

				if (cleValeur.containsKey("dev") == true
						&& cleValeur.get("dev").equals("Visonic"))

				{

					String idSensor = (String) cleValeur.get("id");

					SensorEvent detector = new SensorEvent(idSensor,
							new Date(), (String) cleValeur.get("flag1"));

					if (storageUnitBean.storeObject(detector) == false)
						Log.info("DetectorEvent Store Fail");

					Log.info(detector.toString());

				}

			}

		} catch (Exception e) {
			System.out.println(e);
			System.out.println("??>" + texte);
		}

	}

}
