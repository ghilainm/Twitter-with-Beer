package TwitterApiImplementation;

public class LineSetReference {
	
	private String LineName;
	private String UserSetID;
	private String TweetSetID;
	
	public LineSetReference(String lineName, String UserSetID, String TweetSetID) {
		this.LineName = lineName;
		this.UserSetID = UserSetID;
		this.TweetSetID = TweetSetID;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public String getUserSetID() {
		return UserSetID;
	}

	public void setUserSetID(String UserSetID) {
		this.UserSetID = UserSetID;
	}
	
	public String getTweetSetID() {
		return TweetSetID;
	}

	public void setTweetSetID(String TweetSetID) {
		this.TweetSetID = TweetSetID;
	}
	
}
