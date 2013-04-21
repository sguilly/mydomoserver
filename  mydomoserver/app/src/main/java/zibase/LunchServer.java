package zibase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class LunchServer {

	@SuppressWarnings("unused")
	private ApplicationContext context;

	private void loadContext() {

		String filename = "bean-definition.xml";
		context = new FileSystemXmlApplicationContext(filename);

	}

	public static void main(String[] args) {

		LunchServer server = new LunchServer();
		server.loadContext();

		System.out.println("Start");

	}

}
