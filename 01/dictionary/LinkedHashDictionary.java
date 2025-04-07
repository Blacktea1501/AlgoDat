package dictionary;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class LinkedHashDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V>{

//    LinkedHashDictionary verwendet als Implementierung eine Hashtabelle mit linear verketteten
//    Listen. Achten Sie darauf, dass die Größe der Hashtabelle eine Primzahl ist. Wird ein bestimmter
//    Füllungsgrad (load factor) z.B. von 2 überschritten, dann wird die Tabelle vergrößert, so dass die
//    neue Größe etwa doppelt so groß und wieder eine Primzahl ist. Die Daten werden dann sofort
//    umkopiert.

    LinkedList<Entry<K, V>>[] tab;
    int size = 0;


    @Override
    public V remove(K key) {
        var it = tab[hash(key)].iterator();
        while (it.hasNext()){
            Entry<K,V> e = it.next();
            if (e.getKey().equals(key)) {
                it.remove();
                size--;
                return e.getValue();
            }
        }
        return null;
    }

    public LinkedHashDictionary(int capacity) {
        int newCapacity = isPrime(capacity) ? capacity : nextPrime(capacity);
        this.tab = new LinkedList[newCapacity];
    }

    private int nextPrime(int n) {
        if (n <= 2) return 2;
        if (n % 2 == 0) n++;
        while (!isPrime(n)) {
            n += 2;
        }
        return n;
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;

        int sqrtN = (int) Math.sqrt(n);

        for (int i = 3; i <= sqrtN; i += 2) {
            if (n % i == 0) return false;
        }

        return true;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % tab.length;
    }


    @Override
    public V insert(K key, V value) {
        return null;
    }

    @Override
    public V search(K key) {
        for (Entry<K, V> e : tab[hash(key)]) {
            if (e.getKey().equals(key)) {
                return e.getValue();
            }
        }

        return null;
    }


    @Override
    public int size() {
        return this.tab.length;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;
            private Iterator<Entry<K, V>> currentIterator = null;

            @Override
            public boolean hasNext() {
                while (currentIterator == null || !currentIterator.hasNext()) {
                    if (currentIndex >= tab.length) {
                        return false;
                    }
                    if (tab[currentIndex] != null) {
                        currentIterator = tab[currentIndex].iterator();
                    }
                    currentIndex++;
                }
                return true;
            }

            @Override
            public Entry<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return currentIterator.next();
            }
        };
    }
}