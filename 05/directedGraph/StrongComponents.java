package directedGraph;

import java.util.*;

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

	private Set<V> visited;
	private LinkedList<V> postOrder;


	/**
	 * Ermittelt alle strengen Komponenten mit
	 * dem Kosaraju-Sharir Algorithmus.
	 * @param g gerichteter Graph.
	 */
	public StrongComponents(DirectedGraph<V> g) {
		if (g == null){
			throw new IllegalArgumentException("Graph can not be null.");
		}

		visited = new HashSet<>();
		postOrder = new LinkedList<>();

		for (V v : g.getVertexSet()) {
			if(!visited.contains(v)) {
				dfs(g, v);
			}
		}

		DirectedGraph<V>gT = g.invert();

		visited.clear();
		numberOfComp = 0;

		for (V v : postOrder) {
			if (!visited.contains(v)){
				Set<V> currentSCC = new TreeSet<>();
				dfs2(gT, v, currentSCC);
				if (!currentSCC.isEmpty()){
					comp.put(numberOfComp, currentSCC);
					numberOfComp++;
				}
			}
		}
	}

	private void dfs(DirectedGraph<V> g, V u){
		visited.add(u);
		for (V v : g.getSuccessorVertexSet(u)) {
			if (!visited.contains(v)) {
				dfs(g, v);
			}
		}
		postOrder.addFirst(u);
	}

	private void dfs2(DirectedGraph<V> g, V u, Set<V> currentSCC){
		visited.add(u);
		currentSCC.add(u);
		for (V v : g.getSuccessorVertexSet(u)) {
			if (!visited.contains(v)) {
				dfs2(g, v, currentSCC);
			}
		}
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
		if (comp.isEmpty() && numberOfComp > 0) { // Should not happen if logic is correct
			return "No components found, but number of components is " + numberOfComp;
		}
		if (comp.isEmpty()) {
			// Check if the graph was empty initially
			// This requires access to the original graph's vertex set size,
			// which is not stored. For now, assume if comp is empty, no SCCs were formed.
			return "No strongly connected components found.";
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, Set<V>> entry : comp.entrySet()) {
			sb.append("Component ").append(entry.getKey()).append(": ");
			boolean first = true;
			for (V vertex : entry.getValue()) { // TreeSet sorgt für sortierte Ausgabe der Knoten
				if (!first) {
					sb.append(", ");
				}
				sb.append(vertex);
				first = false;
			}
			sb.append("\n");
		}
		return sb.toString().trim();
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
