import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;



public class PController {
	int id;
	String host;
	final int noOfNodes = 10;
	int port;
	int basePort = 9000;
	boolean init = true;
	boolean isListening = true;
	HashMap<Integer, Boolean> up = new HashMap<>();
	


	public PController(String id) {
		this.id = Integer.parseInt(id);
		try {
			this.host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host");
			
		}
		this.port = this.basePort + this.id;

	}



	public static void main(String[] args) throws InterruptedException, IOException {
		PController me = new PController("11");

		System.out.println("Controller " + me + " is running.");
		ServerSocket listener = new ServerSocket(me.port);

			while (me.isListening) {
				// System.out.println("ghoomte raho");
				Socket socket = listener.accept();

			}
		
	}
}
