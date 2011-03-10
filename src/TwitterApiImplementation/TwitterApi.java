package TwitterApiImplementation;

public interface TwitterApi {
	public boolean CreateUser(String UserName, String Password, String RealName, String AditionalInfo);

	public boolean LogIn(String Username, String Password);
	
	public String CreateList(String Listname);

	public String MoreUsers(String Listname);

	public String RefreshUsers(String Listname);

	public String AddUser(String Listname, String User);

	public String RemoveUser(String Listname, String User);

	public String MoreTweets(String Listname);

	public String RefreshTweets(String Listname);

	public String PostTweet(String TweetMSG);

	public String ReTweet(String TweetID);

	public String Reply(String TweetMSG, String TweetID);
}
