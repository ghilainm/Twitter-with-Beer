package TwitterApiImplementation;
import java.util.LinkedList;

public class TweetsIdSetList {
	private String idNextList;
	private LinkedList<String> tweetsIdList = new LinkedList<String>();
	//Liste des balises contenue dans l'element
	public final static String nextListBalise = "idNextList";
	public final static String tweetBalise = "tweet";
	public final static LinkedList<String> balisesList = new LinkedList<String>();
	private final String NO_NEXT_LIST = "null";
	
	public TweetsIdSetList(String idNextList) {
		if(idNextList.compareTo(NO_NEXT_LIST)==0){
			idNextList = null;
		}
		else
			this.idNextList = idNextList;
	}

	public TweetsIdSetList() {
		this.idNextList = null;
	}

	public static void initTweetsList(){
		balisesList.add(nextListBalise);
		balisesList.add(tweetBalise);
	}
	
	public String toString(){
		/* Format of a tweetlist : 
		 * idNextList
		 * <tweetBalise>tweetId</tweetBalise>
		 * <tweetBalise>tweetId</tweetBalise>
		 * ...
		 */
		String str = 
		"<"+nextListBalise+">"+idNextList+"</"+nextListBalise+">\n";
		for(String tweetId: tweetsIdList){
			str+="<"+tweetBalise+">"+tweetId+"</"+tweetBalise+">\n";
		}
		return str;
	}
	
	public static TweetsIdSetList parseTweetList(String strToParse) throws parsingException{
		int i = 0;
		//We do not test the tweet balise now because we do not now how much there will be
		//So we remove one from the size.
		int[] baliseValidityCheck = new int[(balisesList.size()-1)*2];
		for(String balise:balisesList){
			if(i>=baliseValidityCheck.length)
				break;
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
		
		TweetsIdSetList tweetsList = new TweetsIdSetList(strToParse.substring(baliseValidityCheck[0], baliseValidityCheck[1]));
		//Building the tweetIdList
		int indexOfTweetBalise = strToParse.indexOf("<"+tweetBalise+">");
		int tweetBaliseLength = ("<"+tweetBalise+">").length();
		int tweetBaliseEndLength = ("</"+tweetBalise+">").length();
		while(indexOfTweetBalise!=-1){
			strToParse =  strToParse.substring(indexOfTweetBalise+tweetBaliseLength);
			int tweetBaliseEnd = strToParse.indexOf("</"+tweetBalise+">");
			if(tweetBaliseEnd==-1)
				throw new parsingException("Missing balise </"+tweetBalise+">");
			tweetsList.tweetsIdList.add(strToParse.substring(0,tweetBaliseEnd));
			strToParse = strToParse.substring(tweetBaliseEnd+tweetBaliseEndLength);
			indexOfTweetBalise = strToParse.indexOf("<"+tweetBalise+">");
		}
		return tweetsList;
	}
	
	public LinkedList<String> getTweetsIdList(){
		return tweetsIdList;
	}
	
	public String getNextList(){
		return idNextList;
	}
	
	public boolean isNextList(){
		return idNextList != null;
	}
}
