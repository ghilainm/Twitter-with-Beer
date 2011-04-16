package Network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import DHTExceptions.ValueNotFoundException;
import JavaGui.WelcomeFrame;
import TwitterAPIExceptions.NotLoggedException;
import TwitterAPIExceptions.UserAlreadyUsedException;
import TwitterAPIExceptions.UserNotFoundException;
import TwitterApiImplementation.Constants;
import TwitterApiImplementation.TwitterAPI;

public class NetworkTest {

	
	public static void main(String[] agrs) throws RemoteException, UnknownHostException, UserAlreadyUsedException, ValueNotFoundException{
		
		int nbrUsers = 10;
		int nbrFollowersPerUser = 5;
		int nbrTweetPostedByUser=20;
				
		
		
		Long tt = System.currentTimeMillis();
		
		//Create nbrUsers users
		ArrayList<TwitterAPI> userWorlds = new ArrayList<TwitterAPI>(nbrUsers);
		try {
			
				String hostname1 = "130.104.100.69";
				String hostname2 = InetAddress.getLocalHost().getHostAddress();
				String hostname3 = Server.getMyPublicIP();
				new WelcomeFrame(Client.getTwitterAPI(hostname1));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println();
		System.out.println((System.currentTimeMillis()-tt)/1000);
	}
}
