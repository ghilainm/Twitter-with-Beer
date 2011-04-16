package TwitterApiImplementation;

import java.util.HashMap;
import java.util.LinkedList;

import TwitterAPIExceptions.ThisListAlreadyExistException;
import TwitterAPIExceptions.ThisListDoesNotExistsException;

import DHT.DhtUtilsAPI;
import DHTExceptions.BadSecretException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;

public class ListSet {
	protected String ListSetID;
	protected DhtUtilsAPI dhtHandler;
	protected HashMap<String, TweetFragmentedSet> listStore;
	
	public ListSet(String ListSetID, DhtUtilsAPI dhtHandler) throws ValueNotFoundException{
		this.ListSetID = ListSetID;
		this.dhtHandler = dhtHandler;
		this.listStore = new HashMap<String, TweetFragmentedSet>();
		initListStore();
	}
	
	private void initListStore() throws ValueNotFoundException{
		for(ListSetReference e : getListReferences())
			listStore.put(e.getListName(), new TweetFragmentedSet(e.getTweetSetID(), dhtHandler));
	}
	
	
	public LinkedList<ListSetReference> getListReferences(){

		LinkedList<ListSetReference> result = new LinkedList<ListSetReference>();

		LinkedList<String> entries;
		try {
			entries = dhtHandler.getDht().readSet(ListSetID);
		} catch (ValueNotFoundException e) {
			System.out.println("Can't get ListSet");
			e.printStackTrace();
			return result;
		}		

		for(String entry :entries ){
			ListSetReference lse = (ListSetReference) Translator.StringToObject(entry);
			result.add(lse);
		}

		return result;
	}
	
	public ListSetReference match(LinkedList<ListSetReference> lseList, String ListName){
		for(ListSetReference lse: lseList){
			if(lse.getListName().equals(ListName)){
				return lse;
			}
		}
		return null;
	}
	
	
	public void addList(String newListName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, ThisListAlreadyExistException{
		if(listStore.containsKey(newListName))
			throw new ThisListAlreadyExistException();
		SubFragmentedSet<TweetReference> tweets = new SubFragmentedSet<TweetReference>();
		String newTweetSetID =  dhtHandler.createSet();
		SetHeader newTweetSetHeader = new SetHeader(null, Constants.getTweetType(), newListName);
		tweets.postHeader(newTweetSetID, dhtHandler, newTweetSetHeader);
		listStore.put(newListName, new TweetFragmentedSet(newTweetSetID, dhtHandler));

		ListSetReference lse = new ListSetReference(newListName, newTweetSetID);
		String newStr = Translator.ObjectToString(lse);
		dhtHandler.addSecure(ListSetID,newStr);
		
	}
	
	public void removeList(String ListName) throws SecretProtectedException, BadSecretException, ValueNotFoundException, ThisListDoesNotExistsException{
		
		// Delete content from the list
		TweetFragmentedSet toremove = listStore.get(ListName);
		if(toremove==null)
			throw new ThisListDoesNotExistsException("This list does not exists "+ListName);
		dhtHandler.deleteSet(toremove.getSetID());	

		// Remove reference in set
		ListSetReference lse = new ListSetReference(ListName, toremove.getSetID());
		String newStr = Translator.ObjectToString(lse);
		dhtHandler.removeFromSetSecure(ListSetID,newStr);
		
		// Remove in local cache
		listStore.remove(ListName);	
	}
	
	
}
