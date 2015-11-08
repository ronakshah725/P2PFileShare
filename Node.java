import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;





public class Node {
	static String id;
	String host;

	final int noOfNodes = 10;
	int port;
	static HashMap<String,String> neighborlist = new HashMap<String,String>();
	static HashMap<String,String> mylist = new HashMap<String,String>();
	int basePort = 9000;
	static Scanner sc;
	boolean joined = false;
	private boolean terminate;

	Node(String i) {

		id = i;
		try {
			this.host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		this.port = this.basePort;
		sc = new Scanner (System.in);

	}

	public String toString() {

		return id + "@" + host + ":" + port;
	}

	static int getrandom(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	public synchronized boolean getTerminate() {
		return terminate;
	}
	
	public synchronized void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}
	public static String join(){
		int c= 0;
		Reader r = new Reader("active.txt");
		try {
			while(r.readFile.readLine()!= null){
				c++;	
			}
			r.readFile.close();
			Reader rw = new Reader("active.txt");

			int line_no = new Random().nextInt(c-1);
			System.out.println(line_no + "," + c);//last line is me
			for (int i=0; i<line_no; i++){
				rw.readFile.readLine();
			}
			String id = rw.readFile.readLine();
			rw.readFile.close();
			return id;


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) throws IOException {

		Node me = new Node (args[0]);
		System.out.println(me);


		new Writer("active.txt").write(id);

		//make a file list
		me.randomGenFiles();

		//PENDING start listener thread
		
		new ListenHandler(me).start();


		if(!id.contentEquals("1"))
		{	
			String neighbour = join();
			System.out.println("Make Neighbour : " + neighbour);
			neighborlist.put(neighbour, getHostName(neighbour));	
			System.out.println(neighborlist);

		}
		boolean asking =true;
		while(asking){
			System.out.println("Joined the System, Enter the operation?" + "\n1.Search" + "\n2.Terminate");
			String a =sc.nextLine();
			switch (a) {
			case "1":
				//// search function
				search();
				break;
			case "2":
				// terminate function
				me.setTerminate(true);;
				//PENDING
				postTerminate();
				asking = false;
				System.out.println("Terminating");
				break;
			default:
				break;
			}
		}
	}

	private static void postTerminate() {
		// TODO Auto-generated method stub
		
	}

	public static void search(){
		boolean asking =true;
		while(asking){
			System.out.println("Enter the mode which you want to do: 1.keyword 2. filename 3.close");
			System.out.println(mylist);
			String mode =sc.nextLine();
			switch (mode) {
			case "1":
				// search function by keyword
				System.out.println("Enter the keyword to search from a-p");
				String keyword = sc.nextLine();
				if(mylist.get(keyword)!=null){
					System.out.println("File already in the system");
					break;
				} else {
					System.out.println("In the request method for keyword");
					// PENDING  request method
					break;
				}

			case "2":
				// search function by filename
				System.out.println("Enter the filename to search from name a-p .txt");
				String filenames = sc.nextLine();
				String fileKey = filenames.split("\\.")[0];
				if(mylist.get(fileKey)!=null){
					System.out.println("File already in the system");
					break;
				} else {

					System.out.println("in the method for filename ");
					//PENDING  request method
					break;
				}
			case "3":
				asking = false;
				break;
			default:
				break;
			}
		}
	}

	public static String getHostName(String neighbor) {

		int id = Integer.parseInt(neighbor);
		if ( id < 10 ) {
			return "dc0" + id + ".utdallas.edu";
		} else {
			return "dc" + id + ".utdallas.edu";
		}
	}

	private void randomGenFiles() throws IOException {
		String characters = "abcdefghijklmnop";
		String initialList = "";
		for (int i = 0; i <3; i++) {
			char filename = characters.charAt(new Random().nextInt(characters.length()));
			initialList += filename  +"\t"+ filename+ ".txt" +  "\n";
			mylist.put(filename+"",filename+".txt");
			new Writer(id+"/"+filename+".txt").write("dwbdkcw");
		}
		new Writer(id+"/"+"fileList"+".txt").write(initialList);
	}
	
	

}








/////////////////////////////////////////////////













class ListenHandler extends Thread {
	Node nodeObj;
	ServerSocket listener;

	public ListenHandler(Node me) {
		nodeObj = me;
	}

	public void run() {
		try {

			listener = new ServerSocket(nodeObj.port);  ///listens on 9000
			while (!nodeObj.getTerminate()) {


				// System.out.println("In listener");
				Socket socket = listener.accept();
				
				new ListenerService(socket, nodeObj).start();
			}
			System.out.println("Listeners closed");
		} catch (IOException e) {

			
		} finally {
			try {
				listener.close();
			} catch (IOException e) {
				
			}
		}

	}
}

class ListenerService extends Thread {
	Socket servSocket;
	Node n;
	BufferedReader is;

	public ListenerService(Socket csocket, Node n) {
		this.servSocket = csocket;
		this.n = n;
		is = null;
	}

	public void run() {
		try {

			ObjectInputStream iis = new ObjectInputStream(servSocket.getInputStream());

			while (true) {
				Protocol msg;
				msg = (Protocol) iis.readObject();
				System.out.println("msg type recd : " + msg.type);
				if (msg == null)
					break;
				if (msg.type.startsWith("search")) {
					
					// HANDLE SEARCH REQUESTS PENDING

					break;
					

				} else if (msg.type.startsWith("forward")) {

					//// HANDLE FORWARDS REQUESTS PENDING
					
					break;

				}

			}
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
			
		} finally {
			try {
				servSocket.close();
			} catch (IOException e) {
				
			}
		}
	}
}






