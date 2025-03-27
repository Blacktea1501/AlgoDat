package dictionary;

import java.util.Iterator;

public class OpenHashDictionary<K, V> implements Dictionary<K, V>{

    private int capacity;

    public OpenHashDictionary(int capacity) {
        this.capacity = 0;
    }

    @Override
    public V insert(K key, V value) {
        return null;
    }

    @Override
    public V search(K key) {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public int size() {
        return this.capacity;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }
}
