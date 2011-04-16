package TalkingFlex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

import DHT.DebugHandler;
import DHT.DhtAPI;
import DHTExceptions.ValueNotFoundException;
import Network.Client;
import Tests.TheTestor;
import TwitterAPIExceptions.NotLoggedException;
import TwitterAPIExceptions.UserAlreadyUsedException;
import TwitterApiImplementation.TwitterAPI;
import TwitterApiImplementation.TwitterImplementation;

public class WebServer {
	//   http://localhost:9532
	Parser parser;
	private static final int portNumber = 9532;
	public void startServer(){
		//DhtAPI  debugHandler = Client.getTwitterAPI(hostname1);
		DhtAPI debugHandler = new DebugHandler();
		TheTestor testor = new TheTestor(debugHandler);
		TwitterAPI twitterAPI = null;
		try {
			testor.loadFile("lotr_tt_script.txt");
		} catch (NotLoggedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (UserAlreadyUsedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ValueNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//TwitterAPI twitterAPI = Client.getTwitterAPI("130.104.100.69");
		try {
			twitterAPI = new TwitterImplementation(debugHandler);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*TwitterAPI twitterAPI = null;
		try {
			twitterAPI = Client.getTwitterAPI(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		parser = new Parser(twitterAPI);
		try {
			ServerSocket sS = new ServerSocket(portNumber);
			while(true){
				new ServerRequestHandler(sS.accept(),parser).run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class ServerRequestHandler extends Thread{
		Socket socket;
		Parser parser;
		static int tweetID = 0;
		public ServerRequestHandler(Socket s, Parser parser){
			socket = s;
			this.parser = parser;
		}
		
		public void run(){
			BufferedReader bis = null;
			PrintWriter pw = null;
			String strToParse = "";
			try {
				bis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(socket.getOutputStream(),true);
				String str="";
				int i =0;
				System.out.println("-------------------------------------------");
				while((str=bis.readLine())!=null && bis.ready()){
					strToParse += str+"\n";
					i++;
				}
				int ind = strToParse.indexOf('\n');
				if(ind!= -1)
					System.out.println(strToParse.substring(0, ind));
				else
					System.out.println(strToParse);
				String reponse = parser.handle(strToParse);
				System.out.println(reponse);
				pw.write(reponse);
				pw.flush();
				bis.close();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		new WebServer().startServer();
	}
}
