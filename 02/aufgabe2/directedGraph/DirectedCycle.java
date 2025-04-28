// O. Bittel;
// 22.11.2024

package directedGraph;

import java.util.*;

/**
 * Klasse zur Ermittlung von gerichteten Zyklen.
 * @author Oliver Bittel
 * @since 30.7.2024
 * @param <V> Knotentyp.
 */
public class DirectedCycle<V> {
	// ...
	private final List<List<V>> cycles = new LinkedList<>();	// enthält alle gefundenen Zyklen
	private final DirectedGraph<V> myGraph;

	/**
	 * Führt eine Tiefensuche für g durch und ermittelt dabei Zyklen.
	 * Vorsicht: bei Graphen mit Zyklen werden nicht alle Zyklen gefunden.
	 * @param g gerichteter Graph.
	 */
	public DirectedCycle(DirectedGraph<V> g) {
		myGraph = g;
		// ...
	}


	/**
	 * Liefert alle gefundenen Zyklen zurück.
	 * @return alle gefundenen Zyklen falls ein Zyklus vorhanden, sonst null.
	 */
	public List<List<V>> getCycle(){
		return cycles.isEmpty()? null : Collections.unmodifiableList(cycles);
	}

	/**
	 * Prüft ob Zyklus vorhanden ist.
	 * @return true, falls Zyklus vorhanden ist, sonst falls.
	 */
	public boolean hasCycle(){
		return !cycles.isEmpty();
	}


	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(0,1);
		g.addEdge(1,2);
		g.addEdge(2,5);
		g.addEdge(5,1);

		g.addEdge(2,6);
		g.addEdge(2,5);
		g.addEdge(6,4);
		g.addEdge(4,6);

		g.addEdge(6,3);
		g.addEdge(3,4);

		DirectedCycle<Integer> dc = new DirectedCycle<>(g);
		System.out.println(dc.hasCycle());
		System.out.println(dc.getCycle());
	}
}
