package TwitterApiImplementation;

import java.util.LinkedList;
import TwitterAPIExceptions.UnAuthorizedValueInSet;
import DHT.DhtUtilsAPI;
import DHTExceptions.BadSecretException;
import DHTExceptions.SecretProtectedException;
import DHTExceptions.ValueNotFoundException;

public class SubFragmentedSet<T>{

	private SetHeader head;
	private LinkedList<T> list;
	
	public SetHeader getHead() {
		return head;
	}

	public LinkedList<T> getList() {
		return list;
	}

	public void setHead(SetHeader head) {
		this.head = head;
	}

	public void setList(LinkedList<T> list) {
		this.list = list;
	}
	
	public void getSubset(String setID, DhtUtilsAPI dhtHandler) throws ValueNotFoundException, UnAuthorizedValueInSet{
		LinkedList<String> strList = dhtHandler.getDht().readSet(setID);
		parseSubSet(strList);	
	}

	@SuppressWarnings("unchecked")
	public void parseSubSet(LinkedList<String> itemLst) throws UnAuthorizedValueInSet{
		SetHeader header = null;
		LinkedList<T> list = new LinkedList<T>();
		Object temp;

		for (String item : itemLst){			
			temp = Translator.StringToObject(item);
			if(temp instanceof SetHeader){
				header = (SetHeader) temp;		
			}
			else{
				try{
					list.add((T)temp);	
				}
				catch(ClassCastException e){
					throw new UnAuthorizedValueInSet(item);
				}
			}
		}		
		setList(list);
		setHead(header);
	}

	public boolean postHeader(String setID, DhtUtilsAPI dhtHandler, SetHeader head) throws SecretProtectedException{
		String str = Translator.ObjectToString(head);
		try {
			dhtHandler.addSecure(setID, str);
		} catch (BadSecretException e) {
			e.printStackTrace();
			return false;
		}	
		return true;
	}

	public void removeHeader(String setID, DhtUtilsAPI dhtHandler, SetHeader head) throws SecretProtectedException, ValueNotFoundException{
		String str = Translator.ObjectToString(head);
		dhtHandler.getDht().remove(setID, str);	
	}
}
