package Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
  
	public static void main(String args[]){
		

		int i =1;
		int j=2;
		for (int j2 = 0; j2 < 3; j2++) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Socket s;
					try {
						System.out.println("Running thread ");
						s = new Socket(InetAddress.getLocalHost(),9000);
						PrintWriter p = new PrintWriter(s.getOutputStream(), true);
					    p.println(i+ "value");  
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
