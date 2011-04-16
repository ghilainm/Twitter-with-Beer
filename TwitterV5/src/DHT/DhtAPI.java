package DHT;
import java.util.LinkedList;

import DHTExceptions.BadSecretException;
import DHTExceptions.NoSetException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;

public interface DhtAPI {
	

	// writes the given value at the given key 
	public void write(String key, String value)throws SecretProtectedException;
	
	// writes secture the given value at the given key 
	public void writeSecure(String secret, String key, String value) throws BadSecretException;
	
	// reads the value at the given key, return NOT_FOUND() if no value is associated to key, return null if operation failed
	public String read(String key) throws ValueNotFoundException;
	
	// reads the set value at the given key return NOT_FOUND() if no value is associated key, return null if operation failed
	public LinkedList<String> readSet(String key) throws ValueNotFoundException;
	
	// add the value at the given key, if the set did not exist the method 
	// create a new set with the special secret noSecret as master and set secret
	public void add(String key, String value) throws SecretProtectedException, NoSetException;
	
	// add secure the value at the given key
	// If no set is associated with key a new set is created with
	// master secret equal to sValue and set secret equal to secret.
	public void addSecure(String secret, String key, String sValue, String value) throws BadSecretException, NoSetException;
	
	// remove secure the value at the given key stored with the secret sValue or the master secret of the set sSecret
	public void removeSecure(String secret, String key, String sValue, String value) throws BadSecretException, ValueNotFoundException;
	
	// remove secure the value at the given key
	public void remove(String key, String value) throws SecretProtectedException, ValueNotFoundException;
	
	// delete the set referenced by the Key key.
	public void deleteSet(String key) throws ValueNotFoundException, SecretProtectedException;
	
	//return the value noSecret to use in case of noSecret wanted
	//when calling a secure version of the classical functions
	public String noSecret();
	
	public Transaction createTransaction() throws BadSecretException, ValueNotFoundException, SecretProtectedException;

	public void deleteSetSecure(String secret, String key)throws ValueNotFoundException, BadSecretException;
	
	public void createSet(String key) throws SecretProtectedException;

	public void createSetSecure(String sSecret, String secret, String key) throws  BadSecretException;
}
