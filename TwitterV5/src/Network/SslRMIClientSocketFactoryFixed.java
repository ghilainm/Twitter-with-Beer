package Network;

import java.io.IOException;
import java.net.Socket;
import javax.rmi.ssl.SslRMIClientSocketFactory;

public class SslRMIClientSocketFactoryFixed extends SslRMIClientSocketFactory{
	
	 public Socket createSocket(String host, int port){
		
		 if(port == 0)
			try {
				System.out.println("Creating a client socket on port "+NetworkConst.CLIENT_DEFAULT_PORT);
				return super.createSocket(host, NetworkConst.CLIENT_DEFAULT_PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			try {
				System.out.println("Creating a client socket on port "+port);
				return super.createSocket(host, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
     } 
}
