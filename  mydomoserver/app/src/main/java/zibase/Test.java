package zibase;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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


		String filename = "bean-definition.xml";
		@SuppressWarnings({ "unused", "resource" })
		ApplicationContext context = new FileSystemXmlApplicationContext(filename);

		JpaSensorValueDao jpa = new JpaSensorValueDao();
		
				
		List<SensorValue> resultsTemperature = jpa.findSensorValues("OS439208193");

		for (SensorValue p : resultsTemperature) {

			System.out.println(p.toString());

		}
		
		System.out.println("resultsTemperature=" + resultsTemperature.size());

	}

}
