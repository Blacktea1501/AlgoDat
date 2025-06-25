// O. Bittel;
// 22.11.2024

package directedGraph;

import java.util.*;

/**
 * Klasse zur Ermittlung von gerichteten Zyklen.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 30.7.2024
 */
public class DirectedCycle<V> {
    // ...
    private final List<List<V>> cycles = new LinkedList<>();    // enthält alle gefundenen Zyklen
    private final DirectedGraph<V> myGraph;

    private Set<V> visited;    // enthält alle besuchten Knoten
    private Set<V> onStack;
    private LinkedList<V> currentPath;


    /**
     * Führt eine Tiefensuche für g durch und ermittelt dabei Zyklen.
     * Vorsicht: bei Graphen mit Zyklen werden nicht alle Zyklen gefunden.
     *
     * @param g gerichteter Graph.
     */
    public DirectedCycle(DirectedGraph<V> g) {
        myGraph = g;
        visited = new HashSet<>();
        onStack = new HashSet<>();
        currentPath = new LinkedList<>();

        for (V v : myGraph.getVertexSet()) {
            if (!visited.contains(v)) {
                dfs(v);
            }
        }
    }

    void dfs(V v) {
        visited.add(v);
        onStack.add(v);
        currentPath.add(v);

        for (V w : myGraph.getSuccessorVertexSet(v)) {
            if (!visited.contains(w)) {
                dfs(w);
            } else if (onStack.contains(w)) {
                // Zyklus mit hilfe von path ausgeben
                List<V> cycle = new LinkedList<>();
                boolean foundStartOfCycle = false;
                for (V nodeInPath : currentPath){
                    if (nodeInPath.equals(w)) {
                        foundStartOfCycle = true;
                    }
                    if (foundStartOfCycle) {
                        cycle.add(nodeInPath);
                    }
                }
                cycle.add(w);
                cycles.add(cycle);
            }

        }


        }


        /**
         * Liefert alle gefundenen Zyklen zurück.
         * @return alle gefundenen Zyklen falls ein Zyklus vorhanden, sonst null.
         */
        public List<List<V>> getCycle () {
            return cycles.isEmpty() ? null : Collections.unmodifiableList(cycles);
        }

        /**
         * Prüft ob Zyklus vorhanden ist.
         * @return true, falls Zyklus vorhanden ist, sonst falls.
         */
        public boolean hasCycle () {
            return !cycles.isEmpty();
        }


        public static void main (String[]args){
            DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
            g.addEdge(0, 1);
            g.addEdge(1, 2);
            g.addEdge(2, 5);
            g.addEdge(5, 1);

            g.addEdge(2, 6);
            g.addEdge(2, 5);
            g.addEdge(6, 4);
            g.addEdge(4, 6);

            g.addEdge(6, 3);
            g.addEdge(3, 4);

            DirectedCycle<Integer> dc = new DirectedCycle<>(g);
            System.out.println(dc.hasCycle());
            System.out.println(dc.getCycle());
        }
    }
