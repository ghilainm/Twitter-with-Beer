package TwitterApiImplementation;

import java.util.HashMap;
import java.util.LinkedList;

import TwitterAPIExceptions.ThisLineAlreadyExistException;
import TwitterAPIExceptions.ThisListDoesNotExistsException;

import DHT.DhtUtilsAPI;
import DHTExceptions.BadSecretException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;

public class LineSet {

	protected String LineSetID;
	protected DhtUtilsAPI dhtHandler;
	protected HashMap<String, Line> lineStore;
	protected HashMap<String, UserFragmentedSet> FollowerSetStore;

	public LineSet(String LineSetID, DhtUtilsAPI dhtHandler) throws ValueNotFoundException{
		this.LineSetID = LineSetID;
		this.dhtHandler = dhtHandler;
		this.lineStore = new HashMap<String, Line>();
		initLineStore();
		FollowerSetStore = new HashMap<String, UserFragmentedSet>();
	}

	private void initLineStore() throws ValueNotFoundException{
		for(LineSetReference e : getLineReferences())
			lineStore.put(e.getLineName(), new Line(e.getLineName(), e.getUserSetID(), e.getTweetSetID(), dhtHandler));
	}

	public LinkedList<UserReference> getAllFollowing() throws ValueNotFoundException{
		LinkedList<UserReference> result = new LinkedList<UserReference>();
		for(Line l : lineStore.values()){
			result.addAll(l.getAllUserReferences());
		}
		return result;
	}


	public LinkedList<UserReference> getLineFollowing(String LineName) throws ValueNotFoundException{
		if(!lineStore.containsKey(LineName))
			throw new ValueNotFoundException("This line does not exists "+LineName);
		return lineStore.get(LineName).getAllUserReferences();
	}

	public LinkedList<LineSetReference> getLineReferences(){

		LinkedList<LineSetReference> result = new LinkedList<LineSetReference>();

		LinkedList<String> entries;
		try {
			entries = dhtHandler.getDht().readSet(LineSetID);
		} catch (ValueNotFoundException e) {
			System.out.println("Can't get LineSet");
			e.printStackTrace();
			return result;
		}		

		for(String entry :entries ){
			LineSetReference lse = (LineSetReference) Translator.StringToObject(entry);
			result.add(lse);
		}

		return result;
	}

	public LineSetReference match(LinkedList<LineSetReference> lseList, String LineName){
		for(LineSetReference lse: lseList){
			if(lse.getLineName().equals(LineName)){
				return lse;
			}
		}
		return null;
	}

	public void addFollowing(String lineName, User newFollowing, String myUserName) throws SecretProtectedException, ValueNotFoundException, BadSecretException{
		if(!lineStore.containsKey(lineName))
			throw new ValueNotFoundException("This line does not exists "+lineName);

		// Add new user to the line
		//Test if the user is already in the line
		if(lineStore.get(lineName).addUserToLine(newFollowing.getUserName()))
			return;

		// tell him in which line to post the tweets
		dhtHandler.addSecure(newFollowing.gettoPostSetID(), lineStore.get(lineName).getTweetSetID());

		// register in the follower set of the user if not already done
		UserFragmentedSet followerset = getFollowerLine(newFollowing.getFollowerSetID());	
		LinkedList<UserReference> followerSet = followerset.all();

		for(UserReference ref  : followerSet){
			if(ref.getUserName().equals(myUserName)){
				return;
			}
		}

		UserReference myRef = new UserReference(myUserName);
		followerset.add(myRef);
	}
	
	private UserFragmentedSet getFollowerLine(String SetID) throws ValueNotFoundException{
		if (!FollowerSetStore.containsKey(SetID)){
			FollowerSetStore.put(SetID, new UserFragmentedSet(SetID, dhtHandler));	
		}
		return FollowerSetStore.get(SetID);
	}


	public void removeFollowing(String lineName, User oldFollowing, String myUserName) throws SecretProtectedException, ValueNotFoundException, BadSecretException{
		UserReference oldFollowingRef = new UserReference(oldFollowing.getUserName());
		if(!lineStore.containsKey(lineName))
			throw new ValueNotFoundException("This line does not exists "+lineName);

		//Remove the user from the line
		if(!lineStore.get(lineName).removeUserFromLine(oldFollowing.getUserName()))
			return;

		// tell him in which line to post the tweets
		dhtHandler.removeFromSetSecure(oldFollowing.gettoPostSetID(), lineStore.get(lineName).getTweetSetID());

		// remove the user from the follower set of newFollowing if he is not following him anymore on any line
		UserReference myRef = new UserReference(myUserName);
		if(!getAllFollowing().contains(oldFollowingRef)){
			getFollowerLine(oldFollowing.getFollowerSetID()).remove(myRef);	
			
		}
	}



	public void addLine(String newLineName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, ThisLineAlreadyExistException{
		if(lineStore.containsKey(newLineName))
			throw new ThisLineAlreadyExistException();
		SubFragmentedSet<UserReference> users = new SubFragmentedSet<UserReference>();
		String newUserSetID =  dhtHandler.createSet();
		SetHeader newUserSetHeader = new SetHeader(null, Constants.getUserType(), newLineName);
		users.postHeader(newUserSetID, dhtHandler, newUserSetHeader);

		SubFragmentedSet<TweetReference> tweets = new SubFragmentedSet<TweetReference>();
		String newTweetSetID =  dhtHandler.createSet();
		SetHeader newTweetSetHeader = new SetHeader(null, Constants.getTweetType(), newLineName);
		tweets.postHeader(newTweetSetID, dhtHandler, newTweetSetHeader);

		lineStore.put(newLineName, new Line(newLineName, newUserSetID, newTweetSetID, dhtHandler));
		LineSetReference lse = new LineSetReference(newLineName,newUserSetID, newTweetSetID);
		String newStr = Translator.ObjectToString(lse);
		dhtHandler.addSecure(LineSetID,newStr);

	}


	public void removeLine(String lineName, String myUserName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, ThisListDoesNotExistsException{
		Line toremove = lineStore.get(lineName);
		if(toremove==null)
			throw new ThisListDoesNotExistsException("You can remove a line that does not exist "+lineName);
		String str;
		User guy;

		// Unsubscribe the list from all the users it follows
		for(UserReference u : toremove.getAllUserReferences()){
			str = dhtHandler.getDht().read(u.getUserName());
			guy = (User) Translator.StringToObject(str);
			removeFollowing(lineName, guy, myUserName);
		}

		// Delete content from the line
		dhtHandler.deleteSet(toremove.getTweetSetID());	
		dhtHandler.deleteSet(toremove.getUserNameSetID());			
		
		// Remove reference in set
		LineSetReference lse = new LineSetReference(lineName,toremove.getUserNameSetID(), toremove.getTweetSetID());
		String newStr = Translator.ObjectToString(lse);
		dhtHandler.removeFromSetSecure(LineSetID,newStr);

		// Remove in local cache
		lineStore.remove(lineName);
	}




}
