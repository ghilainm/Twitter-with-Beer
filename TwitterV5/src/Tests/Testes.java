package Tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;


import TwitterAPIExceptions.BadPasswordException;
import TwitterAPIExceptions.NotLoggedException;
import TwitterAPIExceptions.PassWordTooWeak;
import TwitterAPIExceptions.ThisLineAlreadyExistException;
import TwitterAPIExceptions.ThisListAlreadyExistException;
import TwitterAPIExceptions.TweetEmptyException;
import TwitterAPIExceptions.TwitterApiException;
import TwitterAPIExceptions.UserAlreadyUsedException;
import TwitterAPIExceptions.UserNameInvalid;
import TwitterAPIExceptions.UserNotFoundException;
import TwitterApiImplementation.Constants;
import TwitterApiImplementation.Tweet;
import TwitterApiImplementation.TwitterAPI;
import TwitterApiImplementation.TwitterImplementation;

import DHT.DebugHandler;
import DHT.DhtAPI;
import DHTExceptions.BadSecretException;
import DHTExceptions.DhtException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;

public class Testes {
	
	public static void test1(int nbrUsers, int nbrFollowersPerUser, int nbrTweetPostedByUser) throws UserAlreadyUsedException, RemoteException, ValueNotFoundException, UserNotFoundException, NotLoggedException, PassWordTooWeak, UserNameInvalid, TweetEmptyException{
		
		Long tt = System.currentTimeMillis();
		DhtAPI dhtAPI = new DebugHandler();
		
		//Create nbrUsers users
		ArrayList<TwitterAPI> userWorlds = new ArrayList<TwitterAPI>(nbrUsers);
		//try {
			for(int i = 0; i<nbrUsers; i++){
				userWorlds.add(new TwitterImplementation(dhtAPI));
				userWorlds.get(i).createUser("user"+i, "password5^^^$"+i, "userName a"+i);
		//	}
		//} catch (UserAlreadyUsedException e) {
		//	e.printStackTrace();
		}
		
		for(int i = 0; i<nbrUsers; i++){
			for(int j = 0; j<nbrFollowersPerUser; j++)
				userWorlds.get(i).addUser(Constants.getTimeLineName(), userWorlds.get((j+i)%nbrUsers).getUserName());
		}
		
		for(int i = 0; i<nbrUsers; i++){
			for(int j = 0; j<nbrTweetPostedByUser; j++)
				try {
					userWorlds.get(i).postTweet("test"+i);
				} catch (ValueNotFoundException e) {
					e.printStackTrace();
				} catch (NotLoggedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		System.out.println();
		System.out.println((System.currentTimeMillis()-tt)/1000);
	}
	
	public static void testTwitterAPI() throws NotLoggedException, RemoteException, ValueNotFoundException, UserAlreadyUsedException, BadSecretException, UserNotFoundException, PassWordTooWeak, UserNameInvalid, TweetEmptyException{
		DhtAPI dhtAPI = new DebugHandler();
		
		//Creating Matthieu
		TwitterAPI matthieuWorld = null;
		try {
			matthieuWorld = new TwitterImplementation(dhtAPI);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		matthieuWorld.createUser("Matthieu", "Pass1))))))))", "Matthieu Ghilain");
		System.out.println("Matthieu Created\n");
		
		//Posting a tweet
		matthieuWorld.postTweet("Coucou");
		System.out.println("Coucou posted\n");
		
		//Reading the tweet on timeLine
		LinkedList<Tweet> ll = matthieuWorld.moreTweetsFromLine(Constants.getTimeLineName(),matthieuWorld.getUserName());
		for(Tweet t  : ll)
			System.out.println(t.toString());
		System.out.println("\n Tweet read on time line\n");
		
		//Reading the tweet on timeLine
		ll = matthieuWorld.moreTweetsFromLine(Constants.getUserLineName(),matthieuWorld.getUserName());
		for(Tweet t  : ll)
			System.out.println(t.toString());
		System.out.println("\n Tweet read on user line\n");
		
		//Delete the tweet
		matthieuWorld.deleteTweet(ll.get(0).getTweetID());
		System.out.println("Tweet deleted \n");
		
		//Trying to read the deleted tweet on timeLine
		ll = matthieuWorld.allTweet(Constants.getTimeLineName());
		System.out.println("Tweet deleted ok ? "+ll.size()==0+"\n");
		
		//Creating Xavier
		TwitterAPI xavierWorld = null;
		try {
			xavierWorld = new TwitterImplementation(dhtAPI);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xavierWorld.createUser("Xavier", "GothiquePass", "Xavier Decoster");
		System.out.println("Xavier Created\n");
		
		//add a follower
		xavierWorld.addUser(Constants.getUserLineName(), "Matthieu");
		System.out.println("Xavier added matthieu as follower on his userline");
		
		//Matthieu post a new tweet
		matthieuWorld.postTweet("Coucou xavier");
		System.out.println("Matthieu posted a new tweet");
		
		//Read tweet from xavier
		ll = xavierWorld.moreTweetsFromLine(Constants.getUserLineName(),xavierWorld.getUserName());
		for(Tweet t  : ll)
			System.out.println(t.toString());
		System.out.println("\n Tweet read on user line from xavier\n");
		
		//Xavier make a retweet because he is a king!
		xavierWorld.reTweet(ll.get(0).getTweetID());
		System.out.println("\n retweet done");
		
		//We reread the tweet from xavier and check retweet is there
		ll = xavierWorld.allTweet(Constants.getUserLineName());
		for(Tweet t  : ll)
			System.out.println(t.toString()+"\n");		
		System.out.println("\n Tweet read on user line from xavier\n");
		
		//Try a reply....
		xavierWorld.reply("Je suis une queue j'ai retweeter un message de merde", ll.get(0).getTweetID());
		
		ll = xavierWorld.allTweet(Constants.getUserLineName());
		for(Tweet t  : ll)
			System.out.println(t.toString()+"\n");
		
		System.out.println("\n Tweet read on user line from xavier\n");
	}
	
	//Post a lot of tweets and check wether there are find back and not displayed several times
	public static void oneUserTest() throws PassWordTooWeak, UserNameInvalid, TweetEmptyException{

		DebugHandler d = new DebugHandler();
		int nbrTweets  = 3;
		HashSet<String> tweetAdded = new HashSet<String>(200);
		try {
			TwitterAPI twitterAPI = new TwitterImplementation(d);
			//Create a new user ....
			twitterAPI.createUser("test", "password5$$$$", "realName a");
			String tweet = "tweet";
			int i = 0;
			while(i<nbrTweets){
				twitterAPI.postTweet(tweet+i);
				tweetAdded.add(tweet+i);
				i++;
			}
			
			//Test ALL
			LinkedList<Tweet> tweetListResult = twitterAPI.allTweet(Constants.getUserLineName());
			tweetAdded.removeAll(GetMessages(tweetListResult));
			assert tweetAdded.size()==0;
			
			//Test removeTweet ( we remove every twet added)
			i = 0;
			for(Tweet t : tweetListResult){
				twitterAPI.deleteTweet(t.getTweetID());
			}
			tweetListResult = twitterAPI.allTweet(Constants.getUserLineName());
			assert tweetListResult.size() == 0;
			
			//Test Refresh (fist add nbrTweets)
			i = 0;
			while(i<nbrTweets){
				twitterAPI.postTweet(tweet+i);
				tweetAdded.add(tweet+i);
				i++;
			}
			tweetListResult = twitterAPI.refreshTweetsFromLine(Constants.getUserLineName(),"test");
			tweetAdded.removeAll(GetMessages(tweetListResult));
			//All are well read? 
			assert tweetAdded.size() == 0;
			
			twitterAPI.postTweet(tweet+nbrTweets); //Add one tweet
			tweetListResult = twitterAPI.refreshTweetsFromLine(Constants.getUserLineName(),"test");
			//Does i read only one tweet ? 
			assert tweetListResult.size() == 1;
			
			//Test More
			//Nothing before so should return nothing because that was not the first
			tweetListResult = twitterAPI.moreTweetsFromLine(Constants.getUserLineName(),"test");
			assert tweetListResult.size() == 0;
			
			//Restart the session
			twitterAPI = new TwitterImplementation(d);
			//Log in 
			twitterAPI.logIn("test", "password5$$$$");
			tweetListResult = twitterAPI.moreTweetsFromLine(Constants.getUserLineName(),"test");
			while(tweetListResult.addAll(twitterAPI.moreTweetsFromLine(Constants.getUserLineName(),"test")));
			//SHould find back the tweets posted before so nbrtweets + 1
			assert tweetListResult.size() == nbrTweets+1;
			
			//Post a new tweets
			twitterAPI.postTweet(tweet+nbrTweets+1);
			tweetListResult = twitterAPI.moreTweetsFromLine(Constants.getUserLineName(),"test");
			//should not be seen by the more
			assert tweetListResult.size() == 0;
			//Should be seen by the refresh
			tweetListResult = twitterAPI.refreshTweetsFromLine(Constants.getUserLineName(),"test");
			assert tweetListResult.size() == 1;
			
			System.out.println("ok for standard operations...");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ValueNotFoundException e) {
			e.printStackTrace();
		} catch (NotLoggedException e) {
			e.printStackTrace();
		} catch (BadSecretException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserAlreadyUsedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void crashTest() throws PassWordTooWeak, UserNameInvalid{
		DebugHandler d = new DebugHandler();
		try {
			boolean madeException1 = false;
			boolean madeException2 = false;
			boolean madeException3 = false;
			TwitterAPI twitterAPI = new TwitterImplementation(d);
			twitterAPI.createUser("userName", "password5$", "realNam a");
			
			//Trying to create another user with the same username... should make an exception
			try{
				TwitterAPI twitterAPI2 = new TwitterImplementation(d);
				twitterAPI2.createUser("userName", "password1548$", "realName1 a");
			}
			catch (UserAlreadyUsedException e) {
				madeException1 = true;
			} catch (UserNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try{
				TwitterAPI twitterAPI2 = new TwitterImplementation(d);
				twitterAPI2.createUser("userName", "password8541$", "realName a");
			}
			catch (UserAlreadyUsedException e) {
				madeException2 = true;
			}
			try{
				TwitterAPI twitterAPI2 = new TwitterImplementation(d);
				twitterAPI2.logIn("userName", "password2é'");
			}
			catch (Exception e){
				madeException3 = true;
			}
			if(madeException1 && madeException2 && madeException3)
				System.out.println("LogIn and CreateUser Ok...");
			else
				System.out.println("CrashTEst failed...");
			
			TwitterAPI twitterAPI2 = new TwitterImplementation(d);
			twitterAPI2.createUser("userName2", "password----9", "realName a");
			try {
				twitterAPI2.refreshTweetsFromLine("bg", "b1");
			} catch (NotLoggedException e) {
				e.printStackTrace();
			}
			try {
				twitterAPI2.refreshTweetsFromLine("bbbbbb", "userName2");
			} catch (NotLoggedException e) {
				e.printStackTrace();
			}
			try {
				twitterAPI2.refreshTweetsFromLine(Constants.getUserLineName(), "efsegqfsd");
			} catch (NotLoggedException e) {
				e.printStackTrace();
			}
			try {
				twitterAPI2.refreshTweetsFromList("bad input");
			} catch (NotLoggedException e) {
				e.printStackTrace();
			}
			try {
				twitterAPI2.moreTweetsFromList("bad input");
			} catch (NotLoggedException e) {
				e.printStackTrace();
			}
			try {
				twitterAPI2.moreTweetsFromLine("bad input...", "bad name...");
			} catch (NotLoggedException e) {
				e.printStackTrace();
			}
			try {
				twitterAPI2.moreTweetsFromLine("bb", "userName2");
			} catch (NotLoggedException e) {
				e.printStackTrace();
			}
			try {
				twitterAPI2.moreTweetsFromLine(Constants.getTimeLineName(), "bad nam...");
			} catch (NotLoggedException e) {
				e.printStackTrace();
			}
			System.out.println("okeyyyyyyyyy for crash test!");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (UserAlreadyUsedException e) {
			e.printStackTrace();
		} catch (ValueNotFoundException e) {
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void TestListLine() throws PassWordTooWeak, UserNameInvalid, TweetEmptyException{
		DhtAPI d = new DebugHandler();
		TwitterAPI twitterAPI;
		try {
			twitterAPI = new TwitterImplementation(d);
			twitterAPI.createUser("userName", "password585-", "realName a");
			TwitterAPI twitterAPI2 = new TwitterImplementation(d);
			twitterAPI2.createUser("fu","se82dddd8-f","bibiestsuperpetit e");
			boolean Ok = false;
			try {
				twitterAPI.createLine(Constants.getTimeLineName());
			} catch (SecretProtectedException e) {
				e.printStackTrace();
			} catch (BadSecretException e) {
				e.printStackTrace();
			} catch (NotLoggedException e) {
				e.printStackTrace();
			} catch (ThisLineAlreadyExistException e) {
				Ok = true;
			}
			if(!Ok){
				System.out.println("Error Created line that already exist");
			}
			
			boolean recreatedList = true;
			try {
				twitterAPI.createList(Constants.getFavListName());
			} catch (SecretProtectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadSecretException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotLoggedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ThisListAlreadyExistException e) {
				recreatedList = false;
			}
			
			if(recreatedList){
				System.out.println("Error created a list that already exist");
			}
			
			//Let's add user on list...
			try {
				twitterAPI.addUser(Constants.getTimeLineName(), "fu");
			} catch (UserNotFoundException e) {
				e.printStackTrace();
			} catch (NotLoggedException e) {
				e.printStackTrace();
			}
			twitterAPI2.postTweet("coucou les amis");
			
			//should see the tweet of the guy that I just added
			LinkedList<Tweet> lTweets = twitterAPI.refreshTweetsFromLine(Constants.getTimeLineName(), "fu");
			assert lTweets.size() == 1;
			lTweets = twitterAPI.refreshTweetsFromLine(Constants.getTimeLineName(), "fu");
			assert lTweets.size() == 0;
			lTweets = twitterAPI.refreshTweetsFromLine(Constants.getTimeLineName(), "userName");
			assert lTweets.size() == 1;
			//Should see nothing cause nothing on this line
			lTweets = twitterAPI.refreshTweetsFromLine(Constants.getUserLineName(), "userName");
			assert lTweets.size() == 0;
			
			//Let's try to see the line of someone else...
			lTweets = twitterAPI.refreshTweetsFromLine(Constants.getUserLineName(), "fu");
			assert lTweets.size() == 1;
			
			//Let's retweet the tweet read and check if it appears on on lines
			twitterAPI.reTweet(lTweets.get(0).getTweetID());
			try {
				twitterAPI.reply("Je suis super bonne", lTweets.get(0).getTweetID());
			} catch (BadSecretException e) {
				System.out.println("error......");
			}
			lTweets = twitterAPI.refreshTweetsFromLine(Constants.getUserLineName(), "userName");
			assert lTweets.size() == 2;
			System.out.println("Line and List ok!");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserAlreadyUsedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValueNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotLoggedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static Collection<String> GetMessages(LinkedList<Tweet> tweetListResult) {
		LinkedList<String> result = new LinkedList<String>();
		for(Tweet t : tweetListResult)
			result.add(t.getMsg());
		return result;
	}
	private static String randomString(){
		double x = Math.random();
		if(x<0.1)
			return null;
		if(x<0.2)
			return " ";
		if(x<0.3)
			return "";
		if(x<0.4)
			return " & & &";
		if(x<0.5)
			return "Bibi & Xavier";
		if(x<0.8)
			return "SuperRandom...";
		if(x<0.9)
			return "STest"+Math.random();
		else 
			return "Bibibbbbbbbbaaaaaaaaaaaaaaaa";
	}
	private static void fuzzTestingRandom() throws RemoteException{
		DhtAPI d = new DebugHandler();
		try {
			TwitterAPI t = new TwitterImplementation(d);
			t.createUser("Coucoulespot", "password48654$", "realName  b");
			Method[] twittAPImeth = TwitterAPI.class.getMethods();
			int nbrCalls = 10000;
			int i = 0;
			int nbrMeth = twittAPImeth.length;
			Method metToCall;
			int nbrParam;
			while(i<nbrCalls){
				try{
				metToCall = twittAPImeth[(int)(Math.random()*nbrMeth)];
				if(metToCall.getName().equals("disconnect") || metToCall.getName().equals("logIn")|| metToCall.getName().equals("createUser"))continue;
				nbrParam = metToCall.getParameterTypes().length;
				System.out.println("Call "+metToCall.getName());
				if(nbrParam == 0)
						metToCall.invoke(t);
				if(nbrParam == 1)
					metToCall.invoke(t,randomString());
				if(nbrParam == 2)
					metToCall.invoke(t,randomString(),randomString());
				if(nbrParam == 3)
					metToCall.invoke(t,randomString(),randomString(),randomString());
				if(nbrParam == 4)
					metToCall.invoke(t,randomString(),randomString(),randomString(),randomString());
				if(nbrParam == 5)
					metToCall.invoke(t,randomString(),randomString(),randomString(),randomString(),randomString());
				}catch (IllegalArgumentException e) {
					
				} catch (IllegalAccessException e) {
					
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} 
				i++;
			}
		}catch (TwitterApiException e) {
			
		}catch (DhtException e) {
			
		}
		System.out.println("finish fuzz test");
	}
	
	public static String makeRealName(){
		Random r = new Random();
		int nbrChar = r.nextInt() % 20 +10;
		String realName = "e";
		int i = 0;
		while(i<nbrChar){
			realName += ((char)((Math.abs(r.nextInt())%(122-97))+97));
			i++;
		}
		i = 0;
		realName +=" b";
		nbrChar = r.nextInt() % 20 +10;
		while(i<nbrChar){
			realName += ((char)((Math.abs(r.nextInt())%(122-97))+97));
			i++;
		}
		return realName;
	}
	public static void fuzzTestingLegalOp(){
		DhtAPI d = new DebugHandler();
		TwitterAPI t;
		try {
			t = new TwitterImplementation(d);
			t.createUser("Coucoulespot", "password48654$", "realName  b");
			int i = 0;
			int nbrOp = 1000000;
			double x = 0;
			Hashtable<String,LinkedList<String>> lineNames = new Hashtable<String,LinkedList<String>>();
			Hashtable<String,LinkedList<String>> listNames = new Hashtable<String,LinkedList<String>>();
			LinkedList<String> userNames = new LinkedList<String>();
			Hashtable<String,TwitterAPI> tapih = new Hashtable<String,TwitterAPI>();
			
			userNames.add("Coucoulespot");
			tapih.put("Coucoulespot", t);
			LinkedList<String> nLineC = new LinkedList<String>();
			nLineC.add(Constants.getTimeLineName());
			nLineC.add(Constants.getUserLineName());
			lineNames.put("Coucoulespot",nLineC);
			LinkedList<String> nListC = new LinkedList<String>();
			nListC.add(Constants.getFavListName());
			listNames.put("Coucoulespot",nListC);
			
			Random r = new Random();
			
			while(i<nbrOp){
				try{
				x = r.nextDouble();
				String rUser = userNames.get(Math.abs(r.nextInt())%userNames.size());
				if(x<0.02){
					//Create a new user
					String newUserName = "userName"+r.nextDouble();
					userNames.add(newUserName);
					TwitterAPI ntapi = new TwitterImplementation(d);
					ntapi.createUser(newUserName, "monpassword"+r.nextDouble(), makeRealName());
					tapih.put(newUserName, ntapi);
					LinkedList<String> nLineCC = new LinkedList<String>();
					nLineCC.add(Constants.getTimeLineName());
					nLineCC.add(Constants.getUserLineName());
					lineNames.put(newUserName,nLineCC);
					LinkedList<String> nListCC = new LinkedList<String>();
					nListCC.add(Constants.getFavListName());
					listNames.put(newUserName,nListCC);
				}
				else if(x<0.03){
					//RANDOM USER ADDED TO LINE
					LinkedList<String> rLines = lineNames.get(rUser);
					String rLine = rLines.get(Math.abs(r.nextInt())%rLines.size());
					tapih.get(rUser).addUser(rLine, userNames.get(Math.abs(r.nextInt())%userNames.size()));
				}
				else if(x<0.34){
					//POst a tweet...
					tapih.get(rUser).postTweet("tweeet"+r.nextDouble());
				}
				else if(x<0.35){
					//Make a new line :)
					tapih.get(rUser).createLine("lineName"+r.nextDouble());
				}
				else if(x<0.39){
					//Add user to line
					tapih.get(rUser).createLine("lineName"+r.nextDouble());
				}
				else if(x<0.65){
					//refresh tweets from line!
					LinkedList<String> rLines = lineNames.get(rUser);
					String rLine = rLines.get(Math.abs(r.nextInt())%rLines.size());
					tapih.get(rUser).refreshTweetsFromLine(rLine, userNames.get(Math.abs(r.nextInt())%userNames.size()));
				}
				else if(x<0.85){
					//more tweets from line
					LinkedList<String> rLines = lineNames.get(rUser);
					String rLine = rLines.get(Math.abs(r.nextInt())%rLines.size());
					tapih.get(rUser).moreTweetsFromLine(rLine, userNames.get(Math.abs(r.nextInt())%userNames.size()));
				}
				else if(x<1){
					LinkedList<String> rLines = lineNames.get(rUser);
					String rLine = rLines.get(Math.abs(r.nextInt())%rLines.size());
					tapih.get(rUser).moreTweetsFromLine(rLine, userNames.get(Math.abs(r.nextInt())%userNames.size()));
				}
				i++;} catch (RemoteException e) {
					e.printStackTrace();
				} catch (TwitterApiException e){
					e.printStackTrace();
				} catch (DhtException e){
					e.printStackTrace();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("Fuzz test is finish without error !");
	}
	public static void main(String[] args) throws PassWordTooWeak, UserNameInvalid, TweetEmptyException, RemoteException{
		//oneUserTest();
		//crashTest();
		//TestListLine();
		//fuzzTestingRandom();
		fuzzTestingLegalOp();
	}
}
