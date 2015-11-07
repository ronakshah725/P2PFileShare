import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class Node {
	static String id;
	String host;
	final int noOfNodes = 10;
	int port;
	int basePort = 9000;
	
	Node(String i) {

		id = i;
		try {
			this.host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		this.port = this.basePort;


	}
	
	public String toString() {

		return id + "@" + host + ":" + port;
	}
	
	static int getrandom(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		
		Node me = new Node (args[0]);
		System.out.println(me);
		new Writer("active.txt").write(id);
		me.randomGenFiles();
		
		
		
		
		System.out.println("Enter the operation?" + "\n1.Search" + "\n2.Terminate");
		Scanner sc = new Scanner (System.in);
		
		
		
		

	}

	private void randomGenFiles() throws IOException {
		String characters = "abcdefghijklmnop";
		String initialList = "";
		for (int i = 0; i <3; i++) {
		
		char filename = characters.charAt(new Random().nextInt(characters.length()));
		initialList += filename  +"\t"+ filename+ ".txt" +  "\n";
			
			
		new Writer(id+"/"+filename+".txt").write("dwbdkcw");
	
			
			
		}
		new Writer(id+"/"+"fileList"+".txt").write(initialList);
	}

}
