package floyd;

import java.util.*;

// Ermittelt für jeden Knoten v seine Relevanz.
// Die Relevanz eines Knotens v ist die Anzahl aller kürzesten Wege, die über v führen.
//
public class Relevance<V> {
    private Map<V, Integer> relevance = new HashMap<>(); // Relevanz für jeden Knoten v.
    private List<V> allNodesSortedByRelevance;           // Liste aller Knoten, sortiert nach Relevanz

    public Relevance(Floyd<V> floyd) {
        List<V> nodelist = new ArrayList<>(floyd.getAllNodes());

        // Initialisiere Relevanz aller Knoten mit 0
        for (V v : nodelist) {
            relevance.put(v, 0);
        }

        // Für jedes Paar (start, end) von Knoten
        for (V start : nodelist) {
            for (V end : nodelist) {
                if (start.equals(end))
                    continue; // Ignoriere triviale Wege

                List<V> path = floyd.getShortestPath(start, end);
                if (path == null || path.isEmpty())
                    continue;

                // Erhöhe Relevanz für alle Zwischenknoten (ohne Start und Endknoten)
                for (V node : path) {
                    if (!node.equals(start) && !node.equals(end)) {
                        relevance.put(node, relevance.get(node) + 1);
                    }
                }
            }
        }

        // Sortiere Knoten nach Relevanz absteigend
        allNodesSortedByRelevance = new ArrayList<>(nodelist);
        allNodesSortedByRelevance.sort((v1, v2) -> relevance.get(v2) - relevance.get(v1));
    }


    List<V> getAllNodesSortedByRelevance() {
        return Collections.unmodifiableList(allNodesSortedByRelevance);
    }

    int getRelevance(V v) {
        return relevance.get(v);
    }
}
