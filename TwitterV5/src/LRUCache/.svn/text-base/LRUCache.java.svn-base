package LRUCache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LRUCache<K,V> implements CacheAPI<K,V>{
	
	private LinkedHashMap<K, V> map;
	private static final float loadFactor = 0.75f;
	private final int MAX_ENTRIES;
	
	public LRUCache(int nbrEntries){
		map = new LinkedHashMap<K, V>((int) Math.ceil(nbrEntries/loadFactor)+1,loadFactor, true){
			private static final long serialVersionUID = -6181691690048416893L;
			@Override
			protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
				return size()>MAX_ENTRIES;
			}
		};
		MAX_ENTRIES = nbrEntries;
	}
	
	@Override
	public void put(K key, V value) {
		map.put(key, value);
	}

	@Override
	public V get(K key) {
		return map.get(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Collection<Entry<K, V>> getAll() {
		return new ArrayList<Map.Entry<K, V>>(map.entrySet());
	}

	@Override
	public int maxEntries() {
		return MAX_ENTRIES;
	}

	@Override
	public boolean contain(K key) {
		return map.containsKey(key);
	}

	@Override
	public V remove(K key) {
		return map.remove(key);
	}
}
