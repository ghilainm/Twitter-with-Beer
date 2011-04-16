package TwitterApiImplementation;

import java.util.LinkedList;

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

import DHTExceptions.BadSecretException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;

public interface TwitterAPI {
	
	public LinkedList<String> allUsersFromLine(String lineName, String userName)throws  ValueNotFoundException, NotLoggedException;
	
	public void createUser(String userName, String password, String realName) throws  UserAlreadyUsedException, PassWordTooWeak, UserNameInvalid, UserNotFoundException, ValueNotFoundException;
	
	public void logIn(String userName, String password) throws  ValueNotFoundException, UserNotFoundException, BadPasswordException;
	
	public LinkedList<User> moreUsers(String lineName, String userName) throws  NotLoggedException;
		
	public void addUser(String lineName, String newFollowingUserName)throws  UserNotFoundException, NotLoggedException;
	
	public void removeUser(String lineName, String followingUserName)throws  UserNotFoundException, NotLoggedException;
	
	public LinkedList<Tweet> moreTweetsFromLine(String lineName, String userName)throws  NotLoggedException;
	
	public LinkedList<Tweet> refreshTweetsFromLine(String lineName, String userName)throws  NotLoggedException;
	
	public void postTweet(String msg)throws  ValueNotFoundException, NotLoggedException, TweetEmptyException;
	
	public void reTweet(String tweetID)throws  ValueNotFoundException, NotLoggedException;
	
	public void reply(String msg, String tweetID)throws  ValueNotFoundException, BadSecretException, NotLoggedException, TweetEmptyException;
	
	public void createLine(String lineName)throws  SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, ThisLineAlreadyExistException;
	
	public void deleteLine(String lineName)throws  SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, UnauthorisedActionException, ThisListDoesNotExistsException;
	
	public void addTweetToList(String listName, String tweetID)throws  NotLoggedException;
	
	public void removeTweetFromList(String listName, String tweetID)throws  NotLoggedException;
	
	public boolean deleteAccount()throws NotLoggedException;
	
	public LinkedList<String> getLineNames()throws  NotLoggedException;
	
	public String getUserName()throws  NotLoggedException;
	
	public void deleteTweet(String tweetID) throws  BadSecretException, NotLoggedException;
	
	public LinkedList<Tweet> allTweet(String lineName) throws NotLoggedException, ValueNotFoundException;

	public void disconnect() throws NotLoggedException;

	void createList(String listName) throws  SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, ThisListAlreadyExistException;
	
	void deleteList(String listName) throws  SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, UnauthorisedActionException, ThisListDoesNotExistsException;

	LinkedList<Tweet> moreTweetsFromList(String listName) throws  NotLoggedException;

	LinkedList<Tweet> refreshTweetsFromList(String listName) throws  NotLoggedException;

	LinkedList<String> getListNames() throws  NotLoggedException;
}
