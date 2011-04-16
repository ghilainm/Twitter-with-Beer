package TwitterApiImplementation;

import java.text.ParseException;
import java.util.Date;

import DHT.DhtUtilsAPI;
import DHTExceptions.ValueNotFoundException;

public class TweetReference implements comparableItem{
	private String id;
	private StandardDate date;
	private String posterName;
	private boolean isReTweet;
	
	public int hashCode(){
		return id.hashCode()+posterName.hashCode();
	}
	public String getReference() {
		return id;
	}

	public void setReference(String reference) {
		this.id = reference;
	}

	public StandardDate getDate() {
		return date;
	}

	public void setDate(StandardDate date) {
		this.date = date;
	}
	
	public String getPosterName() {
		return posterName;
	}
	
	public void setPosterName(String posterName) {
		this.posterName = posterName;

	}
	
	
	
	public TweetReference(String tweetID, StandardDate date, String posterName, boolean isReTweet) {
		this.id = tweetID;
		this.date = date;
		this.posterName = posterName;
		this.setReTweet(isReTweet);
	}
	
	
	public int compareTo(Object arg0) {
		Date o1 = null;
		Date o2 = null;
		TweetReference tR = (TweetReference)arg0;
		try {
			o1 = date.toDate();
			o2 = tR.getDate().toDate();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(o1.before(o2))
			return 1;
		else if(o1.after(o2))
			return -1;
		else if(tR.isReTweet==isReTweet && tR.posterName.compareTo(posterName)==0 && tR.id.compareTo(id)==0)
			return 0;
		else
			return id.compareTo(tR.id);
	}


	public Tweet getItem(DhtUtilsAPI dhtHandler) throws ValueNotFoundException {
		String str = dhtHandler.getDht().read(id);
		Tweet result = (Tweet) Translator.StringToObject(str);
		return result;
	}
	
	@Override
	public boolean equals(Object t) {
		if(t instanceof TweetReference){
			TweetReference tf = (TweetReference) t;
			try {
				return tf.id.equals(id) && tf.date.toDate().compareTo(date.toDate())==0 && tf.isReTweet == isReTweet;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void setReTweet(boolean isReTweet) {
		this.isReTweet = isReTweet;
	}

	public boolean isReTweet() {
		return isReTweet;
	}
	
	public String toString(){
		return Translator.ObjectToString(this);
	}
}
