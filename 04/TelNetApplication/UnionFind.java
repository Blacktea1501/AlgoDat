// O. Bittel
// 20.02.2025

package TelNetApplication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
	private Map<T,T> parent; // Repräsentant der Teilmenge
	private Map<T, Integer> rank;
	private int numSets;


	/**
     * Legt eine neue Union-Find-Struktur mit allen 1-elementigen Teilmengen von s an.
	 * @param s Menge von Elementen, für die eine Partionierung verwaltet werden soll.
	 *
     */
	public UnionFind(Set<T> s) {
		parent = new HashMap<>();
		rank = new HashMap<>();
		for (T element : s) {
			parent.put(element, element); // Jedes Element ist zunächst sein eigener Repräsentant
			rank.put(element, 0); // Anfangsrang ist 0
		}
		numSets = s.size(); // Anzahl der Teilmengen ist die Größe der Menge s
	}

	/**
     * Liefert den Repräsentanten der Teilmenge zurück, zu der e gehört.
	 * Pfadkompression wird angewendet.
     * @param e Element
     * @throws IllegalArgumentException falls e nicht in der Partitionierung vorkommt.
     * @return Repräsentant der Teilmenge, zu der e gehört.
     */
	public T find(T e) {
		if (!parent.containsKey(e)){
			throw new IllegalArgumentException("Element " + e + " ist nicht Teil der Partitionierung.");
		}

		// Pfadkompression
		if (!parent.get(e).equals(e)) {
			// rekursiver aufruf, um den Repräsentanten zu finden
			parent.put(e, find(parent.get(e)));
		}

		return parent.get(e);
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
		T root1 = find(e1);
		T root2 = find(e2);

		if (!root1.equals(root2)){
			int rank1 = rank.get(root1);
			int rank2 = rank.get(root2);

			if (rank1 < rank2) {
				// root1 hat einen niedrigeren Rang, also wird root1 unter root2 eingereiht
				parent.put(root1, root2);
			} else if (rank1 > rank2) {
				// root2 hat einen niedrigeren Rang, also wird root2 unter root1 eingereiht
				parent.put(root2, root1);
			} else {
				// Beide Ränge sind gleich, wir können einen der beiden Repräsentanten wählen
				parent.put(root2, root1);
				rank.put(root1, rank1 + 1); // Erhöhe den Rang von root1
			}
			numSets--; // size wird verringert, da zwei Teilmengen vereinigt wurden
		}
	}

	/**
	 * Ausgabe der Union-Find-Struktur zu Testzwecken.
	 */
	public void print() {
		System.out.println("Status der Union-Find Struktur:");
		System.out.println("Anzahl der disjunkten Mengen: " + numSets);

		if (parent.isEmpty()) {
			System.out.println("Die Struktur ist leer.");
			return;
		}

		System.out.println("Element -> Parent (Rank):");
		for (T elem : parent.keySet()) {
			T p = parent.get(elem);
			// Um den aktuellen (möglicherweise komprimierten) Parent und korrekten Rang des Repräsentanten anzuzeigen:
			T representative = find(elem); // Ruft find auf, um Pfadkompression sicherzustellen
			int currentRank = rank.get(representative); // Rang des Repräsentanten
			System.out.println("  " + elem + " -> " + p + " (Rep: " + representative + ", Rank des Rep: " + currentRank + ")");
		}

		// Gruppieren der Elemente in ihre jeweiligen Mengen zur besseren Visualisierung
		Map<T, Set<T>> sets = new HashMap<>();
		for (T elem : parent.keySet()) {
			T root = find(elem); // Stellt sicher, dass der Pfad komprimiert ist und wir den wahren Repräsentanten bekommen
			sets.computeIfAbsent(root, k -> new HashSet<>()).add(elem);
		}

		System.out.println("Disjunkte Mengen:");
		int setCount = 0;
		for (Map.Entry<T, Set<T>> entry : sets.entrySet()) {
			setCount++;
			System.out.println("  Menge " + setCount + " (Repräsentant: " + entry.getKey() + "): " + entry.getValue());
		}
		System.out.println("------------------------------------");

	}

	/**
	 * Liefert die Anzahl der Teilmengen in der Partitionierung zurück.
	 * @return Anzahl der Teilmengen.
	 */
	public int size() {
		return numSets;
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
		System.out.println("find(4): " + uf.find(4));
		uf.print();
		uf.union(9,10);
		uf.union(7,8);
		uf.print();
		uf.union(7,9);
		uf.print();
		uf.union(1,7);
		uf.print();
		System.out.println("find(10): " + uf.find(10));
		uf.print();
	}
}
