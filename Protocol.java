
import java.io.Serializable;


@SuppressWarnings("serial")
public class Protocol implements Serializable {

	long ts;
	int hopcount;
	String filename;
	String type;
	NodeDef src;
	
	public Protocol(NodeDef nd,String filename,long ts,int hpcnt, String type) {
		this.src = nd;
		this.filename=filename;
		this.ts = ts;
		this.hopcount = hpcnt;
		this.type = type;
	}
/*
public String toString(){
	
	return "ID:"+ id+", " +"TimeStamp:" + ts+ ", " +"Type:"+ type + "\n" ;
//	return "ID:"+ id+", " +"TimeStamp:" + ts+ ", " +"Type:"+ type + "\n"  ;
}
*/	

}




