package TwitterApiImplementation;

import java.util.LinkedList;

import DHT.DhtAPI;
import DHT.DhtUtils;
import DHT.DhtUtilsAPI;
import DHTExceptions.BadSecretException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;
import TwitterAPIExceptions.BadPasswordException;
import TwitterAPIExceptions.NotLoggedException;
import TwitterAPIExceptions.PassWordTooWeak;
import TwitterAPIExceptions.ThisLineAlreadyExistException;
import TwitterAPIExceptions.ThisListAlreadyExistException;
import TwitterAPIExceptions.ThisListDoesNotExistsException;
import TwitterAPIExceptions.TweetEmptyException;
import TwitterAPIExceptions.UnauthorisedActionException;
import TwitterAPIExceptions.UserAlreadyUsedException;
import TwitterAPIExceptions.UserNameInvalid;
import TwitterAPIExceptions.UserNotFoundException;

public class TwitterImplementation implements TwitterAPI{

	private static final long serialVersionUID = -6101667408026933141L;

	private User loggedUser;
	private Handler handler;
	private DhtUtilsAPI dhtUtils;
	private boolean verbose = false;

	public TwitterImplementation(DhtAPI dhtAPI){
		dhtUtils = new DhtUtils(dhtAPI);
		handler = new Handler(dhtUtils);
	}

	/*
	public String userKey(String userName, String realName){
		return "user"+userName+realName;
	}
	*/

	@Override
	public void createUser(String userName, String password, String realName) throws UserAlreadyUsedException, PassWordTooWeak, UserNameInvalid, UserNotFoundException, ValueNotFoundException {
		//TODO mody the error handling
		try {
			handler.createUser(userName, password, realName);
		} catch (SecretProtectedException e) {
			throw new UserAlreadyUsedException();
		} catch (BadSecretException e) {
			throw new UserAlreadyUsedException();
		}
		try {
			handler.logIn(userName,password);
		}catch (BadPasswordException e) {
			System.out.println("Bad pass while creating user this should never happen");
			e.printStackTrace();
		} 
		loggedUser = handler.getUser();
	}

	@Override
	public void logIn(String userName, String password) throws ValueNotFoundException, UserNotFoundException, BadPasswordException {
		handler.logIn(userName,password);
		loggedUser = handler.getUser();
	}

	// OPERATIONS ON LINES

	@Override
	public void createLine(String lineName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, ThisLineAlreadyExistException {
		handler.createLine(lineName);
	}

	@Override
	public void deleteLine(String lineName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException,UnauthorisedActionException, ThisListDoesNotExistsException {
		if (lineName==null || lineName.equals(Constants.getTimeLineName()) || lineName.equals(Constants.getUserLineName())){
			throw new UnauthorisedActionException();
		}
		handler.deleteLine(lineName);
	}

	@Override
	public LinkedList<User> moreUsers(String lineName, String userName) throws NotLoggedException {
		LinkedList<User> res = null;
		try {
			res = handler.moreUsers(lineName,userName);
		} catch (ValueNotFoundException e) {
			res = null;
		}
		return res;
	}

	@Override
	public void addUser(String lineName, String newFollowingUserName) throws UserNotFoundException {
		try {
			handler.addFollowing(lineName, newFollowingUserName);
		} catch (ValueNotFoundException e) {
			throw new UserNotFoundException();
		} catch (SecretProtectedException e) {
			System.out.println("Should never happen");
		} catch (BadSecretException e) {
			System.out.println("Should never happen");
		}
	}

	@Override
	public void removeUser(String lineName, String followingUserName) throws UserNotFoundException, NotLoggedException {
		try {
			handler.removeFollowing(lineName, followingUserName);
		} catch (SecretProtectedException e) {
			System.out.println("This should not happen...");
			e.printStackTrace();
		} catch (ValueNotFoundException e) {
			throw new UserNotFoundException();
		} catch (BadSecretException e) {
			System.out.println("This should not happen...");
			e.printStackTrace();
		}
	}

	@Override
	public LinkedList<Tweet> moreTweetsFromLine(String lineName, String userName) throws NotLoggedException {
		LinkedList<Tweet> res = null;
		try {
			res = handler.moreTweetsFromLine(lineName,userName);
		} catch (ValueNotFoundException e) {
			res = null;
		}
		return res;
	}

	@Override
	public LinkedList<Tweet> refreshTweetsFromLine(String lineName, String userName) throws NotLoggedException {
		LinkedList<Tweet> res = null;
		try {
			res = handler.refreshTweetsFromLine(lineName,userName);
		} catch (ValueNotFoundException e) {
			res = null;
		}
		return res;		
	}

	// OPERATIONS ON LISTS

	@Override
	public void createList(String listName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, ThisListAlreadyExistException {
		handler.createList(listName);
	}

	public void deleteList(String listName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, UnauthorisedActionException, ThisListDoesNotExistsException {
		if (listName.equals(Constants.getFavListName())){
			throw new UnauthorisedActionException();}
		handler.deleteList(listName);
	}

	@Override
	public void addTweetToList(String listName, String tweetID) {
		try {
			handler.addTweetToList(handler.getList(listName), tweetID);
		} catch (ValueNotFoundException e) {
		} catch (BadSecretException e) {
			System.out.println("This should never happen");
		} catch (NotLoggedException e) {
			System.out.println("This should never happen");
		}
	}

	@Override
	public void removeTweetFromList(String listName, String tweetID) {
		try {
			handler.removeTweetFromList(handler.getList(listName), tweetID);
		} catch (ValueNotFoundException e) {
		} catch (BadSecretException e) {
		} catch (NotLoggedException e) {
			e.printStackTrace();
		}
	}


	@Override
	public LinkedList<Tweet> moreTweetsFromList(String listName) throws NotLoggedException {
		LinkedList<Tweet> res = null;
		try {
			res = handler.moreTweetsFromList(listName);
		} catch (ValueNotFoundException e) {
			res = null;
		}
		return res;
	}

	@Override
	public LinkedList<Tweet> refreshTweetsFromList(String listName) throws NotLoggedException {
		LinkedList<Tweet> res = null;
		try {
			res = handler.refreshTweetsFromList(listName);
		} catch (ValueNotFoundException e) {
			res = null;
		}
		return res;		
	}	



	// OPERATIONS ON TWEETS	

	@Override
	public void postTweet(String msg) throws NotLoggedException, TweetEmptyException {
		SecurityUtils.isTweetValid(msg);
		try {
			handler.postTweet(msg, null);
			if(verbose)System.out.println(getUserName()+ " posted a tweet");
		} catch (ValueNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reTweet(String tweetID) throws ValueNotFoundException, NotLoggedException {
		handler.reTweet(tweetID);
	}

	@Override
	public void reply(String msg, String tweetID) throws ValueNotFoundException, BadSecretException, NotLoggedException, TweetEmptyException {
		SecurityUtils.isTweetValid(msg);
		handler.reply(msg, tweetID);
	}


	@Override
	public boolean deleteAccount() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LinkedList<String> getLineNames() throws NotLoggedException {
		return new LinkedList<String>(handler.getLineNames());
	}
	
	@Override
	public LinkedList<String> getListNames() throws NotLoggedException {
		return new LinkedList<String>(handler.getListNames());
	}

	@Override
	public String getUserName() {
		return loggedUser.getUserName();
	}

	@Override
	public void deleteTweet(String tweetID) throws BadSecretException, NotLoggedException {
		handler.deleteTweet(tweetID);
	}

	public LinkedList<Tweet> allTweet(String lineName) throws NotLoggedException, ValueNotFoundException{
		return handler.allTweet(lineName);
	}

	@Override
	public void disconnect() {
		handler.disconnect();
		handler = null;
		loggedUser = null;
	}

	@Override
	public LinkedList<String> allUsersFromLine(String lineName, String userName) throws  ValueNotFoundException, NotLoggedException {
		handler.isLogged();
		LinkedList<String> ret = new LinkedList<String>();
		if (loggedUser.getUserName().equals(userName)){
			for(UserReference r : handler.lineset.lineStore.get(lineName).getAllUserReferences()){
				ret.add(r.getUserName());
			}
		}
		else{
			for(UserReference r : handler.foreignset.getLine(lineName, userName).getAllUserReferences()){
				ret.add(r.getUserName());
			}
		}
		return ret;
	}
}
