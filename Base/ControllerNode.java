package Base;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerNode {

	public static void main(String[] args) throws InterruptedException, IOException {
		ControllerNode me = new ControllerNode("11");

		System.out.println("Controller " + me + " is running.");
		ServerSocket listener = new ServerSocket(me.port);
		try {
			while (me.isListening) {
				// System.out.println("ghoomte raho");
				Socket socket = listener.accept();
				Thread t1 = new Listeners(socket, me);
				t1.start();
				t1.join();
			}
			if (me.up.size() == me.noOfNodes) {
				// System.out.println("in breaker");
				String nodeInfoString = me.getStoreString();
				for (int i = 1; i <= me.noOfNodes; i++) {
					Protocol p = new Protocol(System.currentTimeMillis(), me.id, new int[me.noOfNodes][me.noOfNodes], "establish"+"sp"+nodeInfoString);
					new NotifyThreads(me, i, p, 0).start();
				}
			}

			me.init = false;

			// start listening again for termination
			me.isListening = true;

			while (me.isListening) {

				Socket socket = listener.accept();
				Thread t1 = new Listeners(socket, me);
				t1.start();
				t1.join();
			}
			if (me.down.size() == me.noOfNodes) {
				System.out.println("Terminating all nodes");
				for (int i = 1; i <= me.noOfNodes; i++) {
					Protocol p = new Protocol(System.currentTimeMillis(), me.id, new int[me.noOfNodes][me.noOfNodes], "terminate");
					new NotifyThreads(me, i, p, 0).start();
				}
			}
		} finally {
			listener.close();
		}
	}

	ControllerNode(String id) {
		this.id = Integer.parseInt(id);
		try {
			this.host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host");
			
		}
		this.port = this.basePort + this.id;
	}

	public String toString() {
		return id + "@" + host + ":" + port;
	}

//	public void initStore() {
//		// take from config file
//		for (int i = 1; i <= noOfNodes; i++) {
////			store.put(i, new NodeDef(i, host, basePort + i));
//		}
//		store.put(11, new NodeDef(11, host, basePort + 11));
//	}
	
	
	public String getStoreString(){
	
		String storeInfo="";
		for (NodeDef nd : this.store.values()){
			storeInfo+=nd + "sp";
		}
			
		return storeInfo; 
		
	}
	static String getIDString(int id) {
		if (id < 10) {
			return "0" + id;
		} else {
			return "" + id;
		}
	}

	int id;
	String host;
	final int noOfNodes = 10;
	int port;
	int basePort = 9000;
	// String controllerHostName = "dc11.utdallas.edu";
	boolean init = true;
	boolean isListening = true;
	HashMap<Integer, Boolean> up = new HashMap<>();
	HashMap<Integer, Boolean> down = new HashMap<>();

	ConcurrentHashMap<Integer, NodeDef> store = new ConcurrentHashMap<Integer, NodeDef>();

}

class NotifyThreads extends Thread {

	ControllerNode n;
	int dstId;
	Protocol obj;

	public NotifyThreads(ControllerNode n, int dstId, Protocol obj, int type) {

		this.n = n;
		this.dstId = dstId;
		this.obj = obj;
	}

	public void run() {
		try {
			int port = n.store.get(dstId).port;
			String host = n.store.get(dstId).host;
			InetAddress address = InetAddress.getByName(host);
			Socket dstSocket = new Socket(address, port);
			ObjectOutputStream oos = new ObjectOutputStream(dstSocket.getOutputStream());
			

			
			
			oos.writeObject(obj);
			oos.close();
			dstSocket.close();
		} catch (UnknownHostException e) {
			
		} catch (IOException e) {
			
		}
	}
}

class Listeners extends Thread {

	Socket servSocket;
	ControllerNode n;
	ObjectInputStream iis;

	public Listeners(Socket csocket, ControllerNode n) {

		this.servSocket = csocket;
		this.n = n;
	}

	public void run() {

		try {
			iis = new ObjectInputStream(servSocket.getInputStream());
			Thread.sleep(500);
			if ((n.init == true)) // not init phase
			{
				Protocol msg = (Protocol) iis.readObject();
				String ninfo = msg.type = msg.type.split("#")[1];
				String [] nd = ninfo.split("sp");
				NodeDef ndef = new NodeDef(Integer.parseInt(nd[0]), nd[1], Integer.parseInt(nd[2]));
				n.up.put(ndef.id, true);
				n.store.put(ndef.id,ndef);
				System.out.println("up" + " " + ndef.id);
				if (n.up.size() == n.noOfNodes) {
					n.isListening = false;
				}
			} else if (n.init == false) {
				Protocol msg = (Protocol) iis.readObject();
				msg.type = msg.type.split("#")[1];
				int id = Integer.parseInt(msg.type);
				n.down.put(id, true);
				if (n.down.size() == n.noOfNodes) {
					n.isListening = false;
				}

			}
		} catch (IOException e) {
			
		} catch (InterruptedException e) {
			
		} catch (ClassNotFoundException e) {
			
		} finally {
			try {
				servSocket.close();
			} catch (IOException e) {
				
			}
		}
	}
}
