package shortestPath;

import undirectedGraph.*;
import java.io.FileNotFoundException;
import sim.SYSimulation;
import java.awt.Color;
import java.io.IOException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Kürzeste Wege im Scotland-Yard Spielplan mit A* und Dijkstra.
 * @author Oliver Bittel
 * @since 30.06.2024
 */
public class ScotlandYard {
	/**
	 * Scotland-Yard Anwendung.
	 * @param args wird nicht verwendet.
	 * @throws FileNotFoundException
	 */
	private static UndirectedGraph<Integer> syGraph; // Scotland Spielplan als Graph
	private static SYSimulation sim; // Scotland Yard Simulator
	private static ShortestPath<Integer> sySpAStar; // A*
	static {
		try {
			syGraph = getGraph();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		try {
			sim = new SYSimulation();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			sySpAStar = new ShortestPath<>(syGraph, new ScotlandYardHeuristic());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	private static ShortestPath<Integer> sySpDijkstra = new ShortestPath<>(syGraph, null); // Dijkstra

	/**
	 * Scotland-Yard Anwendung.
	 * @param args wird nicht verwendet.
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		shortestPath(65, 157, 9.0);
		shortestPath(7, 190, 24.0);
		shortestPath(1, 17, 17.0);
		shortestPath(1, 175, 25.0);
		shortestPath(1, 173, 22.0);

		shortestPathWithSimulation(1, 173, 22.0);
	}

	private static void shortestPath(Integer u, Integer v, Double dist) {
		sySpDijkstra.searchShortestPath(u, v);
		sySpAStar.searchShortestPath(u, v);
		System.out.println();
		System.out.println("Distance from " + u + " to " + v + ": " + dist);
		System.out.println("Distance (Dijkstra) = " + sySpDijkstra.getDistance());
		System.out.println(sySpDijkstra.getShortestPath());
		System.out.println("Distance (A*) = " + sySpAStar.getDistance());
		System.out.println(sySpAStar.getShortestPath());
	}

	private static void shortestPathWithSimulation(Integer u, Integer v, Double dist) {
		sySpDijkstra.setSimulator(sim);
		sim.startSequence("Shortest path (Dijkstra) from " + u + " to " + v);
		sySpDijkstra.searchShortestPath(u, v);
		List<Integer> sp = sySpDijkstra.getShortestPath();
		int a = -1;
		for (int b : sp) {
			if (a != -1)
				sim.drive(a, b, Color.RED.darker());
			sim.visitStation(b);
			a = b;
		}
		sim.stopSequence();

		// A* mit Animation
		sySpAStar.setSimulator(sim);
		sim.startSequence("Shortest path (A*) from " + u + " to " + v);
		sySpAStar.searchShortestPath(u, v);
		sp = sySpAStar.getShortestPath();
		a = -1;
		for (int b : sp) {
			if (a != -1)
				sim.drive(a, b, Color.RED.darker());
			sim.visitStation(b);
			a = b;
		}
		sim.stopSequence();
	}


	/**
	 * Fabrikmethode zur Erzeugung eines gerichteten Graphens für den Scotland-Yard-Spielplan.
	 * <p>
	 * Liest die Verbindungsdaten von der Datei ScotlandYard_Kanten.txt.
	 * Für die Verbindungen werden folgende Gewichte angenommen:
	 * U-Bahn = 5, Taxi = 2 und Bus = 3.
	 * Falls Knotenverbindungen unterschiedliche Beförderungsmittel gestatten,
	 * wird das billigste Beförderungsmittel gewählt. 
	 * Bei einer Vebindung von u nach v wird in den gerichteten Graph sowohl 
	 * eine Kante von u nach v als auch von v nach u eingetragen.
	 * @return Gerichteter und Gewichteter Graph für Scotland-Yard.
	 * @throws FileNotFoundException
	 */
	public static UndirectedGraph<Integer> getGraph() throws FileNotFoundException {

		UndirectedGraph<Integer> sy_graph = new AdjacencyListUndirectedGraph<>();
		Scanner in = new Scanner(new File("data/ScotlandYard_Kanten.txt"));

		for (int v = 1; v <= 199; v++)
			sy_graph.addVertex(v);

		while (in.hasNextInt()) {
			int u = in.nextInt();
			int v = in.nextInt();
			String s = in.next();
			int g;
			if (s.equals("Taxi"))
				g = 2;
			else if (s.equals("Bus"))
				g = 3;
			else
				g = 5;
			
			if (!sy_graph.containsEdge(u, v) || g < sy_graph.getWeight(u, v))
				sy_graph.addEdge(u, v, g);
		}
		
		// Test, ob alle Kanten eingelesen wurden: 
		System.out.println("Number of Vertices: " + sy_graph.getNumberOfVertexes());  // 199
		System.out.println("Number of Edges:    " + sy_graph.getNumberOfEdges());	  // 431
		double wSum = 0.0;
		for (Integer v : sy_graph.getVertexSet())
			for (Integer w : sy_graph.getNeighborSet(v))
				wSum += sy_graph.getWeight(v,w);
		System.out.println("Sum of all Weights: " + wSum);		// 1972.0

		return sy_graph;
	}
}


