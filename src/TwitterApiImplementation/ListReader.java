package TwitterApiImplementation;

import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;



public class ListReader {

	private DhtHandler dhtHandler;

	private String firsttweetID;
	private LinkedList<String> firstTweets;
	private LinkedList<String> previousTweets;
	private LinkedList<String> currentTweets;
	private LinkedList<String> previouslydisplayedTweets;
	//TODO on utilise jamais ces variables...
	private int numtweets;
	private final int CHUNCKSIZE;
	private final String startList;
	private String currentList;
	private String nextList;
	private Map<String, Boolean> userIfollow;
	private TwitterHighLevelTaskHandler twitterHighLevelTaskHandler;


	public ListReader(String key, Map<String, Boolean> userIfollow, int chunksize, DhtHandler dhtHandler, TwitterHighLevelTaskHandler t){
		startList = key;
		firstTweets = new LinkedList<String>();
		firsttweetID ="";
		this.dhtHandler = dhtHandler;
		
		currentList = key;
		numtweets = 0;
		previousTweets = new LinkedList<String>();
		currentTweets = new LinkedList<String>();
		nextList = null;

		CHUNCKSIZE = chunksize;
		this.userIfollow = userIfollow;
		firsttweetID = "";
		twitterHighLevelTaskHandler = t;
	}

	public LinkedList<Tweet> more() throws MissingDataException, parsingException{
		if (firsttweetID == ""){
			return first();
		}
		else
			return next();
	}
	
	
	
	public LinkedList<Tweet> first() throws MissingDataException, parsingException{		
		LinkedList<String> relevantset = new LinkedList<String>();
		
		String line = "";
		TweetsIdSetList tweetsIdListParsed;
		while((line = dhtHandler.readSet(startList))==null);
		if(line.compareTo(dhtHandler.NOT_FOUND())==0)
			throw new MissingDataException("My tweets list not found");
		tweetsIdListParsed = TweetsIdSetList.parseTweetList(line);

		currentList = startList;
		nextList = tweetsIdListParsed.getNextList();
		firstTweets = tweetsIdListParsed.getTweetsIdList();
		currentTweets = firstTweets;
		// previousTweets unchanged
		relevantset = currentTweets;
		numtweets = relevantset.size();
		
		//forward checking
		if (tweetsIdListParsed.isNextList()){
			while((line = dhtHandler.readSet(nextList))==null);
			if(line.compareTo(dhtHandler.NOT_FOUND())==0)
				throw new MissingDataException("My tweets list not found");
			tweetsIdListParsed = TweetsIdSetList.parseTweetList(line);
			previousTweets = currentTweets;
			currentTweets = tweetsIdListParsed.getTweetsIdList();
			relevantset.addAll(currentTweets);
			numtweets = relevantset.size();
			currentList = nextList;
			nextList = tweetsIdListParsed.getNextList();
		}
		
		SortedSet<Tweet> tm = new TreeSet<Tweet>();
		for(String tweetId: relevantset){
			String tweetReadStr;
			while((tweetReadStr=twitterHighLevelTaskHandler.readTweet(tweetId))==null);
			Tweet tweetRead = Tweet.parseTweet(tweetReadStr);
			//Only take the tweet if the user is followed
			System.out.println("Tweet read  " + tweetRead);
			System.out.println("Tweet userName  " + tweetRead.getUserName());
			System.out.println(userIfollow);
			System.out.println(tweetRead.getUserName() + " " + userIfollow.containsKey(tweetRead.getUserName()));
			
			if(userIfollow.containsKey(tweetRead.getUserName()) && userIfollow.get(tweetRead.getUserName()))
				tm.add(tweetRead);
		}
		
		LinkedList<Tweet> list = new LinkedList<Tweet>(tm);
		LinkedList<Tweet> subset;
		if(list.size()>CHUNCKSIZE && list.size()>0){
			subset = (LinkedList<Tweet>) list.subList(0, CHUNCKSIZE-1);
		}
		else if(list.size()>0)
			subset = list;
		else
			subset = new LinkedList<Tweet>();
		
		if(list.size()>0){
			firsttweetID = list.get(0).getTweetId();
			previouslydisplayedTweets = new LinkedList<String>();
			for(Tweet tweet: subset){
				previouslydisplayedTweets.add(tweet.getTweetId());
			}
		}
		return subset;
	}


	public LinkedList<Tweet> next() throws MissingDataException, parsingException{
		
		if (nextList == null){
			System.out.println("no more tweets");
			return null;
		}
		
		String line = "";
		TweetsIdSetList tweetsIdListParsed;
		
		LinkedList<String> relevantset = new LinkedList<String>();
		relevantset.addAll(currentTweets);
		
		//backward checking
		relevantset.addAll(previousTweets);
		relevantset.removeAll(previouslydisplayedTweets);
		
		//forward checking
		if (nextList != null){
			while((line = dhtHandler.readSet(nextList))==null);
			if(line.compareTo(dhtHandler.NOT_FOUND())==0)
				throw new MissingDataException("My tweets list not found");
			tweetsIdListParsed = TweetsIdSetList.parseTweetList(line);
			previousTweets = currentTweets;
			currentTweets = tweetsIdListParsed.getTweetsIdList();
			relevantset.addAll(currentTweets);
			numtweets = relevantset.size();
		}
		
		SortedSet<Tweet> tm = new TreeSet<Tweet>();
		for(String tweetId: relevantset){
			String tweetReadStr;
			while((tweetReadStr=twitterHighLevelTaskHandler.readTweet(tweetId))==null);
			Tweet tweetRead = Tweet.parseTweet(tweetReadStr);
			//Only take the tweet if the user is followed
			if(userIfollow.containsKey(tweetRead.getUserName()) && userIfollow.get(tweetRead.getUserName()))
				tm.add(tweetRead);
		}
		
		LinkedList<Tweet> list = new LinkedList<Tweet>(tm);
		LinkedList<Tweet> subset;
		if(list.size()>CHUNCKSIZE && list.size()>0){
			subset = (LinkedList<Tweet>) list.subList(0, CHUNCKSIZE-1);
		}
		else if(list.size()>0)
			subset = list;
		else
			subset = new LinkedList<Tweet>();
		
		if(list.size()>0){
			firsttweetID = list.get(0).getTweetId();
			previouslydisplayedTweets = new LinkedList<String>();
			for(Tweet tweet: subset){
				previouslydisplayedTweets.add(tweet.getTweetId());
			}
		}
		return subset;
	}
	
	
	public LinkedList<Tweet> refresh() throws MissingDataException, parsingException{
		
		if (firsttweetID == ""){
			return first();
		}
		
		String line = "";
		TweetsIdSetList tweetsIdListParsed;
		while((line = dhtHandler.readSet(startList))==null);
		if(line.compareTo(dhtHandler.NOT_FOUND())==0)
			throw new MissingDataException("My tweets list not found");
		tweetsIdListParsed = TweetsIdSetList.parseTweetList(line);
		
		LinkedList<String> relevantset = tweetsIdListParsed.getTweetsIdList();
			
		while(!relevantset.contains(firsttweetID) && tweetsIdListParsed.isNextList()){
			while((line = dhtHandler.readSet(nextList))==null);
			if(line.compareTo(dhtHandler.NOT_FOUND())==0)
				throw new MissingDataException("My tweets list not found");
			tweetsIdListParsed = TweetsIdSetList.parseTweetList(line);
			relevantset.addAll(tweetsIdListParsed.getTweetsIdList());
		}
		
		if (!relevantset.contains(firsttweetID) && !tweetsIdListParsed.isNextList())
			System.out.println("can't find the first tweet back");
		
		relevantset.removeAll(firstTweets);
	
		
		
		SortedSet<Tweet> tm = new TreeSet<Tweet>();
		for(String tweetId: relevantset){
			String tweetReadStr;
			while((tweetReadStr=twitterHighLevelTaskHandler.readTweet(tweetId))==null);
			Tweet tweetRead = Tweet.parseTweet(tweetReadStr);
			//Only take the tweet if the user is followed
			if(userIfollow.containsKey(tweetRead.getUserName()) && userIfollow.get(tweetRead.getUserName()))
				tm.add(tweetRead);
		}
				
		LinkedList<Tweet> list = new LinkedList<Tweet>(tm);
		
		if ( !relevantset.isEmpty()){
			firstTweets.addAll(relevantset);
			firsttweetID = list.getFirst().getTweetId();
		}
		
		
		return list;		
	}
	
	

}
