import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;


@SuppressWarnings("serial")
public class NewReplyProtocol implements Serializable {
    
    
	HashSet<String> fslist;
    HashMap<String, String> neighborlist;
    NodeDef originator_ip;

    
    public NewReplyProtocol(HashSet<String> fslist, NodeDef originator_ip) {
        this.fslist = fslist;
        this.originator_ip = originator_ip;
    }
    
    
    
    public NewReplyProtocol(HashMap<String, String> neighborlist) {
    this.neighborlist = neighborlist;
      
    }
    

	@Override
    public String toString() {
    	return "NRP: FSLIST:" + fslist +", orig_ip: "+ originator_ip;
    }
}