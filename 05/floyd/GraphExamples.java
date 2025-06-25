/*
 * Fabrikmethoden für Beispiele von gerichteten Graphen
 * 15.11.24; O. Bittel
 */

package floyd;

import directedGraph.*;

public class GraphExamples {

	public static DirectedGraph<Integer> smallGraphExample() {
		// Beispiel aus der Vorlesung S. 9-62
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(0,1, 5.0);
		g.addEdge(0,3, 4.0);
		g.addEdge(1,2, -1.0);
		g.addEdge(1,4, -2.0);
		g.addEdge(2,1, 1.0);
		g.addEdge(3,2, -2.0);
		g.addEdge(3,4, -1.0);
		g.addEdge(4,0, 1.0);
		return g;
	}

	public static DirectedGraph<String> allgaeuGraphExample() {
		DirectedGraph<String> g1 = new AdjacencyListDirectedGraph<>();;

		g1.addEdge("Kimratshofen", "Altusried", 5.0);
		g1.addEdge("Kimratshofen", "Isny", 19.0);
		g1.addEdge("Kimratshofen", "Kreuzthal", 16.0);
		g1.addEdge("Kimratshofen", "Wiggensbach", 10.0);

		g1.addEdge("Altusried", "Dietmannsried", 7.0);
		g1.addEdge("Altusried", "Wiggensbach", 8.0);
		g1.addEdge("Altusried", "Lauben", 9.0);

		g1.addEdge("Dietmannsried", "Börwang", 9.0);
		g1.addEdge("Dietmannsried", "Lauben", 4.0);
		g1.addEdge("Dietmannsried", "Kempten", 12.0);

		g1.addEdge("Börwang", "Wildwoldsried", 6.0);
		g1.addEdge("Börwang", "Kempten", 10.0);

		g1.addEdge("Wildwoldsried", "Unterthingau", 10.0);
		g1.addEdge("Wildwoldsried", "Kempten", 14.0);
		g1.addEdge("Wildwoldsried", "Betzigau", 4.0);

		g1.addEdge("Unterthingau", "Wald", 11.0);
		g1.addEdge("Unterthingau", "Betzigau", 14.0);
		g1.addEdge("Unterthingau", "Görisried", 8.0);

		g1.addEdge("Wald", "Rückholz", 8.0);

		g1.addEdge("Isny", "Kreuzthal", 16.0);
		g1.addEdge("Isny", "Wengen", 11.0);
		g1.addEdge("Isny", "Weitnau", 13.0);
		g1.addEdge("Isny", "Missen", 16.0);

		g1.addEdge("Kreuzthal", "Wiggensbach", 14.0);
		g1.addEdge("Kreuzthal", "Buchenberg", 12.0);

		g1.addEdge("Wiggensbach", "Lauben", 9.0);
		g1.addEdge("Wiggensbach", "Kempten", 8.0);
		g1.addEdge("Wiggensbach", "Buchenberg", 8.0);

		g1.addEdge("Lauben", "Kempten", 7.0);

		g1.addEdge("Kempten", "Betzigau", 9.0);
		g1.addEdge("Kempten", "Buchenberg", 10.0);
		g1.addEdge("Kempten", "Durach", 8.0);
		g1.addEdge("Kempten", "Waltenhofen", 11.0);

		g1.addEdge("Betzigau", "Görisried", 21.0);
		g1.addEdge("Betzigau", "Durach", 6.0);

		g1.addEdge("Görisried", "Durach", 16.0);
		g1.addEdge("Görisried", "Oy", 9.0);
		g1.addEdge("Görisried", "Rückholz", 12.0);

		g1.addEdge("Wengen", "Buchenberg", 8.0);
		g1.addEdge("Wengen", "Waltenhofen", 16.0);

		g1.addEdge("Buchenberg", "Waltenhofen", 8.0);

		g1.addEdge("Durach", "Oy", 13.0);
		g1.addEdge("Durach", "Waltenhofen", 7.0);
		g1.addEdge("Durach", "Moosbach", 11.0);

		g1.addEdge("Oy", "Rückholz", 11.0);
		g1.addEdge("Oy", "Waltenhofen", 20.0);
		g1.addEdge("Oy", "Moosbach", 13.0);
		g1.addEdge("Oy", "Nesselwang", 5.0);
		g1.addEdge("Oy", "Wertach", 8.0);

		g1.addEdge("Rückholz", "Nesselwang", 5.0);
		g1.addEdge("Rückholz", "Pfronten", 16.0);

		g1.addEdge("Weitnau", "Waltenhofen", 17.0);
		g1.addEdge("Weitnau", "Missen", 9.0);
		g1.addEdge("Weitnau", "Martinszell", 17.0);

		g1.addEdge("Waltenhofen", "Martinszell", 5.0);

		g1.addEdge("Moosbach", "Martinszell", 8.0);
		g1.addEdge("Moosbach", "Rettenberg", 12.0);
		g1.addEdge("Moosbach", "Wertach", 16.0);

		g1.addEdge("Nesselwang", "Wertach", 10.0);
		g1.addEdge("Nesselwang", "Pfronten", 10.0);

		g1.addEdge("Missen", "Martinszell", 17.0);
		g1.addEdge("Missen", "Immenstadt", 12.0);

		g1.addEdge("Immenstadt", "Rettenberg", 7.0);

		g1.addEdge("Rettenberg", "Wertach", 11.0);

		DirectedGraph<String> g = new AdjacencyListDirectedGraph<>();
		for (var v : g1.getVertexSet()) {
			for (var w : g1.getSuccessorVertexSet(v)) {
				g.addEdge(v, w, g1.getWeight(v, w));
				g.addEdge(w, v, g1.getWeight(v, w));
			}
		}

		return g;
	}
	
	public static void main(String[] args) {
		DirectedGraph<String> g = allgaeuGraphExample();
		for (var e : g.getSuccessorVertexSet("Waltenhofen"))
			System.out.println("Waltenhofen" + " --> " + e + " : " + g.getWeight("Waltenhofen",e));
	}
}
