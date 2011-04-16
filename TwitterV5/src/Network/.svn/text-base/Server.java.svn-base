package Network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import DHT.DebugHandler;
import DHT.DhtAPI;
import JavaGui.WelcomeFrame;
import TwitterApiImplementation.TwitterAPI;
import TwitterApiImplementation.TwitterImplementation;

public class Server {

	DhtAPI dhtAPI; 
	Registry registry;


	public static String getTwitObjPort(DhtAPI dhtAPI, Registry registry) throws java.rmi.RemoteException {
		TwitterAPI ti = new TwitterImplementation(dhtAPI);
		String id = UUID.randomUUID().toString();
		registry.rebind("TwitObj"+id, ti);
		return id;
	}

	public static String getMyPublicIP(){
        try {
            URL autoIP = new URL("http://www.whatismyip.com/automation/n09230945.asp");
            BufferedReader in = new BufferedReader( new InputStreamReader(autoIP.openStream()));
            String ip_address = (in.readLine()).trim();

            return ip_address;

         }catch (Exception e){
	    	e.printStackTrace();

            return "ERROR";
	    }
    }


	public static void main(String[] args) {
		System.out.println("Server Starting...");

		DhtAPI dhtAPI = new DebugHandler();
	
		
		Registry registry;

		InputStream inputstream;
		OutputStream outputstream;
		System.out.println("Server Critical...");



		try {
			String myhostname1 = InetAddress.getLocalHost().getHostAddress();
			String myhostname2 = getMyPublicIP();
			String myhostname = myhostname1;
			System.out.println("Server is at " + myhostname);
			System.setProperty("java.rmi.server.hostname", myhostname);
			System.setProperty("javax.net.ssl.keyStore", "serverkeys");
			System.setProperty("javax.net.ssl.keyStorePassword", "arquenxa");
			System.setProperty("javax.net.ssl.trustStore", "clienttrust");
			System.setProperty("javax.net.ssl.trustStorePassword", "arquenxa");
			//System.setProperty("javax.rmi.ssl.client.enabledCipherSuites", "TLS_RSA_WITH_AES_128_CBC_SHA");
			//System.setProperty("javax.rmi.ssl.client.enabledProtocols", "TLSv1");
			//System.setProperty("javax.net.debug", "ssl,handshake");
		
			registry = LocateRegistry.createRegistry(NetworkConst.REGISTRY_PORT,
					new SslRMIClientSocketFactoryFixed(),
					new SslRMIServerSocketFactoryFixed(null, null, true));

			//registry = LocateRegistry.createRegistry(64666);

			System.out.println("Server Registery Created...");

			SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(NetworkConst.SERVER_PORT);
			System.out.println("Server Socket Created...");

			System.out.println("Server Running...");

			String inmsg = null;
			String outmsg = null;

			SSLSocket sslsocket;
			InputStreamReader inputstreamreader;
			BufferedReader bufferedreader;
			OutputStreamWriter outputstreamwriter;
			BufferedWriter bufferedwriter;

			while(true){
				System.out.println("Waiting for message...");

				sslsocket = (SSLSocket) sslserversocket.accept();
				System.out.println("Got new message...");

				inputstream = sslsocket.getInputStream();
				inputstreamreader = new InputStreamReader(inputstream);
				bufferedreader = new BufferedReader(inputstreamreader);

				outputstream = sslsocket.getOutputStream();
				outputstreamwriter = new OutputStreamWriter(outputstream);
				bufferedwriter = new BufferedWriter(outputstreamwriter);

				inmsg = null;
				while ((inmsg = bufferedreader.readLine()) != null) {
					System.out.println("Got Message: " + inmsg);
					break;
				}

				if (inmsg.equals("TwitObjRequest")){
					System.out.println("Handling Request");
					outmsg = getTwitObjPort(dhtAPI, registry);
					bufferedwriter.write(outmsg);
					bufferedwriter.flush();
				}
				else{
					System.out.println("Unexpected Message");
				}

				inputstream.close();
				outputstream.close();
			}


		} catch (Exception e) {
			System.out.println("Server shut down: " + e);
		}


	}

}
