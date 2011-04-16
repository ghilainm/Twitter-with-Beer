package TwitterApiImplementation;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import DHT.DhtUtilsAPI;
import DHTExceptions.BadSecretException;
import DHTExceptions.ValueNotFoundException;
import TwitterAPIExceptions.UnAuthorizedValueInSet;

public class UserFragmentedSet{
	//Information about set
	protected String UserFragmentedSetID;
	protected SubFragmentedSet<UserReference> mSfs;
	protected SubFragmentedSet<UserReference> sfs;
	
	//Use for all
	protected HashSet<UserReference> allItems;
	
	protected HashSet<UserReference> displayedItems;
	
	protected final static int CHUNKSIZE = Constants.getUserChunksize();
	protected DhtUtilsAPI dhtHandler;

	public void getNextMoreSubSet() throws ValueNotFoundException{
		try {
			mSfs.getSubset(mSfs.getHead().getNextID(), dhtHandler);
		}catch (UnAuthorizedValueInSet e) {
			//Remove from the set the value that caused the error
			try {
				remove(e.getMessage());
				initMoreSubSet();
			} catch (ValueNotFoundException e1) {
				//Should never happen as we found it is in fact the value that caused a problem
			} catch (BadSecretException e1) {
				//Should never happen as this is our set!
			}
		}
	}
	
	public void initMoreSubSet() throws ValueNotFoundException{
		try {
			mSfs.getSubset(UserFragmentedSetID, dhtHandler);
		}catch (UnAuthorizedValueInSet e) {
			//Remove from the set the value that caused the error
			try {
				remove(e.getMessage());
				initMoreSubSet();
			} catch (ValueNotFoundException e1) {
				//Should never happen as we found it is in fact the value that caused a problem
			} catch (BadSecretException e1) {
				//Should never happen as this is our set!
			}
		}
	}
	
	public void getNextSubSet() throws ValueNotFoundException{
		try {
			sfs.getSubset(sfs.getHead().getNextID(), dhtHandler);
		}catch (UnAuthorizedValueInSet e) {
			//Remove from the set the value that caused the error
			try {
				remove(e.getMessage());
				initMoreSubSet();
			} catch (ValueNotFoundException e1) {
				//Should never happen as we found it is in fact the value that caused a problem
			} catch (BadSecretException e1) {
				//Should never happen as this is our set!
			}
		}
	}
	
	public void initSubSet() throws ValueNotFoundException{
		try {
			sfs.getSubset(UserFragmentedSetID, dhtHandler);
		}catch (UnAuthorizedValueInSet e) {
			//Remove from the set the value that caused the error
			try {
				remove(e.getMessage());
				initMoreSubSet();
			} catch (ValueNotFoundException e1) {
				//Should never happen as we found it is in fact the value that caused a problem
			} catch (BadSecretException e1) {
				//Should never happen as this is our set!
			}
		}
	}
	
	public UserFragmentedSet(String setID, DhtUtilsAPI dhtHandler) throws ValueNotFoundException {
		this.UserFragmentedSetID = setID;
		this.dhtHandler = dhtHandler;
		this.displayedItems =  new HashSet<UserReference>(100);
		mSfs = new SubFragmentedSet<UserReference>();		
		sfs = new SubFragmentedSet<UserReference>();
	}

	public LinkedList<UserReference> more() throws ValueNotFoundException{
		if(mSfs.getHead() == null)
			initMoreSubSet();
		else
			getNextMoreSubSet();
		HashSet<UserReference> setUserRef = new HashSet<UserReference>(mSfs.getList());
		setUserRef.removeAll(displayedItems);
		if(setUserRef.size()==0){
			while(mSfs.getHead().getNextID()!=null && setUserRef.size()==0){
				getNextMoreSubSet();
				setUserRef.addAll(mSfs.getList());
			}
		}
		LinkedList<UserReference> ret = new LinkedList<UserReference>(setUserRef);
		if(ret.size()>CHUNKSIZE)
			ret = new LinkedList<UserReference>(ret.subList(0, CHUNKSIZE-1));
		displayedItems.addAll(ret);
		return ret;				
	}

	public void refreshAll() throws ValueNotFoundException{
		initSubSet();
		if(allItems == null){
			allItems = new HashSet<UserReference>(100);
		}
		
		while(allItems.addAll(sfs.getList()) && sfs.getHead().getNextID() != null)
			getNextSubSet();
	}

	//Cut the list head if she is too big in smaller list
	public boolean cutListHead() throws ValueNotFoundException, BadSecretException{
		initSubSet();

		if (sfs.getList().size() < CHUNKSIZE){
			return false;
		}
		
		LinkedList<UserReference> list = new LinkedList<UserReference>(sfs.getList());

		String nextID = dhtHandler.createSet();

		while (list.size() > CHUNKSIZE){
			SetHeader newheader = new SetHeader(nextID, sfs.getHead().getType(), sfs.getHead().getName());
			LinkedList<UserReference> subset = (LinkedList<UserReference>) list.subList(list.size()-(CHUNKSIZE+1), list.size()-1);
			list.remove(subset);

			nextID = dhtHandler.createSet();

			dhtHandler.addSecure(nextID, Translator.ObjectToString(newheader));
			for(UserReference item : subset){
				add(nextID, item);
				remove(nextID, item);
			} 
		}
		SetHeader newheader = new SetHeader(nextID, sfs.getHead().getType(), sfs.getHead().getName());
		//remove old header from first set
		remove(Translator.ObjectToString(sfs.getHead())); 
		//add new header
		add(Translator.ObjectToString(newheader));
		
		reset();
		return true;
	}
	
	private void remove(String setID,
			Object item) throws BadSecretException, ValueNotFoundException {
		dhtHandler.removeFromSetSecure(setID, Translator.ObjectToString(item));
	}

	private void add(String setID,
			Object item) throws BadSecretException {
		dhtHandler.addSecure(setID, Translator.ObjectToString(item));
	}
	
	public void deleteSet(){
		//TODO Just create a new set
		throw new NotImplementedException();
	}

	public void reset() throws ValueNotFoundException {
		displayedItems = new HashSet<UserReference>();
		mSfs = new SubFragmentedSet<UserReference>();		
		allItems = null;
	}

	public LinkedList<UserReference> all() throws ValueNotFoundException {
		this.refreshAll();
		return new LinkedList<UserReference>(allItems);
	}
	
	public Set<UserReference> getAll() throws ValueNotFoundException{
		this.refreshAll();
		return allItems;
	}
	
	public void remove(String t) throws BadSecretException, ValueNotFoundException{
		dhtHandler.removeFromSetSecure(UserFragmentedSetID, t);
	}
	
	public void add(String t) throws BadSecretException{
		dhtHandler.addSecure(UserFragmentedSetID, t);
	}
	
	public void remove(UserReference t) throws ValueNotFoundException, BadSecretException {
		String myStr = Translator.ObjectToString(t);		
		dhtHandler.removeFromSetSecure(UserFragmentedSetID, myStr);
		allItems.remove(t);
	}
	
	public void add(UserReference myRef) throws BadSecretException {
		String myStr = Translator.ObjectToString(myRef);
		dhtHandler.addSecure(UserFragmentedSetID, myStr);
		allItems.add(myRef);
	}
}
