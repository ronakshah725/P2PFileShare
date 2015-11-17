


import java.io.Serializable;


@SuppressWarnings("serial")
public class NodeDef implements Serializable {
 String id;
	String host;

	public NodeDef(String nodeId, String host) {

		this.id = nodeId;
		this.host = host;
		
	}
	
	public String toString() {

		return id + "sp" + host;
	}
}
