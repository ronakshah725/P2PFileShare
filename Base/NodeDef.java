package Base;



public class NodeDef {
	int id;
	String host;
	int port;
	public NodeDef(int nodeId, String host, int port) {

		this.id = nodeId;
		this.host = host;
		this.port = port;
	}
	
	public String toString() {

		return id + "sp" + host + "sp" + port;
	}
}
