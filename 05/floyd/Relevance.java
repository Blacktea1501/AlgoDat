package floyd;

import java.util.*;

// Ermittelt für jeden Knoten v seine Relevanz.
// Die Relevanz eines Knotens v ist die Anzahl aller kürzesten Wege, die über v führen.
//
public class Relevance<V> {
    private Map<V, Integer> relevance = new HashMap<>(); // Relevanz für jeden Knoten v.
    private List<V> allNodesSortedByRelevance;           // Liste aller Knoten, sortiert nach Relevanz

    public Relevance(Floyd<V> floyd) {
        // berechne die Relevanz für jeden Knoten v:
        // ...

        // Sortiere alle Knoten nach ihrer Relevanz:
        // ...
    }

    List<V> getAllNodesSortedByRelevance() {
        return Collections.unmodifiableList(allNodesSortedByRelevance);
    }

    int getRelevance(V v) {
        return relevance.get(v);
    }
}
