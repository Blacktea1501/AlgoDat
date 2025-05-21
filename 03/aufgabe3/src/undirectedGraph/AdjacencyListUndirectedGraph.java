// O. Bittel;
// 19.03.2018

package undirectedGraph;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Implementierung von DirectedGraph mit einer doppelten TreeMap 
 * für die Nachfolgerknoten und einer einer doppelten TreeMap 
 * für die Vorgängerknoten. 
 * <p>
 * Beachte: V muss vom Typ Comparable&lt;V&gt; sein.
 * <p>
 * Entspicht einer Adjazenzlisten-Implementierung 
 * mit schnellem Zugriff auf die Knoten.
 * @author Oliver Bittel
 * @since 19.03.2018
 * @param <V> Knotentyp.
 */
public class AdjacencyListUndirectedGraph<V> implements UndirectedGraph<V> {
    // doppelte Map für die Nachfolgerknoten:
    private final Map<V, Map<V, Double>> neighbor = new TreeMap<>();
    private int numberEdge = 0;

	@Override
	public boolean addVertex(V v) {
        if (neighbor.containsKey(v))
            return false;
		neighbor.put(v, new TreeMap<>());
        return true;
    }

    @Override
    public boolean addEdge(V v, V w, double weight) {
		addVertex(v);
		addVertex(w);

		boolean ret = false;
		if (!containsEdge(v, w)) {
			ret = true;
			numberEdge++;
		}

		neighbor.get(v).put(w, weight);
		neighbor.get(w).put(v, weight);

		return ret;
	}

    @Override
    public boolean addEdge(V v, V w) {return addEdge(v, w, 1.0);}

    @Override
    public boolean containsVertex(V v) {return neighbor.containsKey(v);}

    @Override
    public boolean containsEdge(V v, V w) {return neighbor.get(v) != null && neighbor.get(v).containsKey(w);}

    @Override
    public double getWeight(V v, V w) {
        if (!containsEdge(v, w))
			throw new IllegalArgumentException();
		return neighbor.get(v).get(w);
    }

	
    @Override
    public int getDegree(V v) {
		if (neighbor.get(v) == null)
			throw new IllegalArgumentException();
		else
			return neighbor.get(v).size();
	}


    @Override
    public Set<V> getNeighborSet(V v) {
		if (neighbor.get(v) == null)
			throw new IllegalArgumentException();
		else
			return Collections.unmodifiableSet(neighbor.get(v).keySet());
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNumberOfVertexes() {
		return neighbor.size();
	}

    @Override
    public int getNumberOfEdges() {
		return this.numberEdge;
	}

    @Override
    public Set<V> getVertexSet() {
		return Collections.unmodifiableSet(neighbor.keySet()); // nicht modifizierbare Sicht
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("");
		for (V v : neighbor.keySet())
			for (V w : neighbor.get(v).keySet())
				s.append(v).append(" --> ").append(w).append(" weight = ").append(neighbor.get(v).get(w)).append("\n");
		return s.toString();
	}

		
	public static void main(String[] args) {
		UndirectedGraph<Integer> g = new AdjacencyListUndirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(1,4);
		g.addEdge(2,3);
		g.addEdge(2,6);
		g.addEdge(3,1);
		g.addEdge(3,4);
		g.addEdge(3,5);
		g.addEdge(4,5);
		g.addEdge(6,3);
		g.addEdge(7,6);
		g.addEdge(7,8);
		g.addEdge(8,6);
		g.addEdge(8,10);
		g.addEdge(8,11);
		g.addEdge(9,8);
		g.addEdge(9,10);
		g.addEdge(11,9);
		
		System.out.println(g.getNumberOfVertexes());	// 11
		System.out.println(g.getNumberOfEdges());		// 17
		System.out.println(g.getVertexSet());	// 1, 2, ..., 11
		System.out.println(g);
			// 1 --> 2 weight = 1.0
			// 1 --> 3 weight = 1.0
			// 1 --> 4 weight = 1.0
			// 2 --> 1 weight = 1.0
			// ...

		System.out.println(g.getDegree(3));				// 5
		System.out.println(g.getNeighborSet(3));			// 1, 2, 4, 5, 6
		System.out.println(g.getDegree(10));				// 2
		System.out.println(g.getNeighborSet(10));		// 8, 9
		
		System.out.println("");
		System.out.println(g.containsEdge(1,2));	// true
		System.out.println(g.containsEdge(2,1));	// true
		System.out.println(g.getWeight(1,2));	// 1.0	
		g.addEdge(1, 2, 5.0);
		System.out.println(g.getWeight(1,2));	// 5.0
		System.out.println(g.getWeight(2,1));	// 5.0
	}

}
