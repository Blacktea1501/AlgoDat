package dictionary;

import java.util.Iterator;

// SortedArrayDictionary (siehe Web-Seite) implementiert ein Dictionary mit einem Feld, in dem
// die Datensätze lückenlos und sortiert gespeichert werden. Für die Suche wird binäre Suche
// eingesetzt

@SuppressWarnings("unchecked")
public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    // Das Array ist ein Array von Dictionaries, um die Einträge zu speichern
    private Entry<K, V>[] data;
    private int size;
    private static final char CAPACITY = 16000;


    public SortedArrayDictionary() {
        this.size = 0;
        this.data = new Entry[CAPACITY];
    }

    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);

        if(i != -1) {
            // key already exists, update value
            V oldValue = this.data[i].getValue();
            this.data[i].setValue(value);
            return oldValue;
        }

        // new key insert
        if (this.size == this.data.length) {
            // resize array
            Entry<K, V>[] newData = new Entry[this.data.length * 2];
            System.arraycopy(this.data, 0, newData, 0, this.data.length);
            this.data = newData;
        }

        int j = this.size - 1;

        while (j >= 0 && this.data[j].getKey().compareTo(key) > 0) {
            this.data[j + 1] = this.data[j];
            j--;
        }

        this.data[j + 1] = new Entry<>(key, value);
        this.size++;

        return null;
    }

    // binary search
    @Override
    public V search(K key) {
        int low = 0;
        int high = this.size - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (this.data[mid].getKey().compareTo(key) == 0) {
                return this.data[mid].getValue();
            } else if (this.data[mid].getKey().compareTo(key) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        // key not found
        return null;
    }


    // binary search for key
    private int searchKey(K key) {
        int low = 0;
        int high = this.size - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (this.data[mid].getKey().compareTo(key) < 0) {
                high = mid - 1;
            } else if (this.data[mid].getKey().compareTo(key) > 0) {
                low = mid + 1;
            } else
                return mid;
        }
        // key not found
        return -1;
    }

    @Override
    public V remove(K key) {
        int i = searchKey(key);

        if (i == -1) {
            // key not found
            return null;
        } else {
            V value = this.data[i].getValue();
            // shift elements to the left
            for (int j = i; j < this.size - 1; j++) {
                this.data[j] = this.data[j + 1];
            }
            this.data[this.size - 1] = null;
            this.size--;
            return value;
        }
    }

    @Override
    // returns how many entries are in the Array
    public int size() {
        return this.size;
    }

    public Entry<K, V> get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }
        return this.data[index];
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size; i++) {
            sb.append(this.data[i].getKey()).append(": ").append(this.data[i].getValue()).append("\n");
        }
        return sb.toString();
    }



    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public Entry<K, V> next() {
                return data[currentIndex++];
            }
        };
    }
}
