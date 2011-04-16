package Network;

import java.io.IOException;
import java.net.ServerSocket;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class SslRMIServerSocketFactoryFixed extends SslRMIServerSocketFactory{
	
	public SslRMIServerSocketFactoryFixed(String[] enabledCipherSuites, String[] enabledProtocols, boolean needClientAuth) {
		super(enabledCipherSuites,enabledProtocols,needClientAuth);
	}

	public ServerSocket createServerSocket(int port){
		
		if(port == 0)
			try {
				System.out.println("Create ServerSocket on port "+ NetworkConst.SERVER_DEFAULT_PORT);
				return super.createServerSocket(NetworkConst.SERVER_DEFAULT_PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			try {
				System.out.println("Create ServerSocket on port "+ port);
				return super.createServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}
	
}
