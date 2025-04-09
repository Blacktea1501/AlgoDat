package dictionary;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class OpenHashDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V>{

    Entry<K, V>[] tab;
    int size;


    public OpenHashDictionary(int capacity) {
        this.tab = new Entry[capacity];
        this.size = 0;
    }

    @Override
    public V remove(K key) {
        var it = tab[hash(key)].iterator();
        while (it.hasNext()){
            Entry<K,V> e = it.next();
            if (e.getKey().equals(key)) {
                it.remove();
                this.size--;
                return e.getValue();
            }
        }
        return null;
    }

    private int nextPrime(int n) {
        if (n <= 2) return 2;
        if (n % 2 == 0) n++;
        while (!(isSpecialPrime(n) && isPrime(n))) {
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

    private boolean isSpecialPrime(int a) {
        return 3 == (a % 4 + 4) % 4;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % tab.length;
    }

    private int mod()


    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value must not be null");
        }

        if (0.66 <= size / tab.length) { // load factor bigger than 2
            resize();
        }
        int index = hash(key);
        int i = 0 
        while (tab[index] != null and i++ < tab.length) {
            if (tab[index].getKey().equals(key)){
                // replace value if key already exists and return old value
                V oldValue = tab[index].getValue();
                tab[index].setValue(value);
                return oldValue;
            }
            power = (int) Math.pow(-1, i);
            index = (((index + power * i*i)%tab.length) + tab.length) % tab.length;
            
        }
        if (tab[index] == null) {

            // add new entry
            tab[index] = new Entry<>(key, value);
            size++;
        }

        return null;
    }

    // THE FUNCTIONS ABOVE THIS COMMENT SHOULD WORK -- THE FUNCTIONS ABOVE THIS COMMENT SHOULD WORK -- THE FUNCTIONS ABOVE THIS COMMENT SHOULD WORK -- THE FUNCTIONS ABOVE THIS COMMENT SHOULD WORK

    private void resize() {
        int newCapacity = nextPrime(tab.length * 2);
        Entry<K, V>[] newTab = new Entry[newCapacity];

        for (Entry<K, V> entry : tab) {
            if (entry != null) {
                int newIndex = Math.abs(e.getKey().hashCode()) % newCapacity;
                if (newTab[newIndex] == null) {
                    newTab[newIndex] = new LinkedList<>();
                }
            }
        }

        this.tab = newTab;
    }

    @Override
    public V search(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        int index = hash(key);
        if (tab[index] != null) {
            for (Entry<K, V> e : tab[index]) {
                if (e.getKey().equals(key)) {
                    // key found
                    return e.getValue();
                }
            }
        }

        // key not found
        return null;
    }


    @Override
    public int size() {
        return this.tab.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] != null) {
                sb.append(i).append(": ");
                for (Entry<K, V> e : tab[i]) {
                    sb.append(e.getKey()).append("=").append(e.getValue()).append(", ");
                }
                sb.setLength(sb.length() - 2); // remove last comma and space
                sb.append("\n");
            }
        }
        return sb.toString();
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
