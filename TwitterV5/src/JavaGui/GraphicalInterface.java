package JavaGui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import TwitterApiImplementation.TwitterAPI;

@SuppressWarnings({ "serial" })
public class GraphicalInterface extends JFrame{
	TweetSendingPanel tweetSendingPanel;
	TimeLinePanel timeLinePanel;
	UserLinePanel userLinePanel;
	FollowersAdderPanel followersAdderPanel;
	Color backGroundPanelColor = new Color(92,111,162);
	Color tabbedPanelColor = DisplayConstant.tabbedPaneGUIColor;
	
	public GraphicalInterface(TwitterAPI twitterHighLevelTaskHandler){
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		JTabbedPane panel = new JTabbedPane();
		panel.setBackground(tabbedPanelColor);
		panel.setPreferredSize(new Dimension(400,600));
		panel.setSize(new Dimension(400,600));
		tweetSendingPanel = new TweetSendingPanel(twitterHighLevelTaskHandler, backGroundPanelColor);
		panel.add(tweetSendingPanel, "Let's Tweet!");
		timeLinePanel = new TimeLinePanel(backGroundPanelColor, 400, 500, new JPanel(), twitterHighLevelTaskHandler);
		panel.add(timeLinePanel, "My TimeLine");
		userLinePanel = new UserLinePanel(backGroundPanelColor, 400,500, new JPanel(), twitterHighLevelTaskHandler);
		panel.add(userLinePanel, "My Tweets");
		followersAdderPanel = new FollowersAdderPanel(backGroundPanelColor, twitterHighLevelTaskHandler);
		panel.add(followersAdderPanel, "Add Followers");
		add(panel);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		pack();
		setResizable(false);
	}
}
