package TwitterApiImplementation;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private StandardDate registerDate;
	private String lineSetID;
	private String listSetID;
	private String passwordHash;
	private String toPostSetID;
	private String FollowerSetID;
	private String realName;
	private String userSecret;
	
	//TODO add the number of tweets posted and number of followers and number
	//of people that he follows
	
	public User(String userName, StandardDate date, String lineSetID, String listSetID, String passwordHash, String FollowerSetID,
			String toPostSetID, String realName) {
		this.userName = userName;
		this.registerDate = date;
		this.lineSetID = lineSetID;
		this.listSetID = listSetID;
		this.passwordHash = passwordHash;
		this.toPostSetID = toPostSetID;
		this.FollowerSetID = FollowerSetID;
		this.realName = realName;
	}

	public User(String userName, String password, String realName) {
		this.userName = userName;
		this.realName = realName;
		this.passwordHash = SecurityUtils.Hash(password);
		this.setUserSecret(SecurityUtils.getUserSecret(userName, password));
		this.registerDate = new StandardDate();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public StandardDate getregisterDate() {
		return registerDate;
	}

	public String getLineSetID() {
		return lineSetID;
	}

	public void setLineSetID(String lineSetID) {
		this.lineSetID = lineSetID;
	}
	
	
	public String getListSetID() {
		return listSetID;
	}

	public void setListSetID(String listSetID) {
		this.listSetID = listSetID;
	}

	public String getpasswordHash() {
		return passwordHash;
	}

	public void setPassword(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String gettoPostSetID() {
		return toPostSetID;
	}

	public void settoPostSetID(String toPostSetID) {
		this.toPostSetID = toPostSetID;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public void setUserSecret(String userSecret) {
		this.userSecret = userSecret;
	}

	public String getUserSecret() {
		return userSecret;
	}

	public void setFollowerSetID(String followerSetID) {
		FollowerSetID = followerSetID;
	}

	public String getFollowerSetID() {
		return FollowerSetID;
	}
	
	public String toString(){
		return Translator.ObjectToString(this);
	}
	
}
