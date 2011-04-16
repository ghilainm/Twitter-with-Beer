package JavaGui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import DHT.DhtAPI;
import DHTExceptions.BadSecretException;
import DHTExceptions.ValueNotFoundException;
import TwitterAPIExceptions.BadPasswordException;
import TwitterAPIExceptions.UserAlreadyUsedException;
import TwitterAPIExceptions.UserNotFoundException;
import TwitterApiImplementation.SecurityUtils;
import TwitterApiImplementation.TwitterAPI;
import TwitterApiImplementation.TwitterImplementation;
import TwitterApiImplementation.User;

public class WelcomeFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Register
	private FieldInput userName;
	private FieldInput realName;
	private PassWordInput passWord;
	
	//login
	private FieldInput userNameLogIn;
	private PassWordInput passWordLogIn;
	private TwitterAPI twitterImpl;
	
	public WelcomeFrame(TwitterAPI twitterImpl){
		super();
		
		//Load images
		ImageLoader.InitImageLoader();
		
		this.twitterImpl = twitterImpl;
		this.setTitle("Welcome to Twitter!");
		
		JTabbedPane tab = new JTabbedPane();
		add(tab);
		
		//Construction of the register Panel
		JPanel registerPanel = new JPanel();
		registerPanel.setLayout(new BoxLayout(registerPanel,BoxLayout.X_AXIS));
		JPanel subRightRegisterPanel = new JPanel();
		subRightRegisterPanel.setLayout(new BoxLayout(subRightRegisterPanel,BoxLayout.PAGE_AXIS));
		FieldDisplay userNameField = new FieldDisplay("User name");
		userName = new FieldInput();
		FieldDisplay realNameField = new FieldDisplay("Real name");
		realName = new FieldInput();
		FieldDisplay passWordField = new FieldDisplay("Password");
		passWord = new PassWordInput();
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new registerButtonListener());
		subRightRegisterPanel.add(Box.createRigidArea(new Dimension(10,10)));
		subRightRegisterPanel.add(userNameField);
		subRightRegisterPanel.add(userName);
		subRightRegisterPanel.add(realNameField);
		subRightRegisterPanel.add(realName);
		subRightRegisterPanel.add(passWordField);
		subRightRegisterPanel.add(passWord);
		subRightRegisterPanel.add(Box.createRigidArea(new Dimension(10,10)));
		subRightRegisterPanel.add(registerButton);
		subRightRegisterPanel.add(Box.createRigidArea(new Dimension(10,10)));
		registerPanel.add(Box.createRigidArea(new Dimension(10,10)));
		registerPanel.add(subRightRegisterPanel);
		registerPanel.add(Box.createRigidArea(new Dimension(10,10)));
		
		
		//Construction of the logPanel
		JPanel logInPanel = new JPanel();
		logInPanel.setLayout(new BoxLayout(logInPanel,BoxLayout.X_AXIS));
		JPanel subLogInPanel = new JPanel();
		subLogInPanel.setLayout(new BoxLayout(subLogInPanel,BoxLayout.PAGE_AXIS));
		FieldDisplay userNameFieldI = new FieldDisplay("User name");
		userNameLogIn = new FieldInput();
		FieldDisplay passWordFieldI = new FieldDisplay("Password");
		passWordLogIn = new PassWordInput();
		JButton logInButton = new JButton("Log In");
		logInButton.addActionListener(new logInButtonListener());
		subLogInPanel.add(Box.createVerticalGlue());
		subLogInPanel.add(userNameFieldI);
		subLogInPanel.add(userNameLogIn);
		subLogInPanel.add(Box.createRigidArea(new Dimension(10,10)));
		subLogInPanel.add(passWordFieldI);
		subLogInPanel.add(passWordLogIn);
		subLogInPanel.add(Box.createVerticalGlue());
		subLogInPanel.add(logInButton);
		subLogInPanel.add(Box.createRigidArea(new Dimension(10,10)));
		logInPanel.add(Box.createRigidArea(new Dimension(10,10)));
		logInPanel.add(subLogInPanel);
		logInPanel.add(Box.createRigidArea(new Dimension(10,10)));
		
		
		tab.addTab("Log In", logInPanel);
		tab.addTab("Register", registerPanel);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		pack();
	}
	
	private class registerButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				twitterImpl.createUser(userName.getText(), new String(passWord.getPassword()), realName.getText());
				startGraphicalInterface(twitterImpl);
			}catch (UserAlreadyUsedException e) {
				new MsgBox("This userName is already used "+userName.getText());
			} catch (ValueNotFoundException e) {
				new MsgBox("This userName is already used "+userName.getText());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private class logInButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				twitterImpl.logIn(userNameLogIn.getText(), new String(passWordLogIn.getPassword()));
				startGraphicalInterface(twitterImpl);
			}catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (ValueNotFoundException e) {
				new MsgBox("Big trouble.... look in the log! ");
				e.printStackTrace();
			} catch (UserNotFoundException e) {
				new MsgBox("This user does not exist");
			} catch (BadPasswordException e) {
				new MsgBox("Guy your password is bad, learn typping!");
			}
		}
	}
	
	public void clear(){
		userName.setText("");
		realName.setText("");
		passWord.setText("");
		userNameLogIn.setText("");
		passWordLogIn.setText("");
	}
	
	private void startGraphicalInterface(TwitterAPI twitterHighLevelTaskHandler){
		clear();
		@SuppressWarnings("unused")
		GraphicalInterface GUI = new GraphicalInterface(twitterHighLevelTaskHandler);
		this.setEnabled(false);
		this.setVisible(false);
		this.dispose();
	}
	
}
