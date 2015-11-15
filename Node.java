import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Node {
	String id;
	String host;
	final int noOfNodes = 2;
	int port;
	HashMap<String, String> neighborlist = new HashMap<String, String>();
	HashMap<String, String> mylist = new HashMap<String, String>();
	
	public synchronized HashMap<String, String> getMylist() {
		return mylist;
	}

	public synchronized void put(String key, String value) {
		this.mylist.put(key, value);
	}
	
	public synchronized String printList(HashMap<String, String> list){
		
		return list.toString();
		
	}



	int basePort = 9000;
	int replyPort = 9002;
	static Scanner sc;
	boolean joined = false;
	private boolean terminate = false;
	ServerSocket s;
	String replies = "";

	Node(String i) {

		this.id = i;
		try {
			this.host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		this.port = this.basePort;
		sc = new Scanner(System.in);

	}
	// this ronak

	/// 9000 handle requests
	/// 9002 handle replies

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

	public static String join() {
		int c = 0;
		Reader r = new Reader("active.txt");
		try {
			while (r.readFile.readLine() != null) {
				c++;
			}
			r.readFile.close();
			Reader rw = new Reader("active.txt");

			int line_no = new Random().nextInt(c - 1);
//			System.out.println(line_no + "," + c);// last line is me
			for (int i = 0; i < line_no; i++) {
				rw.readFile.readLine();
			}
			String id = rw.readFile.readLine();
			rw.readFile.close();
			return id;

		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws IOException {

		Node me = new Node(args[0]);
		System.out.println(me);
		new Writer("active.txt").write(me.id);

		// make a file list
		me.randomGenFiles();

		// start listener thread
		new ListenHandler(me).start();

		// 1 will start the system with no neighbours

		if (!me.id.contentEquals("1")) {
			String neighbour = join();

			// tell neigbour to add you as your neighbour
			if (neighbour != null)
				me.makeNeighbour(me, neighbour);

			System.out.println("Make Neighbour : " + neighbour);
			me.neighborlist.put(neighbour, me.getHostName(neighbour));
			System.out.println(me.neighborlist);

		}
		boolean asking = true;
		while (asking) {
			System.out.println("Joined the System, Enter the operation?" + "\n1.Search" + "\n2.Terminate");
			String a = sc.nextLine();
			switch (a) {
			case "1":
				//// search function
				/////PENDING CLEAR OUT EVERYTHING
				search(me);
				break;
			case "2":
				int nodes = 0;
				Reader r = new Reader("active.txt");
				
					while (r.readFile.readLine() != null) {
						nodes++;
					}
					r.readFile.close();
					
					System.out.println("Number of lines:" +nodes);
					
					if (nodes==me.noOfNodes){
						// terminate function
						me.setTerminate(true);
						
						//check if all nodes up, then  only allow closing
						postTerminate(me);
						asking = false;
						System.out.println("Terminating");

					}
					else{
						System.out.println("Cannot terminate, only " +nodes + " up");
					}
				
				

				break;
			default:
				break;
			}
		}
		
		System.out.println("GoodBye!!");
		System.exit(0);
	}

	private static void postTerminate(Node me) throws IOException {
		System.out.println("in post" + me.neighborlist + " size " + me.neighborlist.size());
		if (me.neighborlist.size() == 1) {
		
			
		} else {
			int number_of_neighbors = me.neighborlist.size();
			Random r = new Random();
			int node_id = r.nextInt(number_of_neighbors);
			String new_node_id = node_id + "";
			HashMap<String, String> updated_neighbors = me.neighborlist;
			updated_neighbors.remove(new_node_id);
			NewReplyProtocol mp = new NewReplyProtocol(updated_neighbors, new NodeDef(me.id, me.host));
			Protocol p = new Protocol("tr", mp);
			new writingSocketThread(me, new_node_id, p).start();
			// me.neighborlist.clear();

		}

	}

	public static void search(Node me) throws UnknownHostException, IOException {
		boolean asking = true;
		while (asking) {
			System.out.println("Enter the mode which you want to do: 1.keyword 2. filename 3.close");
			System.out.println(me.printList(me.mylist));
			String mode = sc.nextLine();
			switch (mode) {
			case "1":
				// search function by keyword
				System.out.println("Enter the keyword to search from a-p");
				String keyword = sc.nextLine();
				if (me.mylist.get(keyword) != null) {
					System.out.println("File already in the system");
				
				} else {
					System.out.println("In the request method for keyword");
					me.replies = me.search_request(keyword, me);
					System.out.println("Replies recd: " + me.replies);
					if(me.replies!=null){
						System.out.println("Following Machines have the files : ");
						for (String string : me.replies.split("#")) {
							System.out.println(string);
						}
						System.out.println("Enter id from where to get the file");
						String inp = sc.nextLine();
	
						Protocol p = new Protocol("gfr", new GetFileProtocol(inp, me.id, keyword, null));
						//
						new writingSocketThread(me, inp, p).start();
						System.out.println("requesting file from " + inp);
					}
					System.out.println("ending sr, list:"+me.printList(me.mylist));
				
				}

				//clear replies for new search
				me.replies="";
				break;

			case "2":
				// search function by filename
				System.out.println("Enter the filename to search from name a-p .txt");
				String filenames = sc.nextLine();
				String fileKey = filenames.split("\\.")[0];
				if (me.mylist.get(fileKey) != null) {
					System.out.println("File already in the system");
					break;
				} else {

					System.out.println("in the method for filename ");
					// PENDING request method
					me.replies = me.search_request(fileKey, me);
					if(me.replies!=null){
						System.out.println("Following Machines(id) have the files : ");
						for (String string : me.replies.split("#")) {
							System.out.println(string);
						}
						System.out.println("Enter id from where to get the file");
						String inp = sc.nextLine();
	
						Protocol p = new Protocol("gfr", new GetFileProtocol(inp, me.id, fileKey, null));
						//
						new writingSocketThread(me, inp, p).start();
					}
					//clear replies for new search
					me.replies="";
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

	public String getHostName(String neighbor) {

		int id = Integer.parseInt(neighbor);
		if (id < 10) {
			return "dc0" + id + ".utdallas.edu";
		} else {
			return "dc" + id + ".utdallas.edu";
		}
	}

	private void randomGenFiles() throws IOException {
		String characters = "abcdefghijklmnop";
		String initialList = "";
		for (int i = 0; i < 3; i++) {
			char filename = characters.charAt(new Random().nextInt(characters.length()));
			initialList += filename + "\t" + filename + ".txt" + "\n";
			
			mylist.put(filename + "", filename + ".txt");
			new Writer(id + "/" + filename + ".txt").write("dwbdkcw");
		}
		new Writer(id + "/" + "fileList" + ".txt").write(initialList);
	}

	public void makeNeighbour(Node n, String id) {

		// tell neighbour to add me in neighbourlist
		// New changes
		Protocol p = new Protocol("mn", n.id);
		new writingSocketThread(n, id, p).start();
	}
	
	
	
	// U-REQUEST
	public String search_request(String keyword, Node n) {
		int hopcount;
		boolean file_received = false;
		for (hopcount = 1; hopcount <= 16 && !file_received; hopcount = hopcount * 2) {
			int time = 2000 * hopcount;

			// broadcast search request to all neighbours
			for (String key : n.neighborlist.keySet()) {

				SendRequestProtocol p = new SendRequestProtocol(new NodeDef(n.id, n.host), new NodeDef(n.id, n.host),
						keyword, hopcount, "");
				Protocol mp = new Protocol("sr", p);
				new writingSocketThread(n, key, mp).start();

			}
			// File request ORIGINATOR listens for replies

			ObjectInputStream iis;
			try {

				n.s = new ServerSocket(n.replyPort);
				s.setSoTimeout(time);

				while (true) {
					Socket socket = s.accept();

					Protocol a;
					iis = new ObjectInputStream(socket.getInputStream());
					a = (Protocol) iis.readObject();
					if (a.type.contentEquals("rp")) {
						
						NewReplyProtocol rp = (NewReplyProtocol) a.o;
						System.out.println("recieved following nrp on 9002:" + rp);
						n.replies += rp.fslist + rp.sep;
						System.out.println("fslist:" + rp.fslist);

					}

				}

			} catch (SocketTimeoutException e) {
				//
				System.out.println("Timer over");

				try {
					System.out.println("Closing Socket with hopcount " + hopcount);
					n.s.close();
				} catch (IOException e1) {

					e1.printStackTrace();
				}

			} catch (IOException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}

			System.out.println("In hopcount "+ hopcount+"replies:"+n.replies);
			if (!n.replies.isEmpty()) {
				// filoe present on some machine, so stop looping on hopcount
				file_received = true;

			}

		}
		return n.replies;

	}

	// public int get_timer(int value){
	// return value;
	// }

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

			listener = new ServerSocket(nodeObj.port); /// listens on 9000
			while (!nodeObj.getTerminate()) {
				System.out.println("In listener");
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

// Node listener listening for requests on port 9000
class ListenerService extends Thread {
	Socket servSocket;
	Node n;
	BufferedReader is;
	static NodeDef orig_ip, inter_ip;
	static ServerSocket s;
	static String replies = "";

	public ListenerService(Socket csocket, Node n) {
		this.servSocket = csocket;
		this.n = n;
		is = null;
	}
	
	
	//I-REQUEST
	public static void on_receive(Node n, SendRequestProtocol p) throws UnknownHostException, IOException {
		orig_ip = p.originator_ip;
		inter_ip = p.intermediate_ip;
		File f = new File(n.id + "/" + p.kwd + ".txt");

		// check if the file exists locally
		if (f.exists()) {
			System.out.println("found file locally ");
			String file_src = n.id;
			NewReplyProtocol nrp = new NewReplyProtocol(file_src, p.originator_ip);
			System.out.println(nrp);
			Protocol pr = new Protocol("rp", nrp);

			//reply on 9002
			Socket dstSocket = new Socket(n.getHostName(p.intermediate_ip.id), n.replyPort);
			ObjectOutputStream oos = new ObjectOutputStream(dstSocket.getOutputStream());
			oos.writeObject(pr);
			dstSocket.close();
			
			//file exists locally, do not do any forwarding of replies
			return;
		}

		int new_hopcount = p.hc;
		new_hopcount--;
		if (new_hopcount > 0) {
			// finding all the neighbors in neighborlist for broadcast
			for (String key : n.neighborlist.keySet()) {
				if (key.contentEquals(p.intermediate_ip.id))
					continue;
				SendRequestProtocol r = new SendRequestProtocol(p.originator_ip, new NodeDef(n.id, n.host), p.kwd,
						new_hopcount, "");
				Protocol mp = new Protocol("sr", r);
				new writingSocketThread(n, key, mp).start();

			}
			int time = 1000 * new_hopcount;
			ObjectInputStream iis;

			try {
				s = new ServerSocket(n.replyPort);
				s.setSoTimeout(time);

				while (true) {
					Socket socket = s.accept();

					Protocol a;
					iis = new ObjectInputStream(socket.getInputStream());
					a = (Protocol) iis.readObject();
					if (a.type.contentEquals("rp")) {
						NewReplyProtocol rp = (NewReplyProtocol) a.o;
						replies += rp.fslist + rp.sep;
//						System.out.println("The replies are:" +replies);
					}

				}

			} catch (SocketTimeoutException e) {
				//
				System.out.println("Timer over");

				try {
					System.out.println("Closing Socket with hopcount " + new_hopcount);

					// forward
					NewReplyProtocol sendReply = new NewReplyProtocol(replies, orig_ip);
					Protocol wp = new Protocol("rp", sendReply);
					Socket dstSocket = new Socket(n.getHostName(inter_ip.id), n.replyPort);
					ObjectOutputStream oos = new ObjectOutputStream(dstSocket.getOutputStream());
					oos.writeObject(wp);
					dstSocket.close();
					s.close();
				} catch (IOException e1) {

					e1.printStackTrace();
				}

			} catch (IOException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}

		}

	}

	@SuppressWarnings("unused")
	public void run() {
		try {

			ObjectInputStream iis = new ObjectInputStream(servSocket.getInputStream());

			while (!n.getTerminate()) {

				Protocol msg = (Protocol) iis.readObject();
				System.out.println("msg type recd : " + msg.type);
				if (msg == null)
					break;

				//// "mn" recieved a request from other node to add IT as
				//// neighbour
				if (msg.type.startsWith("mn")) {
					System.out.println("In make neighbour");
					String neigh_id = (String) msg.o;
					
					// if not a neighbour already
					if (!n.neighborlist.containsKey(neigh_id))
						n.neighborlist.put(neigh_id, n.getHostName(neigh_id));
					System.out.println(n.neighborlist);
					
				}

				//// "sr" file search request

				if (msg.type.startsWith("sr")) {
					//// HANDLE SEARCH REQUESTS
					SendRequestProtocol sr = (SendRequestProtocol) msg.o;
					System.out.println("keyword to search : " + sr.kwd);
					on_receive(n, sr);
				}

				//// "gfr" Get File

				if (msg.type.startsWith("gfr")) {
					GetFileProtocol gfr = (GetFileProtocol) msg.o;
					if (gfr.f == null) { // file param is null, that means,
											// recieved empty bucket to send the
											// file
						File f = new File(n.id + "/" + gfr.kwd + ".txt");
						if (f.exists())
							gfr.f = f;
						Protocol p = new Protocol("gfr", gfr);
						new writingSocketThread(n, gfr.originator_id, p).start();
						;
					} else {
						// file not null means recieved the acxtual file add to
						// directory and file list
						new Writer(n.id + "/" + gfr.kwd + ".txt").write("dwbdkcw");
						n.put(gfr.kwd, gfr.kwd + ".txt");
						System.out.println("in gfr listener, list="+n.printList(n.mylist) );
						new Writer(n.id + "/" + "fileList" + ".txt").write(gfr.kwd + "\t" + gfr.kwd + ".txt");
						System.out.println("Recieved file : "+gfr.kwd + ".txt");
					}
				}

				//// "tr" post terminate make neighbour requests
				if (msg.type.startsWith("tr")) {

					NewReplyProtocol trm = (NewReplyProtocol) msg.o;
					HashMap<String, String> correct_list = trm.neighborlist;
					for (String node : correct_list.keySet()) {
						n.neighborlist.put(node, n.getHostName(node));
						n.makeNeighbour(n, node);
					}

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

///////////////////////////////////////////////////////////////////

class writingSocketThread extends Thread {
	Node n;
	String dstId;
	Protocol p;

	public writingSocketThread(Node n, String dstId, Protocol p) {

		this.n = n;
		this.dstId = dstId;
		this.p = p;
	}

	public void run() {
		try {

			Socket dstSocket = new Socket(n.getHostName(dstId), n.port);
			ObjectOutputStream oos = new ObjectOutputStream(dstSocket.getOutputStream());
			oos.writeObject(p);
			dstSocket.close();

		} catch (UnknownHostException e) {

		} catch (IOException e) {

		}
	}

}
