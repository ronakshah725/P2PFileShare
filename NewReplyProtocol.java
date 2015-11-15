import java.io.Serializable;
import java.util.HashMap;


@SuppressWarnings("serial")
public class NewReplyProtocol implements Serializable {
    
    
    String fslist;
    HashMap<String, String> neighborlist;
    NodeDef originator_ip;
    String sep = "#";
    
    public NewReplyProtocol(String fslist, NodeDef originator_ip) {
        this.fslist = fslist;
        this.originator_ip = originator_ip;
    }
    public NewReplyProtocol(HashMap<String, String> neighborlist, NodeDef originator_ip) {
        // TODO Auto-generated constructor stub
    this.neighborlist = neighborlist;
    this.originator_ip= originator_ip;
        
    }
}