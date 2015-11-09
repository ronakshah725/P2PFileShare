
import java.io.Serializable;


@SuppressWarnings("serial")
public class SendRequestProtocol implements Serializable {

	int hc;
	String kwd;
	String type;
	NodeDef originator_ip;
	NodeDef intermediate_ip;

	
	public SendRequestProtocol(NodeDef originator_ip, NodeDef intermediate_ip,String keyword,int hpcnt, String type) {
		this.originator_ip = originator_ip;
		this.intermediate_ip =intermediate_ip;
		this.kwd=keyword;
		this.hc = hpcnt;
		this.type = type;
	}
/*
public String toString(){
	
	return "ID:"+ id+", " +"TimeStamp:" + ts+ ", " +"Type:"+ type + "\n" ;
//	return "ID:"+ id+", " +"TimeStamp:" + ts+ ", " +"Type:"+ type + "\n"  ;
}
*/	

}




