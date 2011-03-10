package TwitterApiImplementation;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

public class Tweet implements Comparable<Tweet> {

	private String date;
	private String tweetId;
	private String msg;
	private String userName;
	
	//Liste des balises contenue dans l'element
	public final static String idBalise = "id";
	public final static String dateBalise = "date";
	public final static String msgBalise = "msg";
	public final static String userBalise = "user";
	public final static LinkedList<String> balisesList = new LinkedList<String>();
	
	public static void initTweetClass(){
		balisesList.add(dateBalise);
		balisesList.add(idBalise);
		balisesList.add(msgBalise);
		balisesList.add(userBalise);
	}
	
	public Tweet(String msg, String user){
		if(msg.length()>140)
			this.msg = new String(msg.substring(0, 139));
		else
			this.msg = new String(msg);
		tweetId = UUID.randomUUID().toString();
		date = new StandardDate().toString();
		this.userName = user;
	}
	
	public Tweet(String date, String id, String msg, String user){
		if(msg.length()>140)
			this.msg = new String(msg.substring(0, 139));
		else
			this.msg = new String(msg);
		tweetId = id;
		this.date = date;
		this.userName = user;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public String getTweetId(){
		return tweetId;
	}
	
	public String getDate(){
		return date;
	}
	
	public String toString(){
		/* Format d'un Tweet : 
		 * Date
		 * Id
		 * msg
		 * user
		 */
		return "<"+dateBalise+">"+date+"</"+dateBalise+">\n"+"<"+idBalise+">"+tweetId+"</"+idBalise+">\n"
		+"<"+msgBalise+">"+msg+"</"+msgBalise+">\n"+"<"+userBalise+">"+userName+"</"+userBalise+">\n";
	}
	
	private boolean isValideBalise(String str){
		for(String balise : balisesList)
			if(balise.compareTo(str)==0)return true;
		return false;
	}
	
	public static Tweet parseTweet(String strToParse) throws parsingException{
		int i = 0;
		int[] baliseValidityCheck = new int[balisesList.size()*2];
		for(String balise:balisesList){
			baliseValidityCheck[i] = strToParse.indexOf("<"+balise+">");
			if(baliseValidityCheck[i]==-1)throw new parsingException("missing: <"+balise+">");
			baliseValidityCheck[i]=baliseValidityCheck[i]+2+balise.length();
			i++;
			baliseValidityCheck[i] = strToParse.indexOf("</"+balise+">");
			if(baliseValidityCheck[i]==-1)throw new parsingException("missing: </"+balise+">");
			i++;
		}
		
		for(int index = 0; index<baliseValidityCheck.length; index = index + 2){
			//Test to verify that no balise is included into another
			if(baliseValidityCheck[index+1] < baliseValidityCheck[index])throw new parsingException("Balise: <"+balisesList.get(index)+"> and Balise :</"+balisesList.get(index)+">, in wrong order");
		}
		return new Tweet(strToParse.substring(baliseValidityCheck[0],baliseValidityCheck[1]),
				strToParse.substring(baliseValidityCheck[2],baliseValidityCheck[3]),
				strToParse.substring(baliseValidityCheck[4],baliseValidityCheck[5]),
				strToParse.substring(baliseValidityCheck[6],baliseValidityCheck[7]));
	}

	public String getUserName() {
		return userName; 
	}

	@Override
	public int compareTo(Tweet arg0) {

		final String hash1;
		final String hash2;
		MessageDigest md;
		
		Date o1 = null;
		Date o2 = null;
		try {
			o1 = new StandardDate(getDate()).toDate();
			o2 = new StandardDate(arg0.getDate()).toDate();
		} catch (parsingException e) {
			e.printStackTrace();
		}
		if(o1.before(o2))
			return 1;
		else if(o1.after(o2))
			return -1;
		else 
			try {
				md = MessageDigest.getInstance("SHA");
			    hash1 = md.digest((toString()).getBytes()).toString();
			    hash2 = md.digest((arg0.toString()).getBytes()).toString();
			    return hash1.compareTo(hash2);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		return 0;	
	}
	
	
}
