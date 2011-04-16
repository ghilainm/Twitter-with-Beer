package TwitterApiImplementation;

import java.util.HashMap;
import java.util.LinkedList;

import DHT.DhtUtilsAPI;
import DHTExceptions.ValueNotFoundException;

public class ForeignSet {

	protected DhtUtilsAPI dhtHandler;
	protected HashMap<String, Line> foreignLineStore;
	protected HashMap<String, User> userStore;



	public ForeignSet(DhtUtilsAPI dhtHandler) throws ValueNotFoundException{
		this.dhtHandler = dhtHandler;
		this.foreignLineStore = new HashMap<String, Line>();
		this.userStore = new HashMap<String, User>();
	}


	public User getUser(String userName) throws ValueNotFoundException{
		if(!userStore.containsKey(userName)){
			String str = dhtHandler.getDht().read(userName);
			userStore.put(userName, (User) Translator.StringToObject(str));	
		}
		return userStore.get(userName);
	}

	public Line getLine(String lineName, String userName) throws ValueNotFoundException{
		if(!foreignLineStore.containsKey(userName+"#"+lineName)){
			User guy = getUser(userName);
			LinkedList<String> entries = dhtHandler.getDht().readSet(guy.getLineSetID());
			boolean found = false;
			for(String entry :entries ){
				LineSetReference lse = (LineSetReference) Translator.StringToObject(entry);
				if (lse.getLineName().equals(lineName)){
					foreignLineStore.put(userName+"#"+lineName, new Line(lineName, lse.getUserSetID(), lse.getTweetSetID(), dhtHandler));
					found = true;
					break;
				}
			}
			if (!found){
				throw new ValueNotFoundException(lineName + " of " +userName);
			}
		}
		return foreignLineStore.get(userName+"#"+lineName);		
	}


	public LinkedList<UserReference> getLineFollowing(String lineName, String userName) throws ValueNotFoundException{
		if(!foreignLineStore.containsKey(userName+"#"+lineName)){
			getLine(lineName,userName);}
		return foreignLineStore.get(userName+"#"+lineName).getAllUserReferences();
	}

	
	

}
