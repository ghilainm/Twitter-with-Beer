package TwitterApiImplementation;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import DHT.DhtHandler;

public class TwitterHighLevelTaskHandler implements TwitterApi{

	private String userSecret;
	private String userName;
	private DhtHandler dhtHandler;
	//This variable is kept in memorry in order to avoid the problem of transaction
	//with a read inside during the adding and removing of a follower
	private int nbrFollowers;
	//Cache of the user i follow so that i only display the user i follow, 
	//and i don't have to make request all the time to update this follower list
	private Map<String, Boolean> usersIfollow;
	public final static String followersListField = "followersList";
	public final static String nbrFollowersField = "nbrFollowers";
	public final static String userField = "user";
	public final static String userlineField = "userline";
	public final static String timelineField = "timeline";
	public final static String followersField = "followersline";
	public final static String privatekeyField = "privatekey";

	public final static int CHUNCKSIZE = 20;

	private ListReader userLineReader;
	private ListReader timeLineReader;


	public TwitterHighLevelTaskHandler(String userSecret, String userName, DhtHandler d){
		this.userSecret = userSecret;
		this.userName = userName;
		dhtHandler = d;
	}

	public void initUsersIfollow(){
		String followersListString;
		while((followersListString = dhtHandler.readSet(followersKey(getUserName())))==null);
		try {
			LinkedList<String> followersList = UserIdSetList.parseFollowerList(followersListString).getFollowerList();
			usersIfollow = new Hashtable<String,Boolean>(followersList.size()+20);
			for(String user: followersList){
				usersIfollow.put(user, true);
				nbrFollowers++;
			}
			usersIfollow.put(userName,true);
		} catch (parsingException e) {
			e.printStackTrace();
		}
	}

	public boolean postTweet(String tweetMessage) throws MissingDataException{
		Tweet tweetToPost = new Tweet(tweetMessage, userName);
		String tweetLineIdToAdd = "<"+TweetsIdSetList.tweetBalise+">"+tweetToPost.getTweetId()+"</"+TweetsIdSetList.tweetBalise+">\n";
		//TODO add a check that the user exist or add a precondition to postTweet

		//Write the new tweet on the DHT
		while(!dhtHandler.writeSecure(userSecret, tweetToPost.getTweetId(), tweetToPost.toString()));
		
		//Add to myUserLine the tweet		
		//addListElement(userLineKey(getUserName()), tweetLineIdToAdd);
		while(!dhtHandler.addSecure(dhtHandler.noSecret(), userLineKey(getUserName()), userSecret, tweetLineIdToAdd));

		//Add to myTweetList the tweet
		//addListElement(timeLineKey(getUserName()), tweetLineIdToAdd);
		while(!dhtHandler.addSecure(dhtHandler.noSecret(), timeLineKey(getUserName()), userSecret, tweetLineIdToAdd));
		
		//Add to all the followers my tweet
		//Read the followers list
		String followersListString;
		while((followersListString = dhtHandler.readSet(followersKey(getUserName())))==null);

		try {
			LinkedList<String> followersList = FollowerList.parseFollowerList(followersListString).getFollowerList();
			for(String user: followersList){
				//add to the timeline of this user the new tweet.
				//addListElement(timeLineKey(user), tweetLineIdToAdd);
				while(!dhtHandler.addSecure(dhtHandler.noSecret(), timeLineKey(user), userSecret, tweetLineIdToAdd));
			}
		} catch (parsingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String readTweet(String tweetId){
		//Modify to write on the right list
		String tweetStr = "";
		while((tweetStr=dhtHandler.read(tweetId))==null);
		return tweetStr;
	}	


	public boolean listcutter(String key) throws parsingException, MissingDataException{		
		LinkedList<String> total = new LinkedList<String>();
		String newlink;
		//First get your tweetLine
		String line = "";
		while((line = dhtHandler.readSet(key))==null);
		if(line.compareTo(dhtHandler.NOT_FOUND())==0)
			throw new MissingDataException("My tweets list not found");
		TweetsIdSetList firstset = TweetsIdSetList.parseTweetList(line);
		total.addAll(firstset.getTweetsIdList());
		newlink = firstset.getNextList();

		//Get the next list if she exists
		if (newlink != null){
			while((line = dhtHandler.readSet(firstset.getNextList()))==null);
			if(line.compareTo(dhtHandler.NOT_FOUND())==0)
				throw new MissingDataException("My tweets list not found");
			TweetsIdSetList secondset = TweetsIdSetList.parseTweetList(line);
			total.addAll(secondset.getTweetsIdList());
			newlink = firstset.getNextList();
		}

		SortedSet<Tweet> tm = new TreeSet<Tweet>();

		for(String tweetId: total){
			String tweetReadStr;
			while((tweetReadStr=readTweet(tweetId))==null);
			Tweet tweetRead = Tweet.parseTweet(tweetReadStr);
			//Only take the tweet if the user is followed
			if(usersIfollow.containsKey(tweetRead.getUserName()) && usersIfollow.get(tweetRead.getUserName()))
				tm.add(tweetRead);
		}

		ArrayList<Tweet> list = new ArrayList<Tweet>(tm);
		//Removal of the first header
		boolean mustRemoveFirstHeader = list.size()>CHUNCKSIZE;

		while (list.size()>CHUNCKSIZE){
			ArrayList<Tweet> chunck = (ArrayList<Tweet>) list.subList(list.size()-CHUNCKSIZE, list.size());
			list.removeAll(chunck);

			String link = UUID.randomUUID().toString();
			TweetsIdSetList newlist = new TweetsIdSetList(newlink);
			while(!dhtHandler.addSecure(dhtHandler.noSecret(), link, userSecret, newlist.toString()));

			String tweetsToAddToNewList = "";
			for(Tweet tweet: chunck){
				while(!dhtHandler.removeSecure(dhtHandler.noSecret(), key, null, tweet.getTweetId()));
				if(firstset.getNextList().compareTo("null")!=0)
					while(!dhtHandler.removeSecure(dhtHandler.noSecret(), key, null, tweet.getTweetId()));
				tweetsToAddToNewList += tweet.toString();

			}
			while(!dhtHandler.addSecure(dhtHandler.noSecret(), link, userSecret, tweetsToAddToNewList));	
			newlink = link;
		}
		
		if(mustRemoveFirstHeader){
			TweetsIdSetList torem;
			torem = new TweetsIdSetList(firstset.getNextList());
			while(!dhtHandler.removeSecure(dhtHandler.noSecret(), key, null, torem.toString()));
		}
		return true;
	}




	public TwitterTaskResult createNewUser(User userguy){
		
		if(userExist(userguy.username()))
			return new TwitterTaskResult("User already exist", false);
		int nbrFunctions = 5;
		int functions[] = new int[nbrFunctions];
		
		KeyPair pair;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA","SUN");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(1024, random);
			pair = keyGen.generateKeyPair();
			privatekey = pair.getPrivate();
			publickey = pair.getPublic();
			
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			return new TwitterTaskResult("Unable to generate keys, No such algorithm", false);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return new TwitterTaskResult("Unable to generate keys, No such provider", false);
		}
		
		userguy.setPublicKey(publickey);
		//TODO must be in the transaction
		putPrivateKey();
		
		
		functions[0] = DhtHandler.functions.WRITESECURE.id();
		//TODO changer quand on aura la version avec le create.
		functions[1] = DhtHandler.functions.ADDSECURE.id();
		functions[2] = DhtHandler.functions.ADDSECURE.id();
		functions[3] = DhtHandler.functions.ADDSECURE.id();
		functions[4] = DhtHandler.functions.WRITESECURE.id();

		ArrayList<String[]> args = new ArrayList<String[]>(nbrFunctions);
		//Creation of the user in DHT
		String[] arg1 =  {userSecret, userKey(getUserName()), userguy.toString()};
		args.add(arg1);
		//Creation of the userline
		TweetsIdSetList userline = new TweetsIdSetList();
		String[] arg2 =  {dhtHandler.noSecret(), userLineKey(userName), userSecret,  userline.toString()};
		args.add(arg2);
		//Creation of the timeline
		TweetsIdSetList timeline = new TweetsIdSetList();
		String[] arg3 =  {dhtHandler.noSecret(), timeLineKey(userName), userSecret, timeline.toString()};
		args.add(arg3);
		// Add the creation of the follower List
		FollowerList followerlist = new FollowerList();
		String[] arg4 =  {dhtHandler.noSecret(), followersKey(userName), userSecret, followerlist.toString()};
		args.add(arg4);
		// Create the logIn information
		//TODO What do we store?
		String[] arg5 =  {userSecret, logInKey(), "SOMETHING"};
		args.add(arg5);
		try {
			boolean ret = dhtHandler.transaction(functions, args);
			if(ret){
				initUsersIfollow();
				initLines();
				return new TwitterTaskResult("User Created", true);
			}
			return new TwitterTaskResult("An unknown error has occured please retry", false);
		} catch (UnknownFunction e) {
			e.printStackTrace();
			return new TwitterTaskResult("Unknown function", false);
		} catch (parsingException e) {
			e.printStackTrace();
			return new TwitterTaskResult("Bad parsing entry", false);
		} catch (MissingDataException e) {
			e.printStackTrace();
			return new TwitterTaskResult("Bad entry", false);
		}
	}

	public String logInKey(){
		String keyDigest = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte [] bA = SecurityUtils.getBytesFromString((getUserName()+userSecret));
			keyDigest = SecurityUtils.getStringFromBytes(md.digest(bA));
		} catch (NoSuchAlgorithmException e) {
			System.exit(-1);
			e.printStackTrace();
		}
		return keyDigest;
	}

	public static String userKey(String userName){
		return userField+userName;
	}

	public static String userLineKey(String userName){
		return userlineField+userName;
	}

	public static String timeLineKey(String userName){
		return timelineField+userName;
	}

	public static String privateKeyKey(String userName){
		return privatekeyField+userName;
	}

	public static String followersKey(String userName){
		return followersField+userName;
	}

	public static String nbrFollowersKey(String userName){
		return nbrFollowersField+userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
	public boolean userExist(String userName){
		String str = dhtHandler.read(userKey(userName));
		if(str != null){
			if(str.compareTo(dhtHandler.NOT_FOUND())==0)
				return false;
			else {
				try {
					User.parseUser(str);
					return true;
				} catch (parsingException e) {
					return false;
				} catch (NonValidNameException e) {
					return false;
				}
			}
		}
		return false;
	}
	public TwitterTaskResult addFollower(String userIWantToFollow){
		if(userIWantToFollow.compareTo(getUserName())==0){
			return new TwitterTaskResult("You can't add yourself as follower", false);
		}
		//Verify user exist
		if(!userExist(userIWantToFollow))
			return new TwitterTaskResult("This user do not exist", false);
		int nbrFunctions = 2;
		int functions[] = new int[nbrFunctions];
		functions[0] = DhtHandler.functions.ADDSECURE.id();
		//TODO changer quand on aura la version avec le create.
		functions[1] = DhtHandler.functions.WRITESECURE.id();
		ArrayList<String[]> args = new ArrayList<String[]>(nbrFunctions);
		String arg1[] = {dhtHandler.noSecret(), followersKey(userIWantToFollow), userSecret, "<"+FollowerList.userBalise+">"+getUserName()+"</"+FollowerList.userBalise+">"};
		args.add(arg1);
		String arg2[] = {nbrFollowersKey(getUserName()),userSecret,nbrFollowers+""};
		args.add(arg2);
		try {
			if(dhtHandler.transaction(functions, args)){
				nbrFollowers++;
				usersIfollow.put(userIWantToFollow,true);
				return new TwitterTaskResult("Followers added", true);
			}
			return new TwitterTaskResult("An unknown error has occured, please retry", false);
		} catch (UnknownFunction e) {
			e.printStackTrace();
			return new TwitterTaskResult("An unknown error has occured, please retry", false);
		}
	}

	public TwitterTaskResult removeFollower(String userIWantToFollow) {
		if(!userExist(userIWantToFollow))
			return new TwitterTaskResult("This user do not exist", false);
		int nbrFunctions = 2;
		int functions[] = new int[nbrFunctions];
		functions[0] = DhtHandler.functions.REMOVESECURE.id();
		//TODO changer quand on aura la version avec le create.
		functions[1] = DhtHandler.functions.WRITESECURE.id();
		ArrayList<String[]> args = new ArrayList<String[]>(nbrFunctions);
		String arg1[] = {followersKey(userIWantToFollow), userSecret, followersKey(userIWantToFollow), userSecret, "<"+FollowerList.userBalise+">"+getUserName()+"</"+FollowerList.userBalise+">"};
		args.add(arg1);
		String arg2[] = {nbrFollowersKey(getUserName()),userSecret,nbrFollowers+""};
		args.add(arg2);
		try {
			if(dhtHandler.transaction(functions, args)){
				nbrFollowers--;
				usersIfollow.put(userIWantToFollow,false);
				return new TwitterTaskResult("Removed from follower", true);
			}
			return new TwitterTaskResult("An unknown error has occured, please retry", false);
		} catch (UnknownFunction e) {
			e.printStackTrace();
			return new TwitterTaskResult("An unknown error has occured, please retry", false);
		}
	}

	public class TweetList{
		public String nextList;
		public SortedSet<Tweet> tweets;
	}

	//return true if the user exists and has correct password.
	public TwitterTaskResult logIn() throws parsingException, MissingDataException{
		String requestResult = dhtHandler.read(logInKey());
		if(requestResult != null){
			if(requestResult.compareTo(dhtHandler.NOT_FOUND())!=0){
				//Initialisation of the cache of the userIfollow
				initUsersIfollow();
				//Initilisation of the lines
				initLines();
				return new TwitterTaskResult("Logged in", true);
			}
			else	
				return new TwitterTaskResult("This user does not exists", false);
		}
		else 
			return new TwitterTaskResult("An unknown error has occured, please retry", false);
	}

	public LinkedList<Tweet> getTimeLine(){
		try {
			return timeLineReader.more();
		} catch (MissingDataException e) {
			System.exit(-1);
			e.printStackTrace();
		} catch (parsingException e) {
			System.exit(-1);
			e.printStackTrace();
		}
		return new LinkedList<Tweet>();
	}

	public LinkedList<Tweet> getUserLine(){
		try {
			return userLineReader.more();
		} catch (MissingDataException e) {
			System.exit(-1);
			e.printStackTrace();
		} catch (parsingException e) {
			System.exit(-1);
			e.printStackTrace();
		}
		return new LinkedList<Tweet>();
	}

	private void initLines() throws parsingException, MissingDataException{
		listcutter(timeLineKey(getUserName()));
		listcutter(userLineKey(getUserName()));
		timeLineReader = new ListReader(timeLineKey(getUserName()), usersIfollow, CHUNCKSIZE, dhtHandler, this);
		userLineReader = new ListReader(userLineKey(getUserName()), usersIfollow, CHUNCKSIZE, dhtHandler, this);
	}

	public LinkedList<Tweet> refreshTimeLine() {
		try {
			return timeLineReader.refresh();
		} catch (MissingDataException e) {
			e.printStackTrace();
		} catch (parsingException e) {
			e.printStackTrace();
		}
		return new LinkedList<Tweet>();
	}

	public LinkedList<Tweet> refreshUserLine() {
		try {
			return userLineReader.refresh();
		} catch (MissingDataException e) {
			e.printStackTrace();
		} catch (parsingException e) {
			e.printStackTrace();
		}
		return new LinkedList<Tweet>();
	}

	public boolean addListElement(String listName, String content){
		
		if (privatekey == null){
			getPrivateKey();}
		ListEntry elementToAdd = new ListEntry(content, userName, listName, privatekey);
		return dhtHandler.addSecure(dhtHandler.noSecret(), listName, userSecret, elementToAdd.toString());
	}


	public boolean getPrivateKey(){
		String key = "";
		while((key=dhtHandler.read(privateKeyKey(userName)))==null);
		try {
			byte [] pribyte = decode.decodeBuffer(key);
			ByteArrayInputStream fis = new ByteArrayInputStream(pribyte);
			ObjectInputStream ois = new ObjectInputStream(fis);
			privatekey  = (PrivateKey) ois.readObject();
			ois.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (privatekey != null);
	}

	
	public boolean putPrivateKey(){
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(privatekey);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String topost = encode.encode(baos.toByteArray());
		
		while(!dhtHandler.writeSecure(userSecret, privateKeyKey(userName), topost));
		
		return true;
		
	}
}
