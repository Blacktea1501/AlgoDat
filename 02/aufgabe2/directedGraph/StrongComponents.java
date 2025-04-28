package directedGraph;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Klasse für die Bestimmung aller strengen Zusammenhangskomponenten
 * mit dem Kosaraju-Sharir Algorithmus.
 *
 * @author Oliver Bittel
 * @since 9.12.2024
 * @param <V> Knotentyp.
 */
public class StrongComponents<V> {

	// comp speichert für jede Komponente die zugehörigen Knoten.
	// Die Komponenten sind durchnummeriert von 0 bis numberOfComp-1.
	private final Map<Integer, Set<V>> comp = new TreeMap<>();

	// Anzahl der Komponenten:
	private int numberOfComp = 0;

	// ...


	/**
	 * Ermittelt alle strengen Komponenten mit
	 * dem Kosaraju-Sharir Algorithmus.
	 * @param g gerichteter Graph.
	 */
	public StrongComponents(DirectedGraph<V> g) {
		// ...
	}


	/**
	 *
	 * @return Anzahl der strengen Zusammenhangskomponenten.
	 */
	public int numberOfComp() {
		return numberOfComp;
	}

	/**
	 * Liefert alle Knoten der i-ten strengen Zusammenhangskomponente zurück.
	 * Die Komponenten sind durchnummeriert von 0 bis numberOfComp()-1.
	 * @return alle Knoten der i-ten strengen Zusammenhangskomponente.
	 */
	public Set<V> getComp(int i) {
		return comp.get(i);
	}


	@Override
	public String toString() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
		
	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(1,3);
		g.addEdge(2,1);
		g.addEdge(2,3);
		g.addEdge(3,1);
		
		g.addEdge(1,4);
		g.addEdge(5,4);
		
		g.addEdge(5,7);
		g.addEdge(6,5);
		g.addEdge(7,6);
		
		g.addEdge(7,8);
		g.addEdge(8,2);
		
		StrongComponents<Integer> sc = new StrongComponents<>(g);
		
		System.out.println(sc.numberOfComp());  // 4
		
		System.out.println(sc);
			// Component 0: 5, 6, 7, 
        	// Component 1: 8, 
            // Component 2: 1, 2, 3, 
            // Component 3: 4, 
	}
}
