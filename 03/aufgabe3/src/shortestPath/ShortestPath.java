// O. Bittel;
// 25.3.2021; jetzt mit IndexMinPq
// 30.06.2024; Anpassung auf ungerichtete Graphen

package shortestPath;

import undirectedGraph.*;
import sim.SYSimulation;
import java.awt.Color;
import java.util.*;
import java.util.List;

// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 30.06.2024
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {
	
	SYSimulation sim = null;
	
	Map<V,Double> dist; 		// Distanz für jeden Knoten
	Map<V,V> pred; 				// Vorgänger für jeden Knoten
	IndexMinPQ<V,Double> cand; 	// Kandidaten als PriorityQueue PQ

	private UndirectedGraph<V> graph;

	private Heuristic<V> heuristic;


	private V start;
	private V goal;

	/**
	 * Konstruiert ein Objekt, das im Graph g kürzeste Wege
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch 
	 * mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(UndirectedGraph<V> g, Heuristic<V> h) {
		dist = new HashMap<>();
		pred = new HashMap<>();
		cand = new IndexMinPQ<>();

		this.graph = g;
		this.heuristic = h;
	}

	/**
	 * Diese Methode sollte nur verwendet werden, 
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {
		// für später
		// boolean isSimulation = this.sim != null; // true, wenn simulation aktiviert wurde

		this.start = s;
		this.goal = g;
		boolean isAstar = heuristic != null; // true, wenn A*-Verfahren


		dist.clear();
		pred.clear();
		cand.clear(); // Kandidatenliste leeren, falls Methode mehrmals aufgerufen wird

		// Initialisierung
		for (V v : graph.getVertexSet()) {
			dist.put(v, Double.POSITIVE_INFINITY);
			pred.put(v, null);
		}
		dist.put(s, 0.0);
		if (isAstar) {
			cand.add(s, heuristic.estimatedCost(s, g));  // f(s) = g(s)+h(s) = 0 + h(s)
		} else {
			cand.add(s, 0.0);
		}

		// Hauptschleife
		while (!cand.isEmpty()) {
			V u = cand.removeMin(); // Knoten mit minimaler Distanz
			System.out.println("Besuche " + u + " mit d = " + dist.get(u));

			if (sim != null) {
				try {
					sim.visitStation((Integer) u, Color.BLUE); // Knoten animieren
				} catch (ClassCastException e) {
					System.err.println("Animationsfehler: Knotentyp ist kein Integer.");
				}
			}

			if (u.equals(g)) {
				return; // Ziel erreicht
			}

			// Für jeden Nachbarn von u
			for (V v : graph.getNeighborSet(u)) {
				double weightUV = graph.getWeight(u, v);
				double alternative = dist.get(u) + weightUV;
				double estimatedCost = alternative;

				if (isAstar){
					estimatedCost = alternative + heuristic.estimatedCost(v, g);
				}

				if (dist.get(v) == Double.POSITIVE_INFINITY) {
					dist.put(v, alternative);    // Wichtig: dist = g(v), also ohne heuristik
					pred.put(v, u);
					cand.add(v, estimatedCost);
				} else if (alternative < dist.get(v)) {
					dist.put(v, alternative);
					pred.put(v, u);
					cand.change(v, estimatedCost);
				}
			}
		}
	}



	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		if (start == null || goal == null) {
			throw new IllegalArgumentException("No path found.");
		}

		List<V> path = new ArrayList<>();
		V current = goal;
		while (current != null) {
			path.add(current);
			current = pred.get(current);
		}

		if (!path.contains(start)){
			throw new IllegalArgumentException("Kein kürzester Weg berechnet worden.");
		}

		Collections.reverse(path);
		return path;
	}

	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		if (start == null || goal == null) {
			throw new IllegalArgumentException("No path found.");
		}
		return dist.get(goal);
	}

}
