package TwitterApiImplementation;

import java.util.LinkedList;
import java.util.Set;

import TwitterAPIExceptions.BadPasswordException;
import TwitterAPIExceptions.NotLoggedException;
import TwitterAPIExceptions.PassWordTooWeak;
import TwitterAPIExceptions.ThisLineAlreadyExistException;
import TwitterAPIExceptions.ThisListAlreadyExistException;
import TwitterAPIExceptions.ThisListDoesNotExistsException;
import TwitterAPIExceptions.UserAlreadyUsedException;
import TwitterAPIExceptions.UserNameInvalid;
import TwitterAPIExceptions.UserNotFoundException;



import DHT.DhtUtilsAPI;
import DHTExceptions.BadSecretException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;

public class Handler {


	protected UserFragmentedSet followerReferences;
	protected DhtUtilsAPI dhtHandler;
	protected User user;
	protected boolean logged;
	protected LineSet lineset;
	protected ListSet listset;
	protected ForeignSet foreignset;


	public Handler(DhtUtilsAPI dhtHandler){
		this.dhtHandler = dhtHandler;
	}	

	public void logIn(String UserName, String password) throws ValueNotFoundException, UserNotFoundException, BadPasswordException{
		try {
			user = getUser(UserName);
		} catch (ValueNotFoundException e) {
			throw new UserNotFoundException();
		}

		if(!SecurityUtils.verifyPassword(user.getpasswordHash(), password))
			throw new BadPasswordException();

		logged = true;
		dhtHandler.setUserSecret(SecurityUtils.getUserSecret(UserName, password));
		lineset = new LineSet(user.getLineSetID(), dhtHandler);
		listset = new ListSet(user.getListSetID(), dhtHandler);
		foreignset = new ForeignSet(dhtHandler);
		followerReferences = new UserFragmentedSet(user.getFollowerSetID(), dhtHandler);
	}

	public void isLogged() throws NotLoggedException{
		if(!logged)
			throw new NotLoggedException();
	}

	public LinkedList<User> getFollowerSet() throws ValueNotFoundException{
		followerReferences.refreshAll();
		LinkedList<User> result = new LinkedList<User>();
		String str;
		//TODO remove dead ? 
		for(UserReference id : followerReferences.getAll()){
			try {
				str = dhtHandler.getDht().read(id.getUserName());
				User guy = (User) Translator.StringToObject(str);
				result.add(guy);
			} catch (ValueNotFoundException e) {
				System.out.println(id.getUserName()+" was not found");
				e.printStackTrace();
			}
		}
		return result;
	}

	//This method return the tweetID of the new posted tweet.
	public String postTweet(String msg, String parentTweetID) throws ValueNotFoundException, NotLoggedException{
		isLogged();

		StandardDate date = new StandardDate();
		Tweet tweet = new Tweet(date, parentTweetID, dhtHandler.createSet(), msg, user.getUserName(), false, false);

		String tweetRef = null;
		try {
			tweetRef = dhtHandler.writeSecure(Translator.ObjectToString(tweet));
		} catch (BadSecretException e) {
			System.out.println("Write secure with a bad secret : This is improbable");
			e.printStackTrace();
		}

		TweetReference tweetReference = new TweetReference(tweetRef, date, user.getUserName(), false);

		postToFollowers(Translator.ObjectToString(tweetReference));
		return tweetRef;
	}

	public void addFollowing(String lineName,String newFollowingName) throws ValueNotFoundException, SecretProtectedException, BadSecretException{
		lineset.addFollowing(lineName, getUser(newFollowingName), user.getUserName());
	}

	public void createUser(String userName, String password, String realName) throws SecretProtectedException, UserAlreadyUsedException, ValueNotFoundException, BadSecretException, PassWordTooWeak, UserNameInvalid{
		SecurityUtils.isPassWordStrongEnough(password);
		SecurityUtils.verifyUsername(userName);
		SecurityUtils.verifyRealName(realName);
		
		dhtHandler.setUserSecret(SecurityUtils.getUserSecret(userName, password));
		try {
			boolean userNameFree = false;
			try{
				//Test wether user name already exist
				dhtHandler.getDht().read(userName);
			}
			catch(ValueNotFoundException e){
				userNameFree = true;
			}
			if(!userNameFree)
				throw new UserAlreadyUsedException();
			dhtHandler.writeSecure(userName, "");
		} catch (BadSecretException e) {
			throw  new UserAlreadyUsedException();
		}

		StandardDate date = new StandardDate();

		String lineSetID = createLineSet();
		String listSetID = createListSet();

		String followerSetID = dhtHandler.createSet();
		SubFragmentedSet<UserReference> followerSet = new SubFragmentedSet<UserReference>();
		SetHeader head = new SetHeader(null, Constants.getUserType(), Constants.getFollowersName());
		followerSet.postHeader(followerSetID, dhtHandler, head);

		String toPostSetID = dhtHandler.createSet();

		User newuser = new User(userName, date, lineSetID, listSetID, SecurityUtils.getSecurePassword(password), followerSetID, toPostSetID, realName);
		user = newuser;

		String userStr = Translator.ObjectToString(newuser);

		try {
			dhtHandler.writeSecure(userName, userStr);
		} catch (BadSecretException e) {
			e.printStackTrace();
		}
		lineset = new LineSet(user.getLineSetID(), dhtHandler);
		listset = new ListSet(user.getListSetID(), dhtHandler);
		foreignset = new ForeignSet(dhtHandler);
		addFollowing(Constants.getTimeLineName(),userName);
		addFollowing(Constants.getUserLineName(), userName);

	}

	public User getUser() {
		return user;
	}

	public User getUser(String UserName) throws ValueNotFoundException{
		String str = dhtHandler.getDht().read(UserName);
		return (User) Translator.StringToObject(str);
	}	

	/****************************************************************
	 * 					LINES										*
	 ****************************************************************/

	private String createLineSet() throws SecretProtectedException, ValueNotFoundException, BadSecretException{
		String lineSetID = dhtHandler.createSet();

		// Contains at least two lines by default: UserLine and TimeLine

		String userLine = Constants.getUserLineName();
		String timeLine = Constants.getTimeLineName();

		LineSet lineset = new LineSet(lineSetID, dhtHandler);

		try {
			lineset.addLine(userLine);
			lineset.addLine(timeLine);
		} catch (ThisLineAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return lineSetID;

	}

	public Set<String> getLineNames() throws NotLoggedException {
		//Throw an exception if not logged
		isLogged();
		return lineset.lineStore.keySet();
	}

	public LinkedList<Tweet> moreTweetsFromLine(String lineName, String userName) throws ValueNotFoundException, NotLoggedException{
		//Throw an exception if not logged
		isLogged();
		Line l = getLine(lineName,userName);
		if(l==null)
			throw new ValueNotFoundException(lineName);
		return l.moreTweets();
	}

	public LinkedList<Tweet> refreshTweetsFromLine(String lineName, String userName) throws ValueNotFoundException, NotLoggedException {
		//Throw an exception if not logged
		isLogged();
		Line l = getLine(lineName,userName);
		if(l==null)
			throw new ValueNotFoundException(lineName);
		return l.refreshTweets();
	}

	public LinkedList<User> moreUsers(String lineName, String userName) throws ValueNotFoundException, NotLoggedException {
		//Throw an exception if not logged
		isLogged();
		Line l = getLine(lineName,userName);
		if(l==null)
			throw new ValueNotFoundException(lineName);
		return l.moreUsers();
	}

	public Line getLine(String lineName, String userName) throws NotLoggedException, ValueNotFoundException{
		isLogged();
		if (user.getUserName().equals(userName)){
			return lineset.lineStore.get(lineName);}
		else{
			return foreignset.getLine(lineName, userName);
		}
	}
	public void removeFollowing(String lineName, String followingUserName) throws SecretProtectedException, ValueNotFoundException, BadSecretException, NotLoggedException {
		//Throw an exception if not logged or if the followingUserName is not a valid user
		isLogged();
		lineset.removeFollowing(lineName, getUser(followingUserName), user.getUserName());
	}
	public void createLine(String lineName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, ThisLineAlreadyExistException {
		//Throw an exception if not logged
		isLogged();
		lineset.addLine(lineName);
	}

	public void deleteLine(String lineName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, ThisListDoesNotExistsException {
		//Throw an exception if not logged
		isLogged();
		lineset.removeLine(lineName, user.getUserName());
	}

	public LinkedList<Tweet> allTweet(String lineName) throws NotLoggedException, ValueNotFoundException {
		//Throw an exception if not logged
		isLogged();
		Line l = getLine(lineName,user.getUserName());
		if(l==null)
			throw new ValueNotFoundException(lineName);
		return l.allTweets();
	}

	/****************************************************************
	 * 					LISTS										*
	 ****************************************************************/

	private String createListSet() throws SecretProtectedException, ValueNotFoundException, BadSecretException{
		String listSetID = dhtHandler.createSet();
		ListSet listset = new ListSet(listSetID, dhtHandler);

		// Contains at least one list by default: FavoriteList

		String favList = Constants.getFavListName();
		try {
			listset.addList(favList);
		} catch (ThisListAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listSetID;

	}


	public void createList(String listName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, ThisListAlreadyExistException {
		//Throw an exception if not logged
		isLogged();
		listset.addList(listName);
	}

	public void deleteList(String listName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, ThisListDoesNotExistsException {
		//Throw an exception if not logged
		isLogged();
		listset.removeList(listName);
	}

	public TweetFragmentedSet getList(String listName) throws NotLoggedException{
		//Throw an exception if not logged
		isLogged();
		return listset.listStore.get(listName);
	}

	public LinkedList<Tweet> moreTweetsFromList(String listName) throws ValueNotFoundException, NotLoggedException{
		//Throw an exception if not logged
		isLogged();
		TweetFragmentedSet l = getList(listName);
		if(l==null)
			throw new ValueNotFoundException(listName);
		return filter(l.more());
	}

	public Set<String> getListNames() throws NotLoggedException {
		//Throw an exception if not logged
		isLogged();
		return listset.listStore.keySet();
	}

	public void addTweetToList(TweetFragmentedSet list, String tweetID) throws ValueNotFoundException, BadSecretException, NotLoggedException {
		//Throw an exception if not logged
		isLogged();

		String str = dhtHandler.getDht().read(tweetID);
		Tweet tweet = (Tweet) Translator.StringToObject(str);

		TweetReference toadd = new TweetReference(tweetID, tweet.getPostingDate(), tweet.getPosterName(), tweet.isReTweet());
		list.add(toadd);
	}

	public void removeTweetFromList(TweetFragmentedSet list, String tweetID) throws ValueNotFoundException, BadSecretException, NotLoggedException {
		isLogged();

		String str = dhtHandler.getDht().read(tweetID);
		Tweet tweet = (Tweet) Translator.StringToObject(str);

		TweetReference toadd = new TweetReference(tweetID, tweet.getPostingDate(), tweet.getPosterName(), tweet.isReTweet());
		list.remove(toadd);
	}

	public LinkedList<Tweet> refreshTweetsFromList(String listName) throws ValueNotFoundException, NotLoggedException {
		//Throw an exception if not logged
		isLogged();
		TweetFragmentedSet l = getList(listName);
		if(l==null)
			throw new ValueNotFoundException(listName);
		return filter(l.refresh());
	}

	private LinkedList<Tweet> filter(LinkedList<TweetReference> tweetids) throws ValueNotFoundException{
		LinkedList<Tweet> result = new LinkedList<Tweet>();

		// Filtering
		Tweet cur;
		for(TweetReference tweetid : tweetids){
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
		return result;
	}

	/****************************************************************
	 * 					TWEETS										*
	 ****************************************************************/

	public void reTweet(String tweetID) throws ValueNotFoundException, NotLoggedException {
		//Throw an exception if not logged
		isLogged();

		//This will throw an exception if the tweet does not exists
		dhtHandler.getDht().read(tweetID);
		TweetReference tRef = new TweetReference(tweetID, new StandardDate(),user.getUserName(), true);
		postToFollowers(Translator.ObjectToString(tRef));
	}

	//This message post on each set referenced by followerReferences the message msg
	private void postToFollowers(String msg) throws ValueNotFoundException{
		//TODO peut etre qu'on doit transformer le topost set en un fragmented set comme �a �a nous permettrait
		//de pas devoir chaque fois relire tout le set dans le dht qui peut etre tr�s gros mais r�cup�rer
		//des petites parties
		LinkedList<String> entries = dhtHandler.getDht().readSet(user.gettoPostSetID());

		for(String line : entries){
			try {
				dhtHandler.addSecure(line, msg);
			} catch (BadSecretException e) {
				System.out.println("problem, couldn't add to the line");
				e.printStackTrace();
			}
		}
	}

	public void reply(String msg, String parentTweetID) throws ValueNotFoundException, BadSecretException, NotLoggedException {
		//Throw an exception if not logged
		isLogged();
		Tweet parentTweet = (Tweet) Translator.StringToObject(dhtHandler.getDht().read(parentTweetID));
		dhtHandler.addSecure(parentTweet.getKidSetID(),postTweet(msg, parentTweetID));
	}


	public void deleteTweet(String tweetID) throws BadSecretException, NotLoggedException {
		//Throw an exception if not logged
		isLogged();
		//Test if it's a tweet before deleting it...
		try {
			if(Translator.StringToObject(dhtHandler.getDht().read(tweetID)) instanceof Tweet)
				dhtHandler.writeSecure(tweetID, Translator.ObjectToString(new Tweet()));
		} catch (ValueNotFoundException e) {
		}
	}


	public void disconnect() {
		logged = false;
		user = null;
		lineset = null;
		followerReferences = null;
	}	
}
