package DHT;

import DHTExceptions.BadSecretException;
import DHTExceptions.NoSetException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;

public interface DhtUtilsAPI{
	/*
	 * This class is used to perform secure operations on the DHT
	 * abstracting from the userSecret
	 * A call to setUserSecret must be done before any call to other functions.
	 */
	
	
	//This function set the userSecret that will be used
	//in all the functions when doing operation on the DHT
	public void setUserSecret(String userSecret);
	
	//This function create a new protected Set and return the key associated.
	//A protected set can only by written by user knowing the secret set
	//This set has a master key equals to userSecret and a secret set equals to applicationSecret defined
	//in the constant file
	/**
	 * @return The value returned is the key referencing to the set created
	 * @throws SecretProtectedException 
	 */
	public String createSet();
	public void deleteSet(String key) throws ValueNotFoundException, BadSecretException;
	
	//This function is used to add a Value value to a set referenced by a Key key
	/**
	 * @throws NoSetException 
	 * @exception This function throws BadSecretException
	 */
	public void addSecure(String key, String value) throws BadSecretException;
	
	//This function write the Value value in the DHT with  a unique id 
	/**
	 * @return The value returned is the key referencing the value posted
	 * @exception This function throws BadSecretException
	 */
	public String writeSecure(String value) throws BadSecretException;
	public void removeSecure(String key) throws BadSecretException,ValueNotFoundException ;
	
	public DhtAPI getDht();

	public void writeSecure(String key, String value) throws BadSecretException;

	public void removeFromSetSecure(String key, String value) throws  BadSecretException, ValueNotFoundException;
}
