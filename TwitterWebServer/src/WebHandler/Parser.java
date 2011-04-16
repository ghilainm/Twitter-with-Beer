package WebHandler;

import DHTExceptions.BadSecretException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;
import TwitterAPIExceptions.NotLoggedException;
import TwitterAPIExceptions.ThisLineAlreadyExistException;
import TwitterAPIExceptions.ThisListAlreadyExistException;
import TwitterAPIExceptions.TweetEmptyException;
import TwitterAPIExceptions.TwitterApiException;
import TwitterAPIExceptions.UserNotFoundException;
import TwitterApiImplementation.Translator;
import TwitterApiImplementation.TwitterAPI;


public final class Parser {

	public static final String logerror = "You need to be logged to execute this action";
	public static final String parsingerror = "Invalid request";
	private enum StrList
	{
		createUser,
		logIn,
		moreUsers,
		allUsersFromLine,
		addUser,
		removeUser,
		moreTweetsFromLine,
		refreshTweetsFromLine,
		postTweet,
		reTweet,
		reply,
		createLine,
		deleteLine,
		addTweetToList,
		removeTweetFromList,
		deleteAccount,
		getLineNames,
		getListNames,
		getUserName,
		deleteTweet,
		allTweet,
		disconnect,
		createList,
		deleteList,
		moreTweetsFromList,
		refreshTweetsFromList
	}

	public final static String handle(int numArgs, String idToResend, String[] args, String meth, TwitterAPI twitter, String sessionID){
		boolean failed = false;
		String error = null;
		String res = null;

		switch(StrList.valueOf(meth)){
		case allUsersFromLine:{
			try {
				if(args.length!=2){
					failed = true;
					error = parsingerror;
				}
				else
					res = Translator.ObjectToString(twitter.allUsersFromLine(args[0],args[1]));
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "This Line do not exist";
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
			}
			break;
		}
		case createUser:
			try {
				if(args.length!=3){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.createUser(args[0], args[1], args[2]);
			} catch (TwitterApiException e) {
				failed = true;
				error = e.getMessage();
			} catch (ValueNotFoundException e) {
				failed = true;
				error = e.getMessage();
			}
			break;
		case logIn: 	
			try {
				if(args.length!=2){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.logIn(args[0], args[1]);
			}catch (ValueNotFoundException e) {
				failed = true;
				error = "Can't log in";
			}catch (TwitterApiException e) {
				failed = true;
				error = e.getMessage();
			}
			break;
		case moreUsers: 
			try {
				if(args.length!=2){
					failed = true;
					error = parsingerror;
				}
				else
					res = Translator.ObjectToString(twitter.moreUsers(args[0],args[1]));
			}  catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
			break;
		case addUser: 
			try {
				if(args.length!=2){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.addUser(args[0], args[1]);
			}  catch (UserNotFoundException e) {
				failed = true;
				error ="The user you are trying add doesn't exist";
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
			break;
		case removeUser:  
			try {
				if(args.length!=2){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.removeUser(args[0],args[1]);
			} catch (UserNotFoundException e) {
				failed = true;
				error ="Can't find the user you are trying to remove.";
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error =logerror;
				e.printStackTrace();
			}
			break;
		case moreTweetsFromLine: 
			try {
				if(args.length!=2){
					failed = true;
					error = parsingerror;
				}
				else
					res = Translator.ObjectToString(twitter.moreTweetsFromLine(args[0],args[1]));
			}  catch (NotLoggedException e) {
				failed = true;
				error =logerror;
				e.printStackTrace();
			}

			break;

		case refreshTweetsFromLine: 
			try {
				if(args.length!=2){
					failed = true;
					error = parsingerror;
				}
				else
					res = Translator.ObjectToString(twitter.refreshTweetsFromLine(args[0],args[1]));
			} catch (NotLoggedException e) {
				failed = true;
				error =logerror;
				e.printStackTrace();
			}
			break;
		case postTweet:
			try {
				if(args.length!=1){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.postTweet(args[0]);
			}catch (NotLoggedException e) {
				failed = true;
				error =logerror;
				e.printStackTrace();
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "A critial value was not found to post your tweet";
				e.printStackTrace();
			} catch (TweetEmptyException e) {
				error = e.getMessage();
				failed = true;
			}
			break;
		case reTweet:
			try {
				if(args.length!=1){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.reTweet(args[0]);
			} catch (NotLoggedException e) {
				failed = true;
				error =logerror;
				e.printStackTrace();
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "A critial value was not found to post your retweet";
				e.printStackTrace();
			}

			break;
		case reply:  			
			try {
				if(args.length!=2){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.reply(args[0], args[1]);
			} catch (NotLoggedException e) {
				failed = true;
				error =logerror;
				e.printStackTrace();
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "A critial value was not found to post your reply tweet";
				e.printStackTrace();
			} catch (BadSecretException e) {
				failed = true;
				error = "Can't reply to this tweet";
				e.printStackTrace();
			} catch (TweetEmptyException e) {
				failed = true;
				error = e.getMessage();
			}
			break;

		case createLine:
			try {
				if(args.length!=1){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.createLine(args[0]);
			} catch (NotLoggedException e) {
				failed = true;
				error =logerror;
				e.printStackTrace();
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "A critial value was not found to create your line";
				e.printStackTrace();
			} catch (BadSecretException e) {
				failed = true;
				error = "Can't create this line, you have a bad secret";
				e.printStackTrace();
			} catch (SecretProtectedException e) {
				failed = true;
				error = "Can't create this line, its emplacement is protected by a secret";
				e.printStackTrace();
			} catch (ThisLineAlreadyExistException e) {
				failed = true;
				error = "This line already Exists";
				e.printStackTrace();
			}

			break;
		case deleteLine:
			try {
				if(args.length!=1){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.deleteLine(args[0]);
			}catch (ValueNotFoundException e) {
				failed = true;
				error = "A critial value was not found to create your line";
				e.printStackTrace();
			} catch (BadSecretException e) {
				failed = true;
				error = "Can't delete this line, you have a bad secret";
				e.printStackTrace();
			} catch (SecretProtectedException e) {
				failed = true;
				error = "Can't delete this line, its emplacement is protected by a secret";
				e.printStackTrace();
			} catch (TwitterApiException e) {
				failed = true;
				error = e.getMessage();
				e.printStackTrace();
			}
			break;

		case addTweetToList:  
			try {
				if(args.length!=2){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.addTweetToList(args[0], args[1]);
			}catch (TwitterApiException e) {
				failed = true;
				error = e.getMessage();
			}

			break;

		case removeTweetFromList:
			try {
				if(args.length!=2){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.removeTweetFromList(args[0], args[1]);
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
			break;
		case deleteAccount:
			try {
				//TODO
				if(args.length!=1){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.deleteAccount();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}			

			break;
		case getLineNames: 
			try {
				if(args.length!=0){
					failed = true;
					error = parsingerror;
				}
				else
					res = Translator.ObjectToString(twitter.getLineNames());
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}	
			break;
		case getListNames: 
			try {
				if(args.length!=0){
					failed = true;
					error = parsingerror;
				}
				else
					res = Translator.ObjectToString(twitter.getListNames());
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}	
			break;
		case getUserName:
			try {
				if(args.length!=0){
					failed = true;
					error = parsingerror;
				}
				else
					res = Translator.ObjectToString(twitter.getUserName());
			}  catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}

			break;
		case deleteTweet:  
			try {
				if(args.length!=1){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.deleteTweet(args[0]);
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			} catch (BadSecretException e) {
				failed = true;
				error = "You don't have the rights to delete this tweet";
				e.printStackTrace();
			}


			break;
		case allTweet:  
			try {
				if(args.length!=1){
					failed = true;
					error = parsingerror;
				}
				else
					res = Translator.ObjectToString(twitter.allTweet(args[0]));
			} catch (TwitterApiException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "Couldn't retrieve the tweets";
				e.printStackTrace();
			}
			break;
		case disconnect: 
			try {
				if(args.length!=0){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.disconnect();
			}  catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
			break;
		case createList:  
			try {
				if(args.length!=1){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.createList(args[0]);
			}  catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "A critial value was not found to create your list";
				e.printStackTrace();
			} catch (BadSecretException e) {
				failed = true;
				error = "Can't create this list, you have a bad secret";
				e.printStackTrace();
			} catch (SecretProtectedException e) {
				failed = true;
				error = "Can't create this list, its emplacement is protected by a secret";
				e.printStackTrace();
			} catch (ThisListAlreadyExistException e) {
				failed = true;
				error = "This line already Exists";
				e.printStackTrace();
			}

			break;
		case deleteList:  
			try {
				if(args.length!=1){
					failed = true;
					error = parsingerror;
				}
				else
					twitter.deleteList(args[0]);
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "A critial value was not found to create your list";
				e.printStackTrace();
			} catch (BadSecretException e) {
				failed = true;
				error = "Can't create this list, you have a bad secret";
				e.printStackTrace();
			} catch (SecretProtectedException e) {
				failed = true;
				error = "Can't create this list, its emplacement is protected by a secret";
				e.printStackTrace();
			} catch (TwitterApiException e) {
				failed = true;
				error = e.getMessage();
				e.printStackTrace();
			}

			break;
		case moreTweetsFromList:  
			try {
				if(args.length!=1){
					failed = true;
					error = parsingerror;
				}
				else
					res = Translator.ObjectToString(twitter.moreTweetsFromList(args[0]));
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
		case refreshTweetsFromList:  
			try {
				if(args.length!=1){
					failed = true;
					error = parsingerror;
				}
				else
					res = Translator.ObjectToString(twitter.refreshTweetsFromList(args[0]));
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
			break;
		default :{
				failed = true;
				error = parsingerror;
			}
		}
		return makeresult(idToResend, res , meth, failed, error, sessionID);  
	}

	protected static String makeresult(String idToResend, String result, String method, boolean failed, String error, String sessionID){
		return "<?xml version=\"1.0\"?>\n \n<requestResult>" + 
		"<idToResend>" + idToResend + "</idToResend>" +
		"<result>" + result + "</result>" +
		"<meth>" +  method + "</meth>" +
		"<failed>" + failed + "</failed>" +
		"<error>" + error + "</error>" +
		"<"+TwitterApiServlet.twitterSessionID+">"+ sessionID + "</"+TwitterApiServlet.twitterSessionID+">"+
		"</requestResult>";
	}
}
