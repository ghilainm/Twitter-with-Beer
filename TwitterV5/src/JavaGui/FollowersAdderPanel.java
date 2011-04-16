package JavaGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import TwitterAPIExceptions.UserNotFoundException;
import TwitterApiImplementation.Constants;
import TwitterApiImplementation.TwitterAPI;

@SuppressWarnings("serial")
public class FollowersAdderPanel extends JPanel {
	private JButton addFollowerButton;
	private JTextField followerNameField;
	private Color c;
	private TwitterAPI twitterHighLevelTaskHandler;
	
	public FollowersAdderPanel(Color c, TwitterAPI twitterHighLevelTaskHandler){
		this.c  = c;
		this.twitterHighLevelTaskHandler = twitterHighLevelTaskHandler;
		setBackground(c);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		followerNameField = new JTextField();
		addFollowerButton = new JButton("Add Follower");
									
		addFollowerButton.addActionListener(new addFollowersButtonListener());
		
		JPanel bottom = new JPanel();
		bottom.setBackground(c);
		bottom.setLayout(new BoxLayout(bottom,BoxLayout.X_AXIS));
		
		add(followerNameField);
		bottom.add(Box.createRigidArea(new Dimension(10,addFollowerButton.getHeight())));
		bottom.add(addFollowerButton);
		bottom.add(Box.createGlue());
		bottom.add(addFollowerButton);
		add(Box.createVerticalGlue());
		add(bottom);
		add(Box.createVerticalGlue());
	}
	
	private class addFollowersButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				twitterHighLevelTaskHandler.addUser(Constants.getUserLineName(), followerNameField.getText());
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (UserNotFoundException e) {
				new MsgBox("Fool you are ! You try to add a non existing user to your list ;)");
			}
			finally{
				followerNameField.setText("");
			}
	    }
	}
}
