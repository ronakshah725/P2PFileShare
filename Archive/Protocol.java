import java.io.Serializable;

@SuppressWarnings("serial")
public class Protocol implements Serializable
{
	String type;
	Object o;
	
	public Protocol(String type, Object o) {
		this.type = type;
		this.o = o;
	}

}
