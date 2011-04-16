package TalkingFlex;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.util.Date;

import DHTExceptions.BadSecretException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;
import TwitterAPIExceptions.BadPasswordException;
import TwitterAPIExceptions.NotLoggedException;
import TwitterAPIExceptions.PassWordTooWeak;
import TwitterAPIExceptions.ThisLineAlreadyExistException;
import TwitterAPIExceptions.ThisListAlreadyExistException;
import TwitterAPIExceptions.TweetEmptyException;
import TwitterAPIExceptions.UnauthorisedActionException;
import TwitterAPIExceptions.UserAlreadyUsedException;
import TwitterAPIExceptions.UserNameInvalid;
import TwitterAPIExceptions.UserNotFoundException;
import TwitterApiImplementation.Translator;
import TwitterApiImplementation.TwitterAPI;


public class Parser {

	static final String logerror = "You need to be logged to execute this action";
	static final String remoteerror = "There was an error during the remote access";
	static final String pargingerror = "Parsing error, invalid request";

	TwitterAPI twitter;
	
	public Parser(TwitterAPI twitter){
		this.twitter = twitter;
	}


	public enum StrList
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

	public String handle(String text){
		//If the client ask for the XML security file
		Date d = new Date();
		String curDate = HtmlDate.formatDate(d);
		if(text.contains("GET /crossdomain.xml")){
			return " HTTP/1.1 200 OK \n Date: "+curDate+" \n Content-Type: application/xml \n \n <?xml version=\"1.0\"?>\n"+
	    "<!DOCTYPE cross-domain-policy SYSTEM \"http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd\">\n"+
	   "<cross-domain-policy>\n"+
	    "<allow-access-from domain=\"*\" />\n"+	
	   "</cross-domain-policy>\n \n";
		}
		int numArgs = 0;
		String idToResend = null;
		String[] args = null;
		String meth = null;
		try {
			meth = URLDecoder.decode(getvalue(text, "meth"),"UTF-8");
			idToResend = URLDecoder.decode(getvalue(text,"idToResend"),"UTF-8");
			numArgs =  Integer.parseInt(URLDecoder.decode(getvalue(text, "nbrArgs"),"UTF-8"));
			if (numArgs>0){
				args = new String[numArgs];
				for(int i = 0; i < numArgs; i++){
					args[i] = getvalue(text, "arg"+i);	
					args[i] = URLDecoder.decode(args[i],"UTF-8");

				}
			}
		} catch (ParsingException e1) {
			return makeresult(null, null , null, true, pargingerror);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		boolean failed = false;
		String error = null;
		String res = null;

		switch(StrList.valueOf(meth)){
		case allUsersFromLine:{
			try {
				res = Translator.ObjectToString(twitter.allUsersFromLine(args[0],args[1]));
			} catch (RemoteException e) {
				failed = true;
				error = remoteerror;
				e.printStackTrace();
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "This Line do not exist";
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
			break;
		}
		case createUser:
			try {
				twitter.createUser(args[0], args[1], args[2]);
			} catch (RemoteException e) {
				failed = true;
				error = remoteerror;
			} catch (UserAlreadyUsedException e) {
				failed = true;
				error = "User name already used";
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "Can't create user";
			} catch (PassWordTooWeak e) {
				failed = true;
				error = e.getMessage();
			} catch (UserNameInvalid e) {
				failed = true;
				error = e.getMessage();
			}
			break;
		case logIn: 	
			try {
				twitter.logIn(args[0], args[1]);
			} catch (RemoteException e) {
				failed = true;
				error = remoteerror;
				e.printStackTrace();
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "Can't log in";
				e.printStackTrace();
			} catch (UserNotFoundException e) {
				failed = true;
				error = "No user registered with this name";
				e.printStackTrace();
			} catch (BadPasswordException e) {
				failed = true;
				error = "Incorrect password";
				e.printStackTrace();
			}
			break;
		case moreUsers: 
			try {
				res = Translator.ObjectToString(twitter.moreUsers(args[0],args[1]));
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
			break;
		case addUser: 
			try {
				twitter.addUser(args[0], args[1]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (UserNotFoundException e) {
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
				twitter.removeUser(args[0],args[1]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
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
				res = Translator.ObjectToString(twitter.moreTweetsFromLine(args[0],args[1]));
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error =logerror;
				e.printStackTrace();
			}

			break;

		case refreshTweetsFromLine: 
			try {
				res = Translator.ObjectToString(twitter.refreshTweetsFromLine(args[0],args[1]));
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error =logerror;
				e.printStackTrace();
			}
			break;
		case postTweet:
			try {
				twitter.postTweet(args[0]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error =logerror;
				e.printStackTrace();
			} catch (ValueNotFoundException e) {
				failed = true;
				error = "A critial value was not found to post your tweet";
				e.printStackTrace();
			} 
			break;
		case reTweet:
			try {
				twitter.reTweet(args[0]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
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
				twitter.reply(args[0], args[1]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
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
			}
			break;

		case createLine:
			try {
				twitter.createLine(args[0]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
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
				twitter.deleteLine(args[0]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
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
				error = "Can't delete this line, you have a bad secret";
				e.printStackTrace();
			} catch (SecretProtectedException e) {
				failed = true;
				error = "Can't delete this line, its emplacement is protected by a secret";
				e.printStackTrace();
			} catch (UnauthorisedActionException e) {
				failed = true;
				error = "Can't delete this default line";
				e.printStackTrace();
			}

			break;

		case addTweetToList:  
			try {
				twitter.addTweetToList(args[0], args[1]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}

			break;

		case removeTweetFromList:
			try {
				twitter.removeTweetFromList(args[0], args[1]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
			break;
		case deleteAccount:
			try {
				twitter.deleteAccount();
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}			

			break;
		case getLineNames: 
			try {
				res = Translator.ObjectToString(twitter.getLineNames());
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}	
			break;
		case getListNames: 
			try {
				res = Translator.ObjectToString(twitter.getListNames());
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}	
			break;
		case getUserName:
			try {
				res = Translator.ObjectToString(twitter.getUserName());
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}

			break;
		case deleteTweet:  
			try {
				twitter.deleteTweet(args[0]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
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
				res = Translator.ObjectToString(twitter.allTweet(args[0]));
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
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
				twitter.disconnect();
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
			break;
		case createList:  
			try {
				twitter.createList(args[0]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
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
				twitter.deleteList(args[0]);
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
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
			} catch (UnauthorisedActionException e) {
				failed = true;
				error = "Can't delete this default list";
				e.printStackTrace();
			}

			break;
		case moreTweetsFromList:  
			try {
				res = Translator.ObjectToString(twitter.moreTweetsFromList(args[0]));
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
		case refreshTweetsFromList:  
			try {
				res = Translator.ObjectToString(twitter.refreshTweetsFromList(args[0]));
			} catch (RemoteException e) {
				failed = true;
				error =remoteerror;
				e.printStackTrace();
			} catch (NotLoggedException e) {
				failed = true;
				error = logerror;
				e.printStackTrace();
			}
			break;
		}

		return makeresult(idToResend, res , meth, failed, error);  

	}

	private String getvalue(String text, String field) throws ParsingException{

		int fieldStart = text.indexOf(field);
		int fieldEnd = text.indexOf('&', fieldStart);
		if(fieldEnd == -1){
			fieldEnd = text.indexOf(' ', fieldStart);
		}
		if(fieldEnd < fieldStart + field.length() + 1)
			throw new ParsingException();
		return text.substring((fieldStart + field.length() + 1), fieldEnd);
	}


	private String makeresult(String idToResend, String result, String method, boolean failed, String error){
		return "<requestResult>" + 
		"<idToResend>" + idToResend + "</idToResend>" +
		"<result>" + result + "</result>" +
		"<meth>" +  method + "</meth>" +
		"<failed>" + failed + "</failed>" +
		"<error>" + error + "</error>" +
		"</requestResult>";
	}



	public static void main(String[] args){

		String input = 	"/?nbrArgs=1&appid=TwitterAPI&arg1=timeLine&meth=moreTweetsFromLine HTTP/1.1 \n" +
		"Host: localhost:600 \n" +
		"Connection: keep-alive \n" +
		"Accept: */* \n"+
		"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.20 (KHTML, like Gecko) Chrome/11.0.672.2 Safari/534.20 \n"+
		"Accept-Encoding: gzip,deflate,sdch \n"+
		"Accept-Language: en-US,en;q=0.8 \n"+
		"Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.3 \n"	;

		System.out.println(input);

		Parser parser = new Parser(null);
		parser.handle(input);

	}
}
