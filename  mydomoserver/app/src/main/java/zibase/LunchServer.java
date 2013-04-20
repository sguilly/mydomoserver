package zibase;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;


public class LunchServer {

	private void loadContext() {
		String filename = "bean-definition.xml";
		ApplicationContext context = new FileSystemXmlApplicationContext(
				filename);

	}

	public static void main(String[] args) {

		LunchServer server = new LunchServer();
		server.loadContext();

		System.out.println("Start");
		
		

	}

}
