package floyd;

import java.io.PrintWriter;
import java.util.*;

import directedGraph.*;

// Ermittelt alle kürzesten Wege für gerichtete Graphen mit dem Floyd Algorithmus
public class Floyd<V> {
    private final Map<V, Map<V, Double>> dist = new TreeMap<>();    // Distanz-Matrix
    private final Map<V, Map<V, V>> pred = new TreeMap<>();         // Vorgänger-Matrix für kürzeste Wege

    public Floyd(DirectedGraph<V> g) {
        // ...
    }

    public double getDist(V v, V w) {
        return dist.get(v).get(w);
    }

    public List<V> getShortestPath(V v, V w) {
        List<V> path = new LinkedList<>();
        // ...
        return path;
    }

    public Set<V> getAllNodes() {
        return Collections.unmodifiableSet(dist.keySet());
    }

    public void print() {
        for (V i : dist.keySet()) {
            for (V j : dist.keySet())
                if (dist.get(i).get(j).equals(Double.POSITIVE_INFINITY))
                    System.out.printf("%4s","-");
                else
                    System.out.printf("%4.0f",dist.get(i).get(j));
            System.out.println();
        }
        System.out.println();

    }

    void saveAsCsv(String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            // ...
            writer.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found");
        }
}