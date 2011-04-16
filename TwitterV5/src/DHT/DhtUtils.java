package DHT;

import java.util.UUID;

import DHTExceptions.BadSecretException;
import DHTExceptions.NoSetException;
import DHTExceptions.ValueNotFoundException;
import TwitterApiImplementation.Constants;

public class DhtUtils implements DhtUtilsAPI{
	/*
	 * this class is used to perform task while handling exceptions generated
	 * by the DhtAPI, so that the subclasses can abstract from the details
	 */
	public String userSecret;
	public DhtAPI dhtAPI;
	

	public DhtUtils(String userSecret, DhtAPI dhtAPI){
		this.userSecret = userSecret;
		this.dhtAPI = dhtAPI;
	}
	
	public DhtUtils(DhtAPI dhtAPI){
		this.dhtAPI = dhtAPI;
	}
	
	public void setUserSecret(String userSecret){
		this.userSecret = userSecret;
	}
	
	public String genID(){
		return  UUID.randomUUID().toString();
	}
	
	public String createSet(){
		boolean notCreated = true;
		String newSetID = "";
		while(notCreated){
			newSetID = genID();
			try {
				dhtAPI.createSetSecure(userSecret, Constants.applicationSecret,  newSetID);
				notCreated = false;
			} catch (BadSecretException e) {
			} 
		}
		return newSetID;
	}
	
	public void deleteSet(String key) throws ValueNotFoundException, BadSecretException{
		dhtAPI.deleteSetSecure(userSecret, key);
	}
	
	public void addSecure(String key, String value) throws BadSecretException{
		try {
			dhtAPI.addSecure(Constants.applicationSecret, key, userSecret, value);
		} catch (NoSetException e) {
			System.out.println("NoSetException in dhtUtils: Should only happen when key == null key :"+key);
		}
	}
	
	public String writeSecure(String value) throws BadSecretException{
		boolean notCreated = true;
		String newValueID = "";
		while(notCreated){
			newValueID = genID();
				//We put nothing in the set because we just need to create and protect it with our secret
				try {
					dhtAPI.writeSecure(userSecret, newValueID, value);
					notCreated = false;
				} catch (BadSecretException e) {}
		}
		return newValueID;
	}

	@Override
	public DhtAPI getDht() {
		return dhtAPI;
	}

	@Override
	public void writeSecure(String key, String value) throws BadSecretException {
		dhtAPI.writeSecure(userSecret, key, value);
	}
	
	@Override
	public void removeSecure(String key) throws BadSecretException, ValueNotFoundException {
		dhtAPI.removeSecure(userSecret, key, null, null);
	}

	@Override
	public void removeFromSetSecure(String key, String value)throws BadSecretException, ValueNotFoundException {
		dhtAPI.removeSecure(Constants.applicationSecret, key, userSecret, value);

	}
	
	
	/*
	
	public void hashPut(String Value){
		boolean notCreated = true;
		int hash = Value.hashCode();
		String newSetID;
		while(notCreated){
			try {
				newSetID = "" + hash;
				dhtAPI.createSetSecure(Constants.hashsetsecret, Constants.applicationSecret, newSetID);
				notCreated = false;
			} catch (SetAlreadyExistException e) {
			}
			hash++;
		}
	}
	
	
	public boolean hashContains(String Value){
		boolean found = false;
		int hash = Value.hashCode();
		String SetID;
		while(!found){
			try {
				SetID = "" + hash;
				dhtAPI.readSet(SetID);
				found = false;
			} catch (ValueNotFoundException e) {
			}
			hash++;
		}	}
	
	public void hashRemove(String Value){
		
	}
	*/
	
}
