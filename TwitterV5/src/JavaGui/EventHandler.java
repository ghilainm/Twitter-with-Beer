package JavaGui;

import java.util.ArrayList;

public class EventHandler {
	private static final ArrayList<DeleteEventListener> deleteEventListeners = new ArrayList<DeleteEventListener>();
	
	public static void deleteEvent(String tweetID){
		for(DeleteEventListener del : deleteEventListeners){
			del.deleteEventHandler(tweetID);
		}
	}
	
	public static void addDeleteEventListener(DeleteEventListener d){
		deleteEventListeners.add(d);
	}
	
	public static void removeDeleteEventListener(DeleteEventListener d){
		deleteEventListeners.remove(d);
	}
}
