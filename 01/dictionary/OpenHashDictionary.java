package dictionary;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class OpenHashDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    Entry<K, V>[] tab;
    int size;
    final Entry<K,V> DELETED = new Entry<>(null, null);

    public OpenHashDictionary(int capacity) {
        this.tab = new Entry[capacity];
        this.size = 0;
    }

    @Override
    public V remove(K key) {

        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }

        int index = searchAdr(key);
        if (index != -1 && tab[index] != null && tab[index] != DELETED) {
            V value = tab[index].getValue();
            tab[index] = DELETED;
            size--;
            return value;
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

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value must not be null");
        }

        if (0.66 <= (double) this.size / this.tab.length) {
            resize();
        }
        int index = searchAdr(key);
        if (index != -1) {
            if (tab[index] == DELETED || tab[index] == null) {
                tab[index] = new Entry<>(key, value);
                size++;
                return null;
            } else {
                V oldValue = tab[index].getValue();
                tab[index].setValue(value);
                return oldValue;
            }
        }

        return null;
    }


    private int probe(int j, K key) {
        return (hash(key) + j * j) % tab.length;
    }

    private int searchAdr(K key) {
        if (key == null){
            throw new IllegalArgumentException("Key must not be null");
        }
        int candidate = -1;
        int j = 0;
        while (j < tab.length) {
            int index = (hash(key) + (j * j)) % tab.length;
            if (tab[index] == null) {
                return candidate != -1 ? candidate : index;
            } else if (tab[index] == DELETED) {
                if (candidate == -1) {
                    candidate = index;
                }
            } else if (tab[index].getKey().equals(key)) {
                return index;
            }
            j++;
        }
        return candidate;

    }

    private void resize() {
        int newCapacity = nextPrime(this.tab.length * 2);
        Entry<K, V>[] newTab = new Entry[newCapacity];

        // mit sondierung
        for (Entry<K, V> kvEntry : this.tab) {
            if (kvEntry != null && kvEntry != DELETED) {
                int j = 0;
                while (j < newTab.length) {
                    int index = (Math.abs(kvEntry.getKey().hashCode()) % newTab.length + j * j) % newTab.length;
                    if (newTab[index] == null) {
                        newTab[index] = kvEntry;
                        break;
                    }
                    j++;
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

        int adr = searchAdr(key);

        if (adr != -1 && this.tab[adr] != null && this.tab[adr] != DELETED) {
            return tab[adr].getValue();
        }

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
            if (tab[i] != null && tab[i] != DELETED) {
                sb.append(i).append(": ").append(tab[i].getKey()).append(" -> ").append(tab[i].getValue()).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<Entry<K, V>>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                while (index < tab.length && tab[index] == null) {
                    index++;
                }
                return index < tab.length;
            }

            @Override
            public Entry<K, V> next() {
                if (!hasNext()) {
                    throw new IllegalStateException("No more elements");
                }
                return tab[index++];
            }
        };
    }
}