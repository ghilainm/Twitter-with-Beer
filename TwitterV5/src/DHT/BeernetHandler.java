package DHT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.LinkedList;

import DHTExceptions.BadSecretException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;
import LRUCache.CacheAPI;
import LRUCache.LRUCache;

public class BeernetHandler implements DhtAPI{

	private int portNumber;
	private Socket theSocket;
	private PrintWriter out;
	private BufferedReader in;
	private final static String SEPARATOR = "&";
	private final static String PUBLIC = "public";
	private final static String BADSECRET = "error(bad_secret)";
	private final static String NOTFOUND = "NOT_FOUND";
	private final static String SUCCESS = "SUCCESS"; 
	private final static String ABORT = "ABORT"; 
	
	//cache
	private boolean useCache;
	private CacheAPI<String, String> cache;
	private final static int ELEMENTSINCACHE = 10000;
	
	private String EncodeToAscii(String str){
		try {
			return new String(str.getBytes(), "ASCII");
		} catch (UnsupportedEncodingException e) {
			
		}
		return null;
	}
	
	private String DecodeToDefaultEncoding(String str){
		try {
			return new String(str.getBytes("ASCII"));
		} catch (UnsupportedEncodingException e) {
			
		}
		return null;
	}

	public BeernetHandler(int aPort, boolean useCache) {
		portNumber = aPort;
		this.useCache = useCache;
		if(useCache){
			cache = new LRUCache<String,String>(ELEMENTSINCACHE);
		}
	}


	public int connect(){
		try {
			theSocket = new Socket("localhost", portNumber);
			out = new PrintWriter(theSocket.getOutputStream(), true);
			out.flush();
			in = new BufferedReader(
					new InputStreamReader(theSocket.getInputStream()));
			return 0;
		} catch (IOException e) {
			System.err.println("creation of socket failed");
			return -1;
		}
	}


	public int disconnect(){
		try {
			in.close();
			out.close();
			theSocket.close();
			return 0;
		} catch (IOException e) {
			System.err.println("closing socket failed");
			return -1;
		}
	}

	private void sendMessage(String aMsg) {
		out.println(EncodeToAscii(aMsg));
		out.flush();
	}

	private String getMessage(){
		String result = null;
		try {
			result = DecodeToDefaultEncoding(in.readLine());
		} catch (IOException e) {
			System.err.println("problem getting trough socket");
		}
		return result;
	}

	@Override
	public void add(String key, String value) throws SecretProtectedException {
		try {
			addSecure(PUBLIC, key, PUBLIC, value);
		} catch (BadSecretException e) {
			throw new SecretProtectedException(BADSECRET);
		}
	}

	@Override
	public void addSecure(String secret, String key, String sValue, String value) throws BadSecretException {
		sendMessage("add("+secret +SEPARATOR+ value +SEPARATOR+ sValue +SEPARATOR+ key+")");
		String result = getMessage();
		if (result.equals(BADSECRET)){
			throw new BadSecretException(result);
		}  
	}

	@Override
	public void createSet(String key) throws SecretProtectedException {
		try {
			createSetSecure(PUBLIC, PUBLIC, key);
		} catch (BadSecretException e) {
			throw new SecretProtectedException(BADSECRET);
		}
	}

	@Override
	public void createSetSecure(String sSecret, String secret, String key) throws BadSecretException {
		sendMessage("creatset("+sSecret+SEPARATOR+secret+SEPARATOR+key+")");
		String result = getMessage();
		if (result.equals(BADSECRET)){
			throw new BadSecretException(result);
		}
	}


	public void increment(String key) throws BadSecretException {
		
		String trans = 	"trans(proc{Trans TM}" +
						 "{TM read( "+ key + "v)}" +
						 "{TM write( "+ key + "v+1)}" +
						 "{TM commit}"+
						 "end";
		
		sendMessage(trans);
		String result = getMessage();
		if (result.equals(ABORT)){
			throw new BadSecretException(result);
		}
		
	}

	@Override
	public void deleteSet(String key) throws ValueNotFoundException, SecretProtectedException {
		try{
			deleteSetSecure(PUBLIC,key);
		} catch (BadSecretException e) {
			throw new SecretProtectedException(BADSECRET);
		}
	}

	@Override
	public void deleteSetSecure(String secret, String key) throws ValueNotFoundException, BadSecretException {
		sendMessage("deleteset("+secret+SEPARATOR+key+")");
		String result = getMessage();
		if (result.equals(BADSECRET)){
			throw new BadSecretException(result);
		}
		if (result.equals(NOTFOUND)){
			throw new ValueNotFoundException(result);
		}	
	}

	@Override
	public String noSecret() {
		return PUBLIC;
	}

	@Override
	public String read(String key) throws ValueNotFoundException {
		String result = null;
		if(useCache && cache.contain(key)){
			result = cache.get(key);
		}
		else{
			sendMessage("read("+key+")");
			result = getMessage();
			cache.put(key, result);
		}
		if (result.equals(NOTFOUND)){
			throw new ValueNotFoundException(result);
		}	
		return result;
	}

	@Override
	public LinkedList<String> readSet(String key) throws ValueNotFoundException {
		String result = null;
		if(useCache && cache.contain(key)){
			result = cache.get(key);
		}
		else{
			sendMessage("read("+key+")");
			result = getMessage();
			cache.put(key, result);
		}
		
		if (result.equals(NOTFOUND)){
			throw new ValueNotFoundException(result);
		}	

		LinkedList<String> resultlist = new LinkedList<String>();
		String[] lines = result.split(SEPARATOR);
		for(int i = 0; i < lines.length; i++){
			resultlist.add(lines[i]);
		}
		return resultlist;
	}

	@Override
	public void remove(String key, String value) throws SecretProtectedException, ValueNotFoundException {
		try {
			removeSecure(PUBLIC, key, PUBLIC, value);
		} catch (BadSecretException e) {
			throw new SecretProtectedException(BADSECRET);
		}
	}

	@Override
	public void removeSecure(String secret, String key, String sValue, String value) throws BadSecretException, ValueNotFoundException {
		sendMessage("remove("+secret+SEPARATOR+key+SEPARATOR+sValue+SEPARATOR+value+")");
		String result = getMessage();
		if (result.equals(BADSECRET)){
			throw new BadSecretException(result);
		}
		else if (result.equals(NOTFOUND)){
			throw new ValueNotFoundException(result);
		}
	}

	@Override
	public void write(String key, String value) throws SecretProtectedException {
		try {
			writeSecure(PUBLIC,key,value);
		} catch (BadSecretException e) {
			throw new SecretProtectedException(BADSECRET);
		}
	}

	@Override
	public void writeSecure(String secret, String key, String value) throws BadSecretException {
		sendMessage("write("+secret+SEPARATOR+key+SEPARATOR+value+")");
		String result = getMessage();
		if (result.equals(BADSECRET)){
			throw new BadSecretException(result);
		}
	}


	@Override
	public Transaction createTransaction() throws BadSecretException, ValueNotFoundException, SecretProtectedException {
		// TODO Auto-generated method stub
		return null;
	}

}
