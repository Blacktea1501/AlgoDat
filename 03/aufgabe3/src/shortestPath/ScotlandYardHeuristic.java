package shortestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Klasse für Scotland Yard Heuristik.
 * Bietet eine Methode estimatedCost an, die
 * die Distanz zweier Knoten im Scotland-Yard-Spielplan schätzt.
 * Die Heuristik wird für A* benötigt.
 *
 * @author Oliver Bittel
 * @since 30.06.2024
 */
public class ScotlandYardHeuristic implements Heuristic<Integer> {
    private Map<Integer, Point> coord; // Ordnet jedem Knoten seine Koordinaten zu

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Der Konstruktor liest die (x,y)-Koordinaten (Pixelkoordinaten) aller Knoten
     * von der Datei ScotlandYard_Knoten.txt in eine Map ein.
     */
    public ScotlandYardHeuristic() throws FileNotFoundException {
        coord = new TreeMap<>(); // Map mit den Koordinaten der Knoten
        // Lese Koordinaten von ScotlandYard_Knoten.txt in eine Map.

        File inputFile = new File("data/ScotlandYard_Knoten.txt");
        if (!inputFile.exists()) {
            inputFile = new File("03/aufgabe3/data/ScotlandYard_Knoten.txt");
        }
        Scanner scanner = null;

        try {
           scanner = new Scanner(inputFile);

           while (scanner.hasNextInt()){
              int stationNumber = scanner.nextInt();
              int xCoord = scanner.nextInt();
              int yCoord = scanner.nextInt();
               coord.put(stationNumber, new Point(xCoord, yCoord));
           }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

    }

    /**
     * Liefert einen skalierten Euklidischen Abstand zwischen Knoten u und v zurück.
     * Da die Koordinaten von x und y in Pixeleinheiten sind, wird
     * der Euklidische Abstand mit einem Faktor zwischen 0.02 bis 0.1 skaliert.
     * @param u Knoten
     * @param v Knoten
     * @return skalierter Euklidischer Abstand als geschätze Kosten
     */
    public double estimatedCost(Integer u, Integer v) {
        Point p1 = coord.get(u);
        Point p2 = coord.get(v);

        if (p1 == null || p2 == null) {
            return 0.0;
        }

        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance * 0.02; // Skalierungsfaktor
    }
}
