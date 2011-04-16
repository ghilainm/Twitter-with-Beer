package TwitterApiImplementation;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import TwitterAPIExceptions.UnAuthorizedValueInSet;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import DHT.DhtUtilsAPI;
import DHTExceptions.BadSecretException;
import DHTExceptions.ValueNotFoundException;

public class TweetFragmentedSet{
	protected SortedSet<TweetReference> relevantitemsMore; //Old tweets in last call to more read but not displayed
	protected SortedSet<TweetReference> relevantitems; //For all...
	protected HashSet<TweetReference> displayed;
	
	protected final static int CHUNKSIZE = Constants.getTweetChunksize();
	protected DhtUtilsAPI dhtHandler;
	protected String setID;
	protected SubFragmentedSet<TweetReference> moreSfs;
	protected SubFragmentedSet<TweetReference> sfs;

	public TweetFragmentedSet(String setID, DhtUtilsAPI dhtHandler) {
		this.setID = setID;
		this.dhtHandler = dhtHandler;
		reset();
	}
	
	public String getSetID(){
		return setID;
	}
	
	public void getNextMoreSubSet() throws ValueNotFoundException{
		try {
			moreSfs.getSubset(moreSfs.getHead().getNextID(), dhtHandler);
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
		relevantitemsMore.addAll((LinkedList<TweetReference>)moreSfs.getList());
	}
	
	public void initMoreSubSet() throws ValueNotFoundException{
		try {
			moreSfs.getSubset(setID, dhtHandler);
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
		relevantitemsMore.addAll((LinkedList<TweetReference>)moreSfs.getList());
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
		relevantitems.addAll((LinkedList<TweetReference>)sfs.getList());
	}
	
	public void initSubSet() throws ValueNotFoundException{
		try {
			sfs.getSubset(setID, dhtHandler);
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
		relevantitems.addAll((LinkedList<TweetReference>)sfs.getList());
	}
	
	public LinkedList<TweetReference> more() throws ValueNotFoundException{	
		LinkedList<TweetReference> list;
		LinkedList<TweetReference> subset;
		if(moreSfs.getHead() == null){
			initMoreSubSet();
		}
		if (moreSfs.getHead().getNextID() != null){	
			getNextMoreSubSet();
		}
		relevantitemsMore.removeAll(displayed);
		list = new LinkedList<TweetReference>(relevantitemsMore);

		if(list.size()>CHUNKSIZE){
			subset = new LinkedList<TweetReference>(list.subList(0, CHUNKSIZE-1));
		}
		else if(list.size()>0){
			subset = list;
		}
		else{
			subset = new LinkedList<TweetReference>();
		}		
		
		displayed.addAll(subset);
		relevantitemsMore.removeAll(subset);
		return subset;					
	}



	public LinkedList<TweetReference> all() throws ValueNotFoundException{
		initSubSet();
		while(sfs.getHead().getNextID() != null){
			getNextSubSet();
		}
		return new LinkedList<TweetReference>(relevantitems);
	}



	//Get the new tweets added to the head
	public LinkedList<TweetReference> refresh() throws ValueNotFoundException{
		initSubSet();
		SortedSet<TweetReference> newitems = new TreeSet<TweetReference>(sfs.getList());
		
		while (newitems.removeAll(displayed) && sfs.getHead().getNextID() != null){
			getNextSubSet();
			newitems.addAll(sfs.getList());
		}
		displayed.addAll(newitems);		
		return new LinkedList<TweetReference>(newitems);
	}


	public void GetNextSubFragmentedSet(SubFragmentedSet<TweetReference> sfs, SortedSet<TweetReference> relevantitems) throws ValueNotFoundException{
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
		relevantitems.addAll((LinkedList<TweetReference>)sfs.getList());
	}
	
	public void initSubFragmentedSet(SubFragmentedSet<TweetReference> sfs, SortedSet<TweetReference> relevantitems) throws ValueNotFoundException{
		try {
			moreSfs.getSubset(setID, dhtHandler);
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
		relevantitemsMore.addAll((LinkedList<TweetReference>)moreSfs.getList());
	}
	//Cut the list head if she is too big in smaller list
	public boolean cutListHead() throws ValueNotFoundException, BadSecretException{
		SubFragmentedSet<TweetReference> firstone = new SubFragmentedSet<TweetReference>();		
		SortedSet<TweetReference> tfs = new TreeSet<TweetReference>();
		initSubFragmentedSet(firstone, tfs);

		if (firstone.getList().size() < CHUNKSIZE){
			return false;
		}

		SortedSet<TweetReference> items = new TreeSet<TweetReference>();
		items.addAll(firstone.getList());
		LinkedList<TweetReference> list = new LinkedList<TweetReference>(items);

		String nextID = firstone.getHead().getNextID();

		while (list.size() > CHUNKSIZE){
			SetHeader newheader = new SetHeader(nextID, firstone.getHead().getType(), firstone.getHead().getName());
			LinkedList<TweetReference> subset = (LinkedList<TweetReference>) list.subList(list.size()-(CHUNKSIZE+1), list.size()-1);
			list.remove(subset);

			nextID = dhtHandler.createSet();

			dhtHandler.addSecure(nextID, Translator.ObjectToString(newheader));
			for(TweetReference item : subset){
				add(nextID, item);
				remove(nextID, item);
			} 
		}
		SetHeader newheader = new SetHeader(nextID, firstone.getHead().getType(), firstone.getHead().getName());
		//remove old header from first set
		remove(firstone.getHead()); 
		//add new header
		add(newheader);
		firstone.setHead(newheader);
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
		//TODO Not implemented yet
		throw new NoSuchMethodError();
	}

	public void reset(){
		relevantitemsMore = new TreeSet<TweetReference>();
		displayed = new HashSet<TweetReference>();
		relevantitems = new TreeSet<TweetReference>();
		moreSfs = new SubFragmentedSet<TweetReference>();
		sfs = new SubFragmentedSet<TweetReference>();
	}

	public Set<TweetReference> getAll() throws ValueNotFoundException {
		return new TreeSet<TweetReference>(all());
	}

	public void remove(Object t) throws ValueNotFoundException,
			BadSecretException {
		//TODO this is not good, this method should first look for the right set from which remove the reference
		throw new NotImplementedException();
	}

	public void add(Object myRef) throws BadSecretException {
		String myStr = Translator.ObjectToString(myRef);
		dhtHandler.addSecure(setID, myStr);
	}
}
