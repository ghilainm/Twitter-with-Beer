package Tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;

import TwitterAPIExceptions.NotLoggedException;
import TwitterAPIExceptions.UserAlreadyUsedException;
import TwitterApiImplementation.Constants;
import TwitterApiImplementation.Tweet;
import TwitterApiImplementation.TwitterAPI;
import TwitterApiImplementation.TwitterImplementation;
import TwitterApiImplementation.User;

import DHT.BeernetHandler;
import DHT.DebugHandler;
import DHT.DhtAPI;
import DHTExceptions.ValueNotFoundException;

public class TheTestor {
	protected HashMap<String, TwitterAPI> worldStore;
	DhtAPI dhtAPI;


	public TheTestor(DhtAPI dhtAPI){		
		this.dhtAPI =dhtAPI;
		worldStore = new HashMap<String, TwitterAPI>();
	}

	private TwitterImplementation getworld(String userName) throws RemoteException, UserAlreadyUsedException, ValueNotFoundException{
		if(! worldStore.containsKey(userName)){
			worldStore.put(userName, new TwitterImplementation(dhtAPI));
			worldStore.get(userName).createUser(userName, "LOTR", "Probably a Hobbit");}

		return (TwitterImplementation) worldStore.get(userName);
	}


	public void loadFile(String filename) throws NotLoggedException, UserAlreadyUsedException, ValueNotFoundException{
		File file = new File(filename);
		String line; 
		String[] entry;
		BufferedReader input;
		try {
			input = new BufferedReader(new FileReader(file));
			while (( line = input.readLine()) != null){
				if (!line.isEmpty() && line.charAt(0)!='<' && line.charAt(0)!=' '){	
					entry = line.split(":");
					if (entry.length > 1){
					getworld(entry[0]).postTweet(entry[1]);
					}
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	public static void main(String[] args) throws NotLoggedException, UserAlreadyUsedException, ValueNotFoundException, RemoteException{
		DhtAPI dht = new BeernetHandler(Integer.parseInt(args[0]));
		
		TheTestor test = new TheTestor(dht);
		test.loadFile("lotr_fotr_script.txt");
		test.loadFile("lotr_tt_script.txt");
		
		LinkedList<Tweet> tweets = test.worldStore.get("Frodo").moreTweetsFromLine(Constants.getTimeLineName(), "Frodo");
		test.printTweets(tweets);
		while (tweets.size() >0 ){
			tweets = test.worldStore.get("Frodo").moreTweetsFromLine(Constants.getTimeLineName(), "Frodo");
			test.printTweets(tweets);
		}
		

	}

}
