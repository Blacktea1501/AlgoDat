// O. Bittel;
// 14.12.2023

package undirectedGraph;

import java.util.Set;

/**
 * Graph mit ungerichteten Kanten.
 * Mit g.addEdge(v,w, weight) wird eine gerichtete Kante von v nach w mit dem Gewicht weight einfügt.
 * Falls kein Gewicht angegeben wird, dann ist das Gewichte implizit auf 1 gesetzt.
 * Der Graph einthält keine Mehrfachkanten.
 * @author Oliver Bittel
 * @since 22.03.2018
 * @param <V> Knotentyp.
 */
public interface UndirectedGraph<V> {
	/**
     * Fügt neuen Knoten zum Graph dazu.
     * @param v Knoten
     * @return true, falls Knoten noch nicht vorhanden war.
     */
    boolean addVertex(V v);

    /**
     * Fügt neue Kante (mit Gewicht 1) zum Graph dazu.
	 * Falls einer der beiden Knoten noch nicht im Graphen vorhanden ist,
	 * dann wird er dazugefügt.
	 * Falls die Kante schon vorhanden ist, dann wird das Gewicht 
	 * mit 1 überschrieben. 
     * @param v Startknoten
     * @param w Zielknoten
     * @return true, falls Kante noch nicht vorhanden war.
     */
    boolean addEdge(V v, V w);

    /**
     * Fügt neue Kante mit Gewicht weight zum Graph dazu.
	 * Falls einer der beiden Knoten noch nicht im Graphen vorhanden ist,
	 * dann wird er dazugefügt.
	 * Falls die Kante schon vorhanden ist, dann wird das Gewicht 
	 * mit weight überschrieben.
     * @param v Startknoten
     * @param w Zielknoten
     * @param weight Gewicht
     * @return true, falls Kante noch nicht vorhanden war.
     */
    boolean addEdge(V v, V w, double weight);

    /**
     * Prüft ob Knoten v im Graph vorhanden ist.
     * @param v Knoten
     * @return true, falls Knoten vorhanden ist.
     */
    boolean containsVertex(V v);

    /**
     * Prüft ob Kante im Graph vorhanden ist.
     * @param v Startknoten
     * @param w Endknoten
     * @return true, falls Kante vorhanden ist.
     */
    boolean containsEdge(V v, V w);
    
    /**
     * Liefert Gewicht der Kante zurück.
     * @param v Startknoten
     * @param w Endknoten
	 * @throws IllegalArgumentException falls die Kante nicht existiert.
     * @return Gewicht der Kante.
     */
    double getWeight(V v, V w);

    /**
     * Liefert Anzahl der Knoten im Graph zurück.
     * @return Knotenzahl.
     */
    int getNumberOfVertexes();

    /**
     * Liefert Anzahl der Kanten im Graph zurück.
     * @return Kantenzahl.
     */
    int getNumberOfEdges();

    /**
     * Liefert eine nicht modifizierbare Sicht (unmodifiable view) 
	 * auf die Menge aller Knoten im Graph zurück.
	 * 
     * @return Knotenmenge
     */
    Set<V> getVertexSet();
    

    /**
     * Liefert Grad des Knotens v zurück.
     * Das ist die Anzahl der Nachbarknoten v.
     * @param v Knoten
     * @throws IllegalArgumentException falls Knoten v
     * nicht im Graph vorhanden ist.
     * @return Knoteneingangsgrad
     */
    int getDegree(V v);

    /**
     * Liefert eine nicht modifizierbare Sicht (unmodifiable view) auf
	 * die Menge aller Nachbarknoten von v zurück.
     * @param v Knoten
     * @throws IllegalArgumentException falls Knoten v
     * nicht im Graph vorhanden ist.
     * @return Knotenmenge
     */
    Set<V> getNeighborSet(V v);
}
