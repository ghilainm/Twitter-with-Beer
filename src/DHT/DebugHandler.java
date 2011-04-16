package DHT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DebugHandler implements DhtInterface{
	
	Map<String, String> store;
	Map<String, String> storeSecret;
	HashMap<String, setNode> setStore;
	
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
		
		public setNode(){
			this.sSecret = noSecret();
			this.sSecret = noSecret();
			valuesSecrets = new HashMap<String, String>();
		}
		
		//return false if Secret is not good OR if value already in the set and
		//SValue different from the SValue already associated to value in the set
		public boolean addSecure(String secret, String sValue, String value){
			if(secret.compareTo(this.secret)!=0)
				return false;
			else if(valuesSecrets.containsKey(value)){
				if(sValue.compareTo(valuesSecrets.get(value))==0)
					return true;
				else 
					return false;
			}
			else {
				if(sValue==null)
					valuesSecrets.put(value, noSecret());
				else
					valuesSecrets.put(value, sValue);
				return true;
			}
		}
		
		public boolean add(String value){
			return addSecure(noSecret(),noSecret(), value);
		}
		
		public boolean removeSecure(String secret, String sValue, String value){
			//The secret used to access the set has to be equal to the master secret
			//or the access secret
			if(secret.compareTo(this.secret)!=0 && secret.compareTo(this.sSecret)!=0)
				return false;
			//Value not stored in the set
			if(!valuesSecrets.containsKey(value))
				return false;
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
		
		public boolean remove(String value){
			return removeSecure(noSecret(), noSecret(),value);
		}
		
		public String getSSecret() {
			return sSecret;
		}
		
		public String getSecret() {
			return sSecret;
		}
	}
	
	public DebugHandler(){
		store = new HashMap<String,String>(100);
		storeSecret = new HashMap<String,String>(100);
		setStore = new HashMap<String, setNode>(50);
	}
	
	@Override
	public String NOT_FOUND() {
		return "NOT_FOUND";
	}

	@Override
	public boolean write(String key, String value) {
		store.put(key, value);
		storeSecret.put(key, noSecret());
		return true;
	}

	@Override
	public boolean writeSecure(String secret, String key, String value) {
		if(storeSecret.containsKey(key)){
			if(secret.compareTo(storeSecret.get(key))==0){
				store.put(key,value);
				return true;
			}
			else
				return false;
		}
		else{
			store.put(key,value);
			if(secret == null)
				storeSecret.put(key,noSecret());
			else
				storeSecret.put(key,secret);
			return true;
		}
	}

	@Override
	public boolean transaction(int[] function, ArrayList<String[]> args)
			throws UnknownFunction {
		int i = 0;
		for(int fct : function){
			System.out.print(DhtInterface.functions.symbole(fct)+ "    ");
			for(String arg : args.get(i)){
				System.out.print(arg+" - ");
			}
			System.out.println("");
			if(fct>DhtInterface.functions.nbrFunctions())
				throw new UnknownFunction("No function correspond to this id " +fct + " id max is "+(DhtInterface.functions.nbrFunctions()-1));
			String[] arg = args.get(i);
			if(arg.length != DhtInterface.functions.nbrArgs(fct))
				throw new UnknownFunction("No the right number of arguments, got + " + arg.length + " and expected "+ DhtInterface.functions.nbrArgs(fct));
			if(fct == DhtInterface.functions.ADDSECURE.id()){
				while(!addSecure(arg[0], arg[1], arg[2], arg[3]));
			}
			else if(fct == DhtInterface.functions.WRITE.id()){
				while(!write(arg[0], arg[1]));
			}
			else if(fct == DhtInterface.functions.WRITESECURE.id()){
				while(!writeSecure(arg[0], arg[1], arg[2]));
			}
			else if(fct == DhtInterface.functions.ADD.id()){
				while(!add(arg[0], arg[1]));
			}
			else if(fct == DhtInterface.functions.REMOVESECURE.id()){
				removeSecure(arg[0], arg[1], arg[2], arg[3]);
			}
			else{
				//TODO peut etre pas une super exception
				throw new UnknownFunction("This should never happen... programming bug in the enum type");
			}
			i++;
		}
		return true;
	}

	@Override
	public String read(String key) {
		if(store.containsKey(key))
			return store.get(key);
		else 
			return NOT_FOUND();
	}

	@Override
	public String readSet(String key) {
		if(!setStore.containsKey(key))
			return NOT_FOUND();
		String str ="";
		for(String s: setStore.get(key).valuesSecrets.keySet())str+=s+"\n";
		return str;
	}

	@Override
	public boolean add(String key, String value) {
		if(setStore.containsKey(key)){
			return setStore.get(key).add(value);
		}
		else{
			if(createSet(key)){
				return setStore.get(key).add(value);
			}
			else 
				return false;
		}
	}

	@Override
	public boolean addSecure(String secret, String key, String sValue,
			String value) {
		if(setStore.containsKey(key)){
			return setStore.get(key).addSecure(secret, sValue, value);
		}
		
		else{
			if(createSetSecure(secret, noSecret(),key)){
				return setStore.get(key).addSecure(secret, sValue, value);
			}
			else 
				return false;
		}
	}

	@Override
	public boolean removeSecure(String secret, String key, String sValue, String value) {
		if(!setStore.containsKey(key))
			return false;
		return setStore.get(key).removeSecure(secret, sValue, value);
	}

	@Override
	public boolean createSet(String key) {
		return createSetSecure(noSecret(), noSecret(), key);
	}

	@Override
	public boolean createSetSecure(String sSecret, String secret, String key) {
		if(setStore.containsKey(key))
			return false;
		setStore.put(key, new setNode(sSecret, secret));
		return true;
	}

	@Override
	public boolean remove(String key, String value) {
		if(!setStore.containsKey(key))
			return false;
		return setStore.get(key).remove(value);
	}
	
}
