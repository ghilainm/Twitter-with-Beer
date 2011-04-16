package TwitterApiImplementation;

public class SetHeader{
	public String getNextID() {
		return nextID;
	}
	public void setNextID(String nextID) {
		this.nextID = nextID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	String nextID; 
	String type;
	String name;
	SetHeader(String nextID, String type, String name){
		this.nextID = nextID;
		this.type = type;
		this.name = name;	
	}

}
