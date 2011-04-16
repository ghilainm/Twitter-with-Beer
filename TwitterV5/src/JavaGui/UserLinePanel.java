package JavaGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import TwitterAPIExceptions.NotLoggedException;
import TwitterApiImplementation.Constants;
import TwitterApiImplementation.Tweet;
import TwitterApiImplementation.TwitterAPI;


public class UserLinePanel extends LinePanel {
	
	private TwitterAPI twitterHighLevelTaskHandler;
	
	public UserLinePanel(Color color, int xSize, int ySize, JPanel panel, TwitterAPI twitterHighLevelTaskHandler) {
		super(color, xSize, ySize, panel);
		this.twitterHighLevelTaskHandler = twitterHighLevelTaskHandler;
		initRefresh();
		more();
	}

	public void reFresh(){
		LinkedList<Tweet> myUserLine = null;
		try {
			myUserLine = twitterHighLevelTaskHandler.refreshTweetsFromLine(Constants.getUserLineName());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotLoggedException e) {
			new MsgBox("Please connect before trying to post a tweet...");
		}
		if(myUserLine==null)return;
		reDraw(myUserLine);
	}
	
	public void more(){
		LinkedList<Tweet> myUserLine = null;
		try {
			myUserLine = twitterHighLevelTaskHandler.moreTweetsFromLine(Constants.getUserLineName());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotLoggedException e) {
			new MsgBox("Please connect before trying to post a tweet...");
		}
		if(myUserLine==null)return;
		reDraw(myUserLine);
	}
	

}
