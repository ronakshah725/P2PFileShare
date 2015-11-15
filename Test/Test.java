package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Test {
  public static String replies="";
	public static void main(String args[]){
		
		try {
			String fd;
			System.out.println("Starting server");
			ServerSocket s = new ServerSocket(8000);
			s.setSoTimeout(10000);
			while(true){
			Socket ar = s.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(ar.getInputStream()));
			if((fd = in.readLine())!=null){
				replies += fd + "#";
			}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("timeout");
			String a[] = replies.split("#");
			for (int i = 0; i<a.length; i++)
				System.out.println(a[i]);
		}
	}
}
