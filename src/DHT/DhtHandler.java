package DHT;
import java.util.ArrayList;


public interface DhtHandler {
	
	//String corresponding to a read request when no value is
	//associated to the key
	public String NOT_FOUND();
	
	public enum functions{
		ADDSECURE(4,"addSecure"),
		ADD(2,"add"),
		//READ(1,"read"),
		WRITE(2,"write"),
		WRITESECURE(3, "writeSecure"),
		REMOVESECURE(4, "removeSecure")
		/*
		READSET(1,"readSet")*/
		;
		
		private int id; 
		private final int nbrArgs; 
		private final String symbole; 
		private static int nbrFunctions;
		
		private functions(int nbrArgs, String symbole) {
	        this.nbrArgs = nbrArgs;
	        this.symbole = symbole;
	        addFunction();
		}
		
		private void addFunction(){
			id = nbrFunctions;
			nbrFunctions++;
		}
		
		public int id(){return id;}
		public int nbrArgs(){return nbrArgs;}
		public String symbol(){return symbole;}
		public static int nbrFunctions(){
			return nbrFunctions;
		}
		
		public static int nbrArgs(int id) throws UnknownFunction{
			//Error, no function correspond to this id.
			if(id>functions.nbrFunctions)
				throw new UnknownFunction("No function correspond to this id");
			for(functions f : functions.values()){
				if(f.id == id)
					return f.nbrArgs;
			}
			return -1;
		}
		
		public static String symbole(int id) throws UnknownFunction{
			//Error, no function correspond to this id.
			if(id>functions.nbrFunctions)
				throw new UnknownFunction("No function correspond to this id");
			for(functions f : functions.values()){
				if(f.id == id)
					return f.symbole;
			}
			return null;
		}
	}
	
	// writes the given value at the given key 
	public boolean write(String key, String value);
	
	// writes secture the given value at the given key 
	public boolean writeSecure(String secret, String key, String value);
	
	// perform the series of functions provided as argument with the arguments args
	// in an atomic form, everything or nothing. return true if everything was done
	// correctly, the functions must be defined in the interface
	/*
	 * TODO BIG PROBLEM MY DEAR... comment on fait pour les transaction, car beernet
	 * nous propose un super joli syst�me de transaction mais dans notre cas �a marche pas
	 * car on a du code java qui repr�sente la transaction... donc comment faire :). Sauf
	 * si les transaction ne comporte pas de read/get/readSet dans ce cas l� �a marche...
	 * et je pense qu'on peut s'en foutre des read/get/readSet...
	*/
	public boolean transaction(int[] function, ArrayList<String[]> args) throws UnknownFunction;
	
	// reads the value at the given key, return NOT_FOUND() if no value is associated to key, return null if operation failed
	public String read(String key);
	
	// reads the set value at the given key return NOT_FOUND() if no value is associated key, return null if operation failed
	public String readSet(String key);
	
	// add the value at the given key, if the set did not exist the method call createSet
	public boolean add(String key, String value);
	
	// add secure the value at the given key if the set did not exist the method
	public boolean addSecure(String secret, String key, String sValue, String value);
	
	// remove secure the value at the given key stored with the secret sValue or the master secret of the set sSecret
	public boolean removeSecure(String secret, String key, String sValue, String value);
	
	// remove secure the value at the given key
	public boolean remove(String key, String value);
	
	//create a new set associated to key
	public boolean createSet(String key);
	
	//create a new secure set with a master key and an access key
	public boolean createSetSecure(String sSecret, String secret, String key);
	
	//return the value noSecret to use in case of noSecret wanted
	//when calling a secure version of the classical functions
	public String noSecret();
	//TODO ADD put, get, putsecure
	
}
