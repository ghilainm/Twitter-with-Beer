package TwitterApiImplementation;

public interface comparableItem extends Comparable<Object>{
	public abstract int compareTo(Object arg0);
	
	public boolean equals(Object t);
}