package Base;


import java.io.Serializable;


@SuppressWarnings("serial")
public class Protocol implements Serializable {

	long ts;
	int id;
	int [][] matrix = new int[10][10];
	String type;
	
	public Protocol(long ts, int id, int[][] matrix, String type) {
		this.ts = ts;
		this.id = id;
		this.matrix = matrix;
		this.type = type;
	}

public String toString(){
	
	return "ID:"+ id+", " +"TimeStamp:" + ts+ ", " +"Type:"+ type + "\n" + getPrintableMat(matrix) ;
//	return "ID:"+ id+", " +"TimeStamp:" + ts+ ", " +"Type:"+ type + "\n"  ;

}

public static String getPrintableMat(int[][] mat) {
	int c=0;
	String display= "M"+" = ";
	int temp=display.length();
	//display+="";
	for(int i[]:mat)
		for(int j:i)
		{
			display+=j+"\t";
			
			if(++c%10==0){
				display+="\n";
				for (int j2 = 0; j2 < temp; j2++) {
					display+=" ";
				}
				display+="";
			}
	
		}
	display=display.substring(0, display.length()-2);
return 	display;
}

//public static void main(String[] args) {
//	int [][] m = new int[10][10];
//	
//	for(int i = 0; i<10; i++){
//		for (int j = 0; j<10; j++ ){
//			m[i][j] = new Random().nextInt(10);
//		}
//	}
//	System.out.println(getPrintableMat(m));
//	Protocol p = new Protocol(System.currentTimeMillis(), 15, m, "wd dedew  ef e");
//	System.out.println(p);
//}
//	
	
	
	
	

}


