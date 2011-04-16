package TwitterApiImplementation;

import java.io.Serializable;

public class Tweet implements Serializable{

	private static final long serialVersionUID = 8121502140419728578L;
	private String tweetID;
	private String parentID;
	private String kidSetID;
	private String msg;
	private String posterName;
	private StandardDate postingDate;
	private boolean isDeleted;
	//Name of the person that has done the retweet
	private String reTweeterName;
	//True if the tweet is a retweet
	private boolean isReTweet;
	public static final int maxCharPerLine = 90;
	
	public Tweet(StandardDate postingDate, String parentID, String kidSetID, String msg, String posterName, boolean isDeleted, boolean isReTweet) {
		this.parentID = parentID;
		this.msg = msg;
		this.postingDate = postingDate;
		this.posterName = posterName;
		this.isDeleted = isDeleted;
		this.kidSetID = kidSetID;
		this.setReTweet(isReTweet);
	}
	/*
	public String formatMsg(String msg){
		//TODO fix to add return to line only after a word!
		int i = 0;
		int iMax = msg.length();
		String res = "";
		while(i<iMax){
			if(i%maxCharPerLine==0 && i>0){
				res += System.getProperty("line.separator");
			}
			res+=msg.charAt(i);
			i++;
		}
		return res;
	}
	*/
	//Create a deleted tweet
	public Tweet(){
		isDeleted = true;
	}
	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public StandardDate getPostingDate() {
		return postingDate;
	}

	public void setDate(StandardDate postingDate) {
		this.postingDate = postingDate;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDelted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getPosterName() {
		return posterName;
	}

	public void setPosterName(String posterName) {
		this.posterName = posterName;
	}

	public void setKidsID(String kidSetID) {
		this.kidSetID = kidSetID;
	}

	public String getKidSetID() {
		return kidSetID;
	}

	public void setReTweet(boolean isReTweet) {
		this.isReTweet = isReTweet;
	}

	public boolean isReTweet() {
		return isReTweet;
	}

	public void setTweetID(String tweetID) {
		this.tweetID = tweetID;
	}

	public String getTweetID() {
		return tweetID;
	}

	public void setReTweeterName(String reTweeterName) {
		this.reTweeterName = reTweeterName;
	}

	public String getReTweeterName() {
		return reTweeterName;
	}
	
	public String toString(){
		return Translator.ObjectToString(this);
	}
}
