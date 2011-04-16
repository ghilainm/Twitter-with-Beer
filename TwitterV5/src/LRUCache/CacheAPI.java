package LRUCache;
import java.util.Collection;
import java.util.Map;

public interface CacheAPI<K,V> {
	public void put(K key, V value);
	public V get(K key);
	public int size();
	public void clear();
	public Collection<Map.Entry<K, V>> getAll();
	public int maxEntries();
	public boolean contain(K key);
	public V remove(K key);
}
