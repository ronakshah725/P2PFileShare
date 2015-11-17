import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Reader {
	BufferedReader readFile;
	
	public Reader(String str) {
		  try {
			readFile = new BufferedReader(new FileReader(str));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(" File not found, in Reader class");
		}

	}
	

	
	


}
