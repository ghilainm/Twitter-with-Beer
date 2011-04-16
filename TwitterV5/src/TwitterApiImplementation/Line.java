package TwitterApiImplementation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import DHT.DhtUtilsAPI;
import DHTExceptions.BadSecretException;
import DHTExceptions.ValueNotFoundException;

public class Line {
	private String lineName;
	private String userNameSetID;
	private String tweetSetID;
	private DhtUtilsAPI dhtHandler;
	private TweetFragmentedSet tweetRefs;
	private UserFragmentedSet userRefs;
	protected static final int CHUNKSIZE = Constants.getChunksize();

	public Line(String lineName, String userNameSetID, String tweetSetID, DhtUtilsAPI dhtHandler) throws ValueNotFoundException{
		this.lineName = lineName;
		this.userNameSetID = userNameSetID;
		this.tweetSetID = tweetSetID;
		this.dhtHandler = dhtHandler;
		this.userRefs = new UserFragmentedSet(userNameSetID, dhtHandler);
		this.tweetRefs = new TweetFragmentedSet(tweetSetID, dhtHandler);
	}

	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public String getTweetSetID() {
		return tweetSetID;
	}
	public void setTweetSetID(String tweetSetID) {
		this.tweetSetID = tweetSetID;
	}
	public String getUserNameSetID() {
		return userNameSetID;
	}
	public void setUserNameSetID(String userNameSetID) {
		this.userNameSetID = userNameSetID;
	}

	@Override
	public String toString(){
		return lineName +" "+ userNameSetID +" "+ tweetSetID;
	}


	public LinkedList<Tweet> moreTweets() throws ValueNotFoundException{
		return filter(tweetRefs.more());
	}	


	public LinkedList<Tweet> refreshTweets() throws ValueNotFoundException{
		return filter(tweetRefs.refresh());
	}


	public LinkedList<User> moreUsers() throws ValueNotFoundException{
		LinkedList<User> result = new LinkedList<User>();
		LinkedList<UserReference> followingSet = userRefs.more();
		for(UserReference ur : followingSet){
			result.add(ur.getItem(dhtHandler));
		}
		return result;
	}	

	public LinkedList<Tweet> allTweets() throws ValueNotFoundException{
		return filter(tweetRefs.all());
	}

	public LinkedList<UserReference> getAllUserReferences() throws ValueNotFoundException{	
		return userRefs.all();
	}	

	public LinkedList<UserReference> getMoreUserReferences() throws ValueNotFoundException{
		return userRefs.more();
	}

	//This function return true if the user was already in the line
	public boolean addUserToLine(String userName) throws ValueNotFoundException, BadSecretException {
		UserReference userRef = new UserReference(userName);
		if(userRefs.getAll().contains(userRef))return true;
		userRefs.add(userRef);
		return false;	
	}	

	//This function return false if the user was not in the line
	public boolean removeUserFromLine(String userName) throws ValueNotFoundException, BadSecretException {
		UserReference userRef = new UserReference(userName);
		if(!userRefs.getAll().contains(userRef))
			return false;
		userRefs.remove(userRef);	
		return true;	
	}	

	//This method take a set of tweetids reference and return
	//a set of tweet, all the tweet referenced in tweetids 
	//will be return in the tweet list except the tweet deleted and
	//the tweet that were posted by a user that is not followed by this line
	public LinkedList<Tweet> filter(LinkedList<TweetReference> tweetids) throws ValueNotFoundException{
		LinkedList<Tweet> result = new LinkedList<Tweet>();
		Set<UserReference> followingSet = userRefs.getAll();
		HashSet<String> followingNamesSet = new HashSet<String>(followingSet.size());
		for(UserReference ur : followingSet){
			followingNamesSet.add(ur.getUserName());
		}

		// Filtering
		Tweet cur;
		for(TweetReference tweetid : tweetids){
			if (followingNamesSet.contains(tweetid.getPosterName())){
				try {
					cur = tweetid.getItem(dhtHandler);
					if(!cur.isDeleted()){
						//Change the field of the tweet to isRetweet if the tweet was a retweet
						cur.setReTweet(tweetid.isReTweet());
						//Change the name of the reTweeterName if the tweet is a retweet
						if(tweetid.isReTweet())
							cur.setReTweeterName(tweetid.getPosterName());
						cur.setTweetID(tweetid.getReference());
						result.addLast(cur);
					}
				} catch (ValueNotFoundException e) {
					System.out.println("problem when retrieving tweet");
					e.printStackTrace();
				}

			}	
		}
		return result;
	}
}
