package TwitterApiImplementation;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import JavaGui.GraphicalInterface;
import JavaGui.WelcomeFrame;



public class Main {
	
	
	public static void main(String[] args){
		//Setting the look and feel of the application to the running environment
	    try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Initialisation of the different static classes
		TweetsIdSetList.initTweetsList();
		FollowerList.initFollowerList();
		Tweet.initTweetClass();
		User.initUserClass();
		
		//Start logInScreen and wait
		DhtHandler d = new DebugHandler();
		new WelcomeFrame(d);
		new WelcomeFrame(d);
		
		
	}
	
	
}


