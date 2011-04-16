package Network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import TwitterApiImplementation.TwitterAPI;

public class Client{

	public static TwitterAPI getTwitterAPI(String hostname) {
		TwitterAPI ti = null;
		InputStream inputstream;
		OutputStream outputstream;
		try {
			
			System.out.println(Server.getMyPublicIP());
			System.out.println(InetAddress.getLocalHost().getHostAddress());
			
			System.setProperty("javax.net.ssl.keyStore","clientkeys");
			System.setProperty("javax.net.ssl.keyStorePassword","arquenxa");
			System.setProperty("javax.net.ssl.trustStore","servertrust");
			System.setProperty("javax.net.ssl.trustStorePassword","arquenxa");
			//System.setProperty("javax.rmi.ssl.client.enabledCipherSuites","TLS_RSA_WITH_AES_128_CBC_SHA");
			//System.setProperty("javax.rmi.ssl.client.enabledProtocols","TLSv1");

			System.out.println("Connection Request...");

			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(hostname, NetworkConst.SERVER_PORT);

            System.out.println("Connection Established...");

			inputstream = sslsocket.getInputStream();
			InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
			BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
			
			outputstream = sslsocket.getOutputStream();
			OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
			BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

			String outmsg = "TwitObjRequest\n";
			
			System.out.println("Send Message...");

			bufferedwriter.write(outmsg);
			bufferedwriter.flush();
			
			String inmsg = null;
            while ((inmsg = bufferedreader.readLine()) != null) {
            	System.out.println("Got Message: " + inmsg);
            	break;
            }
			
			// Close the socket
			inputstream.close();
			outputstream.close();

/*
			if(inmsg.equals("no more conncetions allowed"){
				System.out.println("Unable to find an available port on the server side");
			}
*/
			
			Registry registry = LocateRegistry.getRegistry(hostname,NetworkConst.REGISTRY_PORT,new SslRMIClientSocketFactoryFixed());
			
			ti = (TwitterAPI) registry.lookup("TwitObj"+inmsg);

			System.out.println("Got TwitObj...");


		}
		catch (RemoteException re) {
			System.out.println();
			System.out.println("RemoteException");
			System.out.println(re);
		}
		catch (NotBoundException nbe) {
			System.out.println();
			System.out.println("NotBoundException");
			System.out.println(nbe);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ti;
	}


}
