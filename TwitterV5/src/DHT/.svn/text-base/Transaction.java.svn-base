package DHT;

import DHTExceptions.BadSecretException;
import DHTExceptions.NoSetException;
import DHTExceptions.ValueNotFoundException;

public abstract class Transaction {
	

	
	
	
	//Add a write secure to the actual transaction
	public abstract void writeSecureCreate(String secret, String key, String value);
	
	//public abstract void readCreate(String key);
	
	//public abstract void readSetCreate(String key);
	
	//Add a addSecure to the actual transaction
	public abstract void addSecureCreate(String secret, String key, String sValue, String value);
	
	//Add a removeSecure to the actual transaction
	public abstract void removeSecureCreate(String secret, String key, String sValue, String value);
	
	public abstract boolean run() throws BadSecretException, ValueNotFoundException, NoSetException;
	
	//Remove the planned call from the transaction
	public abstract void flush();

}
