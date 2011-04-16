package TwitterApiImplementation;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TwitterAPIExceptions.PassWordTooWeak;
import TwitterAPIExceptions.TweetEmptyException;
import TwitterAPIExceptions.UserNameInvalid;

public class SecurityUtils {
	public static final String ENCODINGSET = "UTF8";
	public static final int MINPASSWORDLENGTH = 8;
	private static final String regExp = "(\\W+\\d+\\w+)|(\\W+\\w+\\d+)|(\\d+\\W+\\w+)|(\\d+\\w+\\W+)|(\\w+\\W+\\d+)|(\\w+\\d+\\W+)"; 
	private static final Pattern p = Pattern.compile(regExp);
	private static final String regExpRealName = "\\w+\\s+\\w+";
	private static final Pattern pRealName = Pattern.compile(regExpRealName);
	private static final String regExpTweet = "\\W+|\\w+||\\d";
	private static final Pattern pTweet = Pattern.compile(regExpTweet);
	
	public static String getUserSecret(String userName, String password) {
		String keyDigest = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte[] b = md.digest((userName+password).getBytes());
			keyDigest = SecurityUtils.getStringFromBytes(b);
		} catch (NoSuchAlgorithmException e) {
			System.exit(-1);
			e.printStackTrace();
		}
		return keyDigest;
	}
	
	public static byte[] getBytesFromString(String str){
		try {
			return str.getBytes(ENCODINGSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getStringFromBytes(byte [] bA){
		try {
			return new String(bA, ENCODINGSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String Hash(String data){
		String keyDigest = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte[] b = md.digest(data.getBytes());
			keyDigest = SecurityUtils.getStringFromBytes(b);
		} catch (NoSuchAlgorithmException e) {
			System.exit(-1);
		}
		return keyDigest;
	}

	public static boolean verifyPassword(String passwordHash, String password) {
		return passwordHash.compareTo(getSecurePassword(password))==0;
	}

	public static String getSecurePassword(String password) {
		return Hash(password);
	}
	
	public static void isPassWordStrongEnough(String password) throws PassWordTooWeak{
		Matcher m = p.matcher(password);
		if(password.length()<MINPASSWORDLENGTH || !m.find())
			throw new PassWordTooWeak("Password must be at least "+MINPASSWORDLENGTH+" characters long and contain\n a number, a special characeter and a normal character");
	}

	public static void verifyUsername(String userName) throws UserNameInvalid {
		if(userName.contains(" "))
			throw new UserNameInvalid("Username can't contain spaces "+userName);
	}

	public static void verifyRealName(String realName) throws UserNameInvalid {
		Matcher m = pRealName.matcher(realName);
		if(!m.find())
			throw new UserNameInvalid("A real name must contain a first and last name "+realName);
	}

	public static void isTweetValid(String tweetMsg) throws TweetEmptyException {
		Matcher m = pTweet.matcher(tweetMsg);
		if(!m.find())
			throw new TweetEmptyException("A tweet can't be empty");
	}
}
