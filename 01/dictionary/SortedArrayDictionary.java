package dictionary;

import java.util.ArrayList;
import java.util.Iterator;

// SortedArrayDictionary (siehe Web-Seite) implementiert ein Dictionary mit einem Feld, in dem
// die Datensätze lückenlos und sortiert gespeichert werden. Für die Suche wird binäre Suche
// eingesetzt

public class SortedArrayDictionary<K, V> implements Dictionary<K, V> {

    private ArrayList<Dictionary<K,V>> keys;

    public SortedArrayDictionary() {
        this.keys = new ArrayList<>();
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
        return this.keys.size();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }
}
