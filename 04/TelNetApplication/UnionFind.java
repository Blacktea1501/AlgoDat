// O. Bittel
// 20.02.2025

package TelNetApplication;

import java.util.Set;

/**
 * Klasse für Union-Find-Strukturen.
 * Unterstützt die effiziente Verwaltung einer Partionierung einer Menge
 * (disjunkte Zerlegung in Teilmengen).
 *
 * Union-Find-Struktur mit Pfadkompression und Union-by-Rank.
 *
 * Im Durchschnitt haben unionByRank und findWithCompression praktisch eine Laufzeit von O(1).
 *
 * @author Oliver Bittel
 * @since 24.01.2025
 */
public class UnionFind<T> {
	// ...

	/**
     * Legt eine neue Union-Find-Struktur mit allen 1-elementigen Teilmengen von s an.
	 * @param s Menge von Elementen, für die eine Partionierung verwaltet werden soll.
	 *
     */
	public UnionFind(Set<T> s) {
		// ...
	}

	/**
     * Liefert den Repräsentanten der Teilmenge zurück, zu der e gehört.
	 * Pfadkompression wird angewendet.
     * @param e Element
     * @throws IllegalArgumentException falls e nicht in der Partionierung vorkommt.
     * @return Repräsentant der Teilmenge, zu der e gehört.
     */
	public T find(T e) {
		return null;
		// ...
	}


	/**
     * Vereinigt die beiden Teilmengen, die e1 bzw. e2 enthalten.
	 * Die Vereinigung wird nur durchgeführt,
	 * falls die beiden Mengen unterschiedlich sind.
	 * Es wird union-by-rank durchgeführt.
     * @param e1 Element.
	 * @param e2 Element.
	 * @throws IllegalArgumentException falls e1 und e2 keine Elemente der Union-Find-Struktur sind.
     */
	public void union(T e1, T e2) {
		return;
		// ...
	}

	/**
	 * Ausgabe der Union-Find-Struktur zu Testzwecken.
	 */
	public void print() {
		return;
		// ...
	}

	/**
	 * Liefert die Anzahl der Teilmengen in der Partitionierung zurück.
	 * @return Anzahl der Teilmengen.
	 */
	public int size() {
		// not implemented
		return 0; // Placeholder, implement as needed
	}

    public static void main(String[] args) {
		Set<Integer> s = Set.of(1,2,3,4,5,6,7,8,9,10);
		UnionFind<Integer> uf = new UnionFind<>(s);
		uf.union(1,2);
		uf.print();
		uf.union(3,4);
		uf.print();
		uf.union(2,4);
		uf.print();
		uf.find(4);
		uf.print();
		uf.union(9,10);
		uf.union(7,8);
		uf.print();
		uf.union(7,9);
		uf.print();
		uf.union(1,7);
		uf.print();
		uf.find(10);
		uf.print();
	}
}
