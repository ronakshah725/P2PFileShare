package Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;

public class Client {
  
	public static void main(String args[]){
		
		HashSet<String> a =  new HashSet<>();
		a.add("a");
		a.add("b");
		a.add("c");
		
		HashSet<String> b=  new HashSet<>();
		b.add("d");
		b.add("c");
		
		b.addAll(a);
		System.out.println(b);
		
		for (int j2 = 0; j2 < 3; j2++) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Socket s;
					try {
						System.out.println("Running thread ");
						s = new Socket(InetAddress.getLocalHost(),8000);
						PrintWriter p = new PrintWriter(s.getOutputStream(), true);
					    p.println("value");  
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block

					}
					
			}
			}).start();
			
		}

	}
}
