package Tests;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;

import TwitterAPIExceptions.NotLoggedException;
import TwitterAPIExceptions.ThisLineAlreadyExistException;
import TwitterAPIExceptions.UserAlreadyUsedException;
import TwitterAPIExceptions.UserNotFoundException;
import TwitterApiImplementation.Constants;
import TwitterApiImplementation.Tweet;
import TwitterApiImplementation.TwitterAPI;
import TwitterApiImplementation.TwitterImplementation;
import TwitterApiImplementation.User;

import DHT.DebugHandler;
import DHT.DhtAPI;
import DHTExceptions.BadSecretException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;

public class TestSimulation {

	DhtAPI dhtAPI;
	protected HashMap<String, TwitterAPI> worldStore;
	
	public TestSimulation(){
		dhtAPI = new DebugHandler();
		worldStore = new HashMap<String, TwitterAPI>();
	}
	
	
	
	
	public void createUser(String userName, String passWord, String fullName) throws RemoteException, UserAlreadyUsedException, ValueNotFoundException{
		worldStore.put(userName, new TwitterImplementation(dhtAPI));
		worldStore.get(userName).createUser(userName, passWord, fullName);
	}
	
	
	
	public void testTwitterAPI() throws NotLoggedException, RemoteException, ValueNotFoundException, UserAlreadyUsedException, BadSecretException, UserNotFoundException{
		
		//Creating Matthieu
		TwitterAPI matthieuWorld = null;
		try {
			matthieuWorld = new TwitterImplementation(dhtAPI);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		matthieuWorld.createUser("Matthieu", "Pass1", "Matthieu Ghilain");
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
	
	
	public void printTweets(LinkedList<Tweet> tweets){
		System.out.println("print tweets: ");
		for(Tweet tw : tweets){
			System.out.println(tw.getMsg());
		}
		System.out.println();

	}
	
	public void printUsers(LinkedList<User> users){
		System.out.println("print users: ");
		for(User u : users){
			System.out.println(u.getUserName());
		}
		System.out.println();

	}
	
	
	
	public void printStrings(LinkedList<String> str){
		System.out.println("print users: ");
		for(String u : str){
			System.out.println(u);
		}
		System.out.println();

	}
	
	
	public static void main(String[] args) throws RemoteException, UserAlreadyUsedException, ValueNotFoundException, SecretProtectedException, BadSecretException, NotLoggedException, ThisLineAlreadyExistException, UserNotFoundException, InterruptedException{
		TestSimulation sim = new TestSimulation();
		LinkedList<Tweet> tweets;
		
		for(int i = 0; i<100 ;i++){
		sim.createUser("Paul"+i, "bonjour" + i, "Jean Paul"+i);
		}
		
		sim.worldStore.get("Paul1").createLine("Musique");
		sim.worldStore.get("Paul1").postTweet("Hello World I am here");
		//Thread.sleep(1000);
		sim.worldStore.get("Paul2").postTweet("I am secretly crying");
		//Thread.sleep(1000);
		sim.worldStore.get("Paul1").addUser("Musique", "Paul2");
		//Thread.sleep(1000);
		sim.worldStore.get("Paul2").postTweet("I am in a huge band");
		//Thread.sleep(1000);

		
		sim.worldStore.get("Paul2").postTweet("and I am awesome");

		sim.printStrings(sim.worldStore.get("Paul1").allUsersFromLine("Musique", "Paul1"));
		//Thread.sleep(1000);
		tweets = sim.worldStore.get("Paul1").moreTweetsFromLine("Musique", "Paul1");
		//Thread.sleep(1000);
		sim.printTweets(tweets);
		//Thread.sleep(1000);
		sim.worldStore.get("Paul2").postTweet("I say dull things");

		//sim.printTweets(sim.worldStore.get("Paul1").allTweet("Musique"));
		sim.worldStore.get("Paul1").removeUser("Musique", "Paul2");
		//Thread.sleep(1000);
		sim.worldStore.get("Paul2").postTweet("why the unlove?");
		//Thread.sleep(1000);
		tweets = sim.worldStore.get("Paul1").refreshTweetsFromLine("Musique", "Paul1");
		//Thread.sleep(1000);
		sim.printTweets(tweets);
		//Thread.sleep(1000);
		sim.printStrings(sim.worldStore.get("Paul1").allUsersFromLine("Musique", "Paul1"));
		//Thread.sleep(1000);
		sim.worldStore.get("Paul1").addUser("Musique", "Paul2");
		//Thread.sleep(1000);
		sim.printStrings(sim.worldStore.get("Paul1").allUsersFromLine("Musique", "Paul1"));
		//Thread.sleep(1000);
		sim.worldStore.get("Paul2").postTweet("back on train?");	
		//Thread.sleep(1000);
		
		tweets = sim.worldStore.get("Paul1").refreshTweetsFromLine("Musique", "Paul1");
		//Thread.sleep(1000);
		sim.printTweets(tweets);
		
		

	}
}
