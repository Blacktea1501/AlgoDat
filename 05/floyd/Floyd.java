package floyd;

import java.io.PrintWriter;
import java.util.*;

import directedGraph.*;

// Ermittelt alle kürzesten Wege für gerichtete Graphen mit dem Floyd Algorithmus
public class Floyd<V> {
    private final Map<V, Map<V, Double>> dist = new TreeMap<>();    // Distanz-Matrix
    private final Map<V, Map<V, V>> pred = new TreeMap<>();         // Vorgänger-Matrix für kürzeste Wege

    public Floyd(DirectedGraph<V> g) {
        Set<V> nodes = g.getVertexSet();

        // Initialisiere Distanz- und Vorgängermatrizen
        for (V u : nodes) {
            dist.put(u, new TreeMap<>());
            pred.put(u, new TreeMap<>());
            for (V v : nodes) {
                if (u.equals(v)) {
                    dist.get(u).put(v, 0.0);
                    pred.get(u).put(v, null);
                } else if (g.getSuccessorVertexSet(u).contains(v)) {
                    dist.get(u).put(v, g.getWeight(u, v));
                    pred.get(u).put(v, u);
                } else {
                    dist.get(u).put(v, Double.POSITIVE_INFINITY);
                    pred.get(u).put(v, null);
                }
            }
        }

        // Floyd-Warshall Algorithmus
        for (V k : nodes) {
            for (V i : nodes) {
                for (V j : nodes) {
                    double ik = dist.get(i).get(k);
                    double kj = dist.get(k).get(j);
                    double ij = dist.get(i).get(j);
                    if (ik + kj < ij) {
                        dist.get(i).put(j, ik + kj);
                        pred.get(i).put(j, pred.get(k).get(j));
                    }
                }
            }
        }

        // Prüfe auf negative Zyklen
        for (V v : nodes) {
            if (dist.get(v).get(v) < 0) {
                throw new IllegalArgumentException("Negativer Zyklus im Graphen bei Knoten " + v);
            }
        }
    }



    public double getDist(V v, V w) {
        return dist.get(v).get(w);
    }

    public List<V> getShortestPath(V v, V w) {
        LinkedList<V> path = new LinkedList<>();
        if (dist.get(v).get(w) == Double.POSITIVE_INFINITY) {
            return path; // Kein Pfad vorhanden
        }

        V current = w;
        while (!current.equals(v)) {
            path.addFirst(current);
            current = pred.get(v).get(current);
            if (current == null) return new LinkedList<>(); // Fehlerhafte Pfadinformation
        }
        path.addFirst(v);
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
            Set<V> nodes = dist.keySet();
            writer.print(";");
            for (V col : nodes) {
                writer.print(col + ";");
            }
            writer.println();
            for (V row : nodes) {
                writer.print(row + ";");
                for (V col : nodes) {
                    double d = dist.get(row).get(col);
                    if (d == Double.POSITIVE_INFINITY)
                        writer.print("-;");
                    else
                        writer.print(d + ";");
                }
                writer.println();
            }
            writer.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

}
