package zibase;

import java.io.IOException;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import org.springframework.context.SmartLifecycle;

@SuppressWarnings("restriction")
public class RestServerBean implements SmartLifecycle {

	private HttpServer server;
	private boolean running = false;
	
	String BASE_URI;


	public void setBASE_URI(String bASE_URI) {
		BASE_URI = bASE_URI;
	}

	public RestServerBean() {

	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void start() {

		
		try {
			server = HttpServerFactory.create(BASE_URI); 
			
			server.start();
			
			running = true;
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		System.out.println("Server is listening on port 8080");
	}

	@Override
	public void stop() {
		System.out.println("Stopping REST server...");
		if (server != null) {
			server.stop(1);

			running = false;
		}
	}

	@Override
	public void stop(Runnable runnable) {
		stop();
		runnable.run();
	}

	@Override
	public int getPhase() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isAutoStartup() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
