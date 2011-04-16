package TwitterApiImplementation;

public class ListSetReference {
	
	private String ListName;
	private String TweetSetID;
	
	public ListSetReference(String listName, String TweetSetID) {
		this.ListName = listName;
		this.TweetSetID = TweetSetID;
	}

	public String getListName() {
		return ListName;
	}

	public void setListName(String listName) {
		ListName = listName;
	}
	
	public String getTweetSetID() {
		return TweetSetID;
	}

	public void setTweetSetID(String TweetSetID) {
		this.TweetSetID = TweetSetID;
	}
	
}
