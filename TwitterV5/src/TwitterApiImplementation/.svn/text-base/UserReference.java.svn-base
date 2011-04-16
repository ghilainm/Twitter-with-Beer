package TwitterApiImplementation;

import DHT.DhtUtilsAPI;
import DHTExceptions.ValueNotFoundException;

public class UserReference implements comparableItem{
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserReference(String userName) {
		this.userName = userName;
	}
	
	public User getItem(DhtUtilsAPI dhtHandler) throws ValueNotFoundException {
		String str = dhtHandler.getDht().read(userName);
		User result = (User) Translator.StringToObject(str);
		return result;
	}


	public int compareTo(Object arg0) {
		return ((UserReference)arg0).userName.compareTo(userName);
	}

	@Override
	public boolean equals(Object t) {
		if(t instanceof UserReference){
			UserReference tt = (UserReference)t;
			return tt.userName.compareTo(userName)==0;
		}
		else 
			return false;
	}
	
	public boolean equals(UserReference t) {
		return userName.compareTo(t.userName)==0;
	}
	
	public String toString(){
		return Translator.ObjectToString(this);
	}
	
	public int hashCode(){
		return userName.hashCode();
	}
}
