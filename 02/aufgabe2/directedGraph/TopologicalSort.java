// O. Bittel;
// 30.07.2024

package directedGraph;

import java.security.cert.CollectionCertStoreParameters;
import java.util.*;

/**
 * Klasse zur Erstellung einer topologischen Sortierung.
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class TopologicalSort<V> {
    private List<V> ts = new LinkedList<>(); // topologisch sortierte Folge
	private boolean hasCycle = false; // Flag zur Zykluserkennung

	private Set<V> visited;
	private Set<V> onStack;

	/**
	 * Führt eine topologische Sortierung für g mit Tiefensuche durch.
	 * @param g gerichteter Graph.
	 */
	public TopologicalSort(DirectedGraph<V> g) {
		visited = new HashSet<>();
		onStack = new HashSet<>();

		for (V v : g.getVertexSet()) {
			if (!visited.contains(v)) {
				dfs(g, v);
			}
		}

		if (hasCycle) {
			ts.clear();
		}
    }

	private void dfs(DirectedGraph<V> g, V u){
		visited.add(u);
		onStack.add(u);


		for (V v : g.getSuccessorVertexSet(u)) {
			if (hasCycle) {
				return; // Zyklus bereits gefunden, Abbruch
			}
			if (!visited.contains(v)) {
				dfs(g, v);
			} else if (onStack.contains(v)) {
				// Rückwärtskante gefunden, Graph hat einen Zyklus
				hasCycle = true;
				return;
			}
		}

		onStack.remove(u); // Knoten u verlässt den Rekursionsstack
		ts.add(0, u);      // Füge u am Anfang der Liste hinzu (Post-Order-Verarbeitung)
	}
    
	/**
	 * Liefert eine nicht modifizierbare Liste (unmodifiable view) zurück,
	 * die topologisch sortiert ist.
	 * @return topologisch sortierte Liste, falls topologsche Sortierung möglich ist, sonst null.
	 */
	public List<V> topologicalSortedList() {
        return ts.isEmpty() ? null : Collections.unmodifiableList(ts);
    }
    

	public static void main(String[] args) {
		test1();
		test2();
	}

	private static void test1() {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1, 3);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(3, 5);
		g.addEdge(4, 6);
		g.addEdge(5, 6);
		g.addEdge(6, 7);
		//g.addEdge(7, 1); // Kante führt zu Zyklus

		TopologicalSort<Integer> ts = new TopologicalSort<>(g);
		
		if (ts.topologicalSortedList() != null) {
			System.out.println("Topologische Sortierung:");
			System.out.println(ts.topologicalSortedList()); // [2, 1, 3, 5, 4, 6, 7]
		}
	}

	private static void test2() {
		// Morgendliches Anziehen:
		DirectedGraph<String> anziehGraph = new AdjacencyListDirectedGraph<>();
		anziehGraph.addEdge("Socken", "Schuhe");
		anziehGraph.addEdge("Unterhose", "Hose");
		anziehGraph.addEdge("Hose", "Schuhe");
		anziehGraph.addEdge("Hose", "Gürtel");
		anziehGraph.addEdge("Unterhemd", "Hemd");
		anziehGraph.addEdge("Hemd", "Pulli");
		anziehGraph.addEdge("Pulli", "Mantel");
		anziehGraph.addEdge("Gürtel", "Mantel");
		anziehGraph.addEdge("Mantel", "Schal");
		anziehGraph.addEdge("Schuhe", "Handschuhe");
		anziehGraph.addEdge("Schal", "Handschuhe");
		anziehGraph.addEdge("Mütze", "Handschuhe");

		TopologicalSort<String> ts = new TopologicalSort<>(anziehGraph);

		if (ts.topologicalSortedList() != null) {
			System.out.println("Topologische Sortierung:");
			System.out.println(ts.topologicalSortedList());
		}
	}
}
