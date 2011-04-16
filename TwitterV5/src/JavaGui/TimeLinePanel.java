package JavaGui;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.LinkedList;

import javax.swing.JPanel;

import TwitterAPIExceptions.NotLoggedException;
import TwitterApiImplementation.Constants;
import TwitterApiImplementation.Tweet;
import TwitterApiImplementation.TwitterAPI;

public class TimeLinePanel extends LinePanel {
	private TwitterAPI twitterHighLevelTaskHandler;
	
	public TimeLinePanel(Color color, int xSize, int ySize, JPanel panel, TwitterAPI twitterHighLevelTaskHandler) {
		super(color, xSize, ySize, panel);
		this.twitterHighLevelTaskHandler = twitterHighLevelTaskHandler;
		initRefresh();
		more();
	}

	public void reFresh(){
		LinkedList<Tweet> myTweetLine = null;
		try {
			myTweetLine = twitterHighLevelTaskHandler.refreshTweetsFromLine(Constants.getTimeLineName());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotLoggedException e) {
			e.printStackTrace();
		}
		if(myTweetLine==null)return;
		reDraw(myTweetLine);
	}
	
	public void more(){
		LinkedList<Tweet> myTweetLine = null;
		try {
			myTweetLine = twitterHighLevelTaskHandler.moreTweetsFromLine(Constants.getTimeLineName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotLoggedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(myTweetLine==null)return;
		reDraw(myTweetLine);
	}

}
