package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Test {
  
	public static void main(String args[]){
		
		try {
			String fd;
			System.out.println("Starting server");
			ServerSocket s = new ServerSocket(9000);
			s.setSoTimeout(10000);
			while(true){
			Socket ar = s.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(ar.getInputStream()));
			while((fd = in.readLine())!=null){
				System.out.println(fd);
				System.out.println("looping");
			}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("timeout");
		}
	}
}
