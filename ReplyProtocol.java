import java.io.Serializable;

@SuppressWarnings("serial")
public class ReplyProtocol implements Serializable {
	
	
	NodeDef filesource_IP;
	NodeDef originator_ip;
	
	public ReplyProtocol(NodeDef filesource_IP, NodeDef originator_ip) {
		this.filesource_IP = filesource_IP;
		this.originator_ip = originator_ip;
	}
}
