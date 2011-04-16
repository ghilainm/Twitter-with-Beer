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

public interface TwitterAPI extends java.rmi.Remote  {
	
	public LinkedList<String> allUsersFromLine(String lineName, String userName)throws java.rmi.RemoteException, ValueNotFoundException, NotLoggedException;
	
	public void createUser(String userName, String password, String realName) throws java.rmi.RemoteException, UserAlreadyUsedException, PassWordTooWeak, UserNameInvalid, UserNotFoundException, ValueNotFoundException;
	
	public void logIn(String userName, String password) throws java.rmi.RemoteException, ValueNotFoundException, UserNotFoundException, BadPasswordException;
	
	public LinkedList<User> moreUsers(String lineName, String userName) throws java.rmi.RemoteException, NotLoggedException;
		
	public void addUser(String lineName, String newFollowingUserName)throws java.rmi.RemoteException, UserNotFoundException, NotLoggedException;
	
	public void removeUser(String lineName, String followingUserName)throws java.rmi.RemoteException, UserNotFoundException, NotLoggedException;
	
	public LinkedList<Tweet> moreTweetsFromLine(String lineName, String userName)throws java.rmi.RemoteException, NotLoggedException;
	
	public LinkedList<Tweet> refreshTweetsFromLine(String lineName, String userName)throws java.rmi.RemoteException, NotLoggedException;
	
	public void postTweet(String msg)throws java.rmi.RemoteException, ValueNotFoundException, NotLoggedException, TweetEmptyException;
	
	public void reTweet(String tweetID)throws java.rmi.RemoteException, ValueNotFoundException, NotLoggedException;
	
	public void reply(String msg, String tweetID)throws java.rmi.RemoteException, ValueNotFoundException, BadSecretException, NotLoggedException, TweetEmptyException;
	
	public void createLine(String lineName)throws java.rmi.RemoteException, SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, ThisLineAlreadyExistException;
	
	public void deleteLine(String lineName)throws java.rmi.RemoteException, SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, UnauthorisedActionException, ThisListDoesNotExistsException;
	
	public void addTweetToList(String listName, String tweetID)throws java.rmi.RemoteException, NotLoggedException;
	
	public void removeTweetFromList(String listName, String tweetID)throws java.rmi.RemoteException, NotLoggedException;
	
	public boolean deleteAccount()throws java.rmi.RemoteException,NotLoggedException;
	
	public LinkedList<String> getLineNames()throws java.rmi.RemoteException, NotLoggedException;
	
	public String getUserName()throws java.rmi.RemoteException, NotLoggedException;
	
	public void deleteTweet(String tweetID) throws java.rmi.RemoteException, BadSecretException, NotLoggedException;
	
	public LinkedList<Tweet> allTweet(String lineName) throws NotLoggedException, ValueNotFoundException, java.rmi.RemoteException;

	public void disconnect() throws java.rmi.RemoteException,NotLoggedException;

	void createList(String listName) throws java.rmi.RemoteException, SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, ThisListAlreadyExistException;
	
	void deleteList(String listName) throws java.rmi.RemoteException, SecretProtectedException, BadSecretException, ValueNotFoundException, NotLoggedException, UnauthorisedActionException, ThisListDoesNotExistsException;

	LinkedList<Tweet> moreTweetsFromList(String listName) throws java.rmi.RemoteException, NotLoggedException;

	LinkedList<Tweet> refreshTweetsFromList(String listName) throws java.rmi.RemoteException, NotLoggedException;

	LinkedList<String> getListNames() throws java.rmi.RemoteException, NotLoggedException;
}
