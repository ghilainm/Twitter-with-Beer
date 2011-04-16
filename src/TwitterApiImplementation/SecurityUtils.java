package TwitterApiImplementation;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
	public static final String ENCODINGSET = "UTF8";
	
	//Retunr the secret used by a user to put a value that he does not
	//want to be rewritable by someone else in the dht.
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
}
