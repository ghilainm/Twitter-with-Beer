package TwitterApiImplementation;

import java.util.LinkedList;
import java.util.Set;

import DHTExceptions.BadSecretException;
import DHTExceptions.ValueNotFoundException;

public interface FragmentedSetAPI<T> {
	public void deleteSet();
	public LinkedList<T> more() throws ValueNotFoundException;
	
	//A call to this function won't reload the all set but update the actual one
	//by adding the new elements
	public LinkedList<T> all() throws ValueNotFoundException;
	public Set<T> getAll() throws ValueNotFoundException;
	public void refreshAll() throws ValueNotFoundException;
	public LinkedList<T> refresh() throws ValueNotFoundException;
	public boolean cutListHead() throws ValueNotFoundException;
	public void remove(T t)throws ValueNotFoundException, BadSecretException;
	
	//This method the more and refresh function
	public void reset() throws ValueNotFoundException;
	public void add(T myRef) throws BadSecretException;
}
