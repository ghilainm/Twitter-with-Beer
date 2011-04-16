package DHT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import DHTExceptions.BadSecretException;
import DHTExceptions.NoSetException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;


public class DebugHandler implements DhtAPI {
	
	Map<String, String> store;
	Map<String, String> storeSecret;
	HashMap<String, setNode> setStore;
	private static final boolean verbose = false;
	
	public String noSecret(){
		return "NO_SECRET";
	}
	
	private class setNode{
		private String sSecret;
		private String secret;
		// <Value, SValue>
		//TODO precise in sementic what is happening when you add a value and 
		//this value already exist.
		private HashMap<String, String> valuesSecrets;
		
		public setNode(String sSecret, String secret){
			if(sSecret == null)
				this.sSecret = noSecret();
			else
				this.sSecret = sSecret;
			if(secret == null)
				this.secret = noSecret();
			else
				this.secret = secret;
			valuesSecrets = new HashMap<String, String>();
		}
		
		//return false if Secret is not good OR if value already in the set and
		//SValue different from the SValue already associated to value in the set
		public boolean addSecure(String secret, String sValue, String value) throws BadSecretException{
			if(secret.compareTo(this.secret)!=0)
				throw new BadSecretException("This set is protected by a secret");
			else if(valuesSecrets.containsKey(value)){
				if(sValue.compareTo(valuesSecrets.get(value))==0)
					return true;
				else 
					throw new BadSecretException("This value is protected by a secret");
			}
			else {
				if(sValue==null)
					valuesSecrets.put(value, noSecret());
				else
					valuesSecrets.put(value, sValue);
				return true;
			}
		}
		
		public boolean add(String value) throws SecretProtectedException{
			try {
				return addSecure(noSecret(),noSecret(), value);
			} catch (BadSecretException e) {
				throw new SecretProtectedException("Protected by secret");
			}
		}
		
		public boolean removeSecure(String secret, String sValue, String value) throws BadSecretException, ValueNotFoundException{
			//The secret used to access the set has to be equal to the master secret
			//or the access secret
			if(secret.compareTo(this.secret)!=0 && secret.compareTo(this.sSecret)!=0)
				throw new BadSecretException("This set and value are protected by a secret");
			//Value not stored in the set
			if(!valuesSecrets.containsKey(value))
				throw new ValueNotFoundException("No value was found for "+value+" in the set");
			//sSecret used to remove the ky
			if(sValue.compareTo(sSecret)==0){
				valuesSecrets.remove(value);
				return true;
			}
			//sValue used to remove the key
			else if(sValue.compareTo(valuesSecrets.get(value))==0){
				valuesSecrets.remove(value);
				return true;
			}
			else
				return false;
		}
		
		public boolean remove(String value) throws SecretProtectedException, ValueNotFoundException{
			try {
				return removeSecure(noSecret(), noSecret(),value);
			} catch (BadSecretException e) {
				throw new SecretProtectedException("Protected by secret");
			}
		}
	}
	
	public DebugHandler(){
		store = new HashMap<String,String>(20);
		storeSecret = new HashMap<String,String>(20);
		setStore = new HashMap<String, setNode>(5);
	}
	

	@Override
	public void write(String key, String value) throws SecretProtectedException {
		try {
			writeSecure(noSecret(), key, value);
		} catch (BadSecretException e) {
			throw new SecretProtectedException("This key is protected by a secret");
		}
	}

	@Override
	public void writeSecure(String secret, String key, String value) throws BadSecretException {
		if(verbose)System.out.println(secret+" "+key+" \n"+value+"\n");
		if(storeSecret.containsKey(key)){
			if(secret.compareTo(storeSecret.get(key))==0){
				store.put(key,value);
			}
			else
				throw new BadSecretException("Bad secret for this key");
		}
		else{
			store.put(key,value);
			if(secret == null)
				storeSecret.put(key,noSecret());
			else
				storeSecret.put(key,secret);
		}
	}

	@Override
	public String read(String key) throws ValueNotFoundException {
		if(store.containsKey(key))
			return store.get(key);
		else 
			throw new ValueNotFoundException("No value for key "+key);
	}

	@Override
	public LinkedList<String> readSet(String key) throws ValueNotFoundException {
		if(!setStore.containsKey(key))
			throw new ValueNotFoundException("No value for key "+key);
		LinkedList<String> ret = new LinkedList<String>();
		if(verbose)System.out.println("####");
		for(String s: setStore.get(key).valuesSecrets.keySet()){
			if(verbose){
				System.out.println(s+"\n");
			}
			ret.add(s);
		}
		if(verbose)System.out.println("####");
		return ret;
	}

	@Override
	public void add(String key, String value) throws SecretProtectedException, NoSetException {
		if(setStore.containsKey(key)){
			setStore.get(key).add(value);
		}
		else{
			throw new NoSetException("There is not set associated with the key "+key);
		}
	}

	@Override
	public void addSecure(String secret, String key, String sValue,
			String value) throws BadSecretException, NoSetException {
		if(verbose)System.out.println(secret+" "+key+" "+sValue+" \n"+value+"\n");
		if(setStore.containsKey(key)){
			setStore.get(key).addSecure(secret, sValue, value);
		}
		
		else{
			throw new NoSetException("There is not set associated with the key "+key);
		}
	}

	@Override
	public void removeSecure(String secret, String key, String sValue, String value) throws BadSecretException, ValueNotFoundException {
		if(!setStore.containsKey(key))
			throw new ValueNotFoundException("No value for key "+key);
		setStore.get(key).removeSecure(secret, sValue, value);
	}

	public void createSet(String key) throws SecretProtectedException {
		try {
			createSetSecure(noSecret(), noSecret(), key);
		} catch (BadSecretException e) {
			throw new SecretProtectedException(e.getMessage());
		}
	}

	public void createSetSecure(String sSecret, String secret, String key) throws BadSecretException {
		if(setStore.containsKey(key)){
			throw new BadSecretException("There is already a set with this key "+key);
		}
		setStore.put(key, new setNode(sSecret, secret));
	}

	@Override
	public void remove(String key, String value) throws SecretProtectedException, ValueNotFoundException {
		if(!setStore.containsKey(key))
			throw new ValueNotFoundException("No value for key "+key);
		setStore.get(key).remove(value);
	}
	
	private class DebugTransaction extends Transaction{
		private ArrayList<String[]> argsWriteSecure;
		private ArrayList<String[]> argsAddSecure;
		private ArrayList<String[]> argsRemoveSecure;
		
		@Override
		public void writeSecureCreate(String secret, String key, String value) {
			String [] newArgs = {secret,key,value};
			argsWriteSecure.add(newArgs);
		}

		@Override
		public void addSecureCreate(String secret, String key,
				String sValue, String value) {
			String [] newArgs = {secret,key,sValue,value};
			argsAddSecure.add(newArgs);
		}

		@Override
		public void removeSecureCreate(String secret, String key,
				String sValue, String value) {
			String [] newArgs = {secret,key,sValue,value};
			argsRemoveSecure.add(newArgs);
		}

		@Override
		public boolean run() throws BadSecretException, ValueNotFoundException, NoSetException {
			for(String[] writeSecureCall : argsWriteSecure){
				writeSecure(writeSecureCall[0], writeSecureCall[1], writeSecureCall[2]);
			}
			for(String[] addSecureCall : argsAddSecure){
				addSecure(addSecureCall[0], addSecureCall[1], addSecureCall[2],addSecureCall[3]);
			}
			for(String[] removeSecureCall : argsRemoveSecure){
				removeSecure(removeSecureCall[0], removeSecureCall[1], removeSecureCall[2],removeSecureCall[3]);
			}
			return true;
		}
		
		@Override
		public void flush(){
			argsWriteSecure.clear();
			argsAddSecure.clear();
			argsRemoveSecure.clear();
		}
	}
	@Override
	public Transaction createTransaction() {
		return new DebugTransaction();
	}

	@Override
	public void deleteSet(String key) throws ValueNotFoundException, SecretProtectedException {
		try{
			deleteSetSecure(noSecret(),key);
		} catch (BadSecretException e) {
			throw new SecretProtectedException(e.getMessage());
		} 
	}
	
	@Override
	public void deleteSetSecure(String secret, String key) throws ValueNotFoundException, BadSecretException {
		if(!setStore.containsKey(key))
			throw new ValueNotFoundException("This set does not exist");
		if(setStore.get(key).sSecret.compareTo(secret)==0)
			setStore.remove(key);
		else
			throw new BadSecretException("The secret "+secret+" is not good for the key "+ key);
	}
	
}
