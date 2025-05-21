package shortestPath;

/**
 * @author Oliver Bittel
 * @since 30.06.2024
 * @param <V> Knotentyp.
 */
public interface Heuristic<V> {
	/**
	 * Schätzt die Kosten (Distanz) von u nach v ab.
	 * @param u
	 * @param v
	 * @return Geschätzte Kosten.
	 */
	double estimatedCost(V u, V v);
}
