package JavaGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.rmi.RemoteException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import DHTExceptions.ValueNotFoundException;
import TwitterAPIExceptions.NotLoggedException;
import TwitterApiImplementation.TwitterAPI;



@SuppressWarnings("serial")
public class TweetSendingPanel extends JPanel {
	public JButton tweetButton;
	public TweetEntryPanel msgField;
	private TwitterAPI twitterHighLevelTaskHandler;
	private int outMargin = 20;
	private static Color bgColor;
	
	public TweetSendingPanel(TwitterAPI twitterHighLevelTaskHandler, Color c){
		bgColor = c;
		this.setBackground(bgColor);
		this.twitterHighLevelTaskHandler = twitterHighLevelTaskHandler;
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		msgField = new TweetEntryPanel();
		tweetButton = new JButton("Tweet");
		tweetButton.addActionListener(new TweetButtonListener());
		
		JPanel global = new JPanel();
		global.setOpaque(false);
		global.setLayout(new BoxLayout(global,BoxLayout.PAGE_AXIS));
		
		global.add(Box.createVerticalStrut(20));
		global.add(msgField);
		global.add(Box.createVerticalStrut(20));
		JPanel bottom = new JPanel();
		bottom.setOpaque(false);
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
		bottom.add(tweetButton);
		bottom.add(Box.createHorizontalGlue());
		global.add(bottom);
		global.add(Box.createVerticalStrut(430));
		
		add(Box.createHorizontalStrut(outMargin));
		add(global);
		add(Box.createHorizontalStrut(outMargin));
	}
	
	private class TweetButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				twitterHighLevelTaskHandler.postTweet(msgField.getText());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ValueNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotLoggedException e) {
				new MsgBox("Please login before trying to post a tweet dude! ");
			}
			msgField.setText("Tweet sent");
		}
	}
}
