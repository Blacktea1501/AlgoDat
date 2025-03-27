package dictionary;

import java.util.Hashtable;
import java.util.Iterator;

public class LinkedHashDictionary<K, V> implements Dictionary<K, V>{

//    LinkedHashDictionary verwendet als Implementierung eine Hashtabelle mit linear verketteten
//    Listen. Achten Sie darauf, dass die Größe der Hashtabelle eine Primzahl ist. Wird ein bestimmter
//    Füllungsgrad (load factor) z.B. von 2 überschritten, dann wird die Tabelle vergrößert, so dass die
//    neue Größe etwa doppelt so groß und wieder eine Primzahl ist. Die Daten werden dann sofort
//    umkopiert.

    private Hashtable<K, V> hashtable;

    public LinkedHashDictionary(int capacity) {
        this.hashtable = new Hashtable<>(capacity);
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
        return this.hashtable.size();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }
}