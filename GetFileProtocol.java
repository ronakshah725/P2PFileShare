import java.io.File;
import java.io.Serializable;

@SuppressWarnings("serial")
public class GetFileProtocol implements Serializable {
	String FS_id;
	String originator_id;
	
	String kwd;
	File f;
	public GetFileProtocol(String fS_IP,String originator_id,  String kwd, File f) {
		this.FS_id = fS_IP;
		this.kwd = kwd;
		this.originator_id = originator_id;
		this.f=f;
	}
	
	
	
	
	

}
