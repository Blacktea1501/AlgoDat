// O. Bittel;
// 2.8.2023

package directedGraph;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;


/**
 * Klasse zur Analyse von Web-Sites.
 *
 * @author Oliver Bittel
 * @since 30.10.2023
 */
public class AnalyzeWebSite {
    public static void main(String[] args) throws IOException {
        // Graph aus Website erstellen und ausgeben:
        //DirectedGraph<String> webSiteGraph = buildGraphFromWebSite("02/aufgabe2/data/WebSiteKlein");
        DirectedGraph<String> webSiteGraph = buildGraphFromWebSite("02/aufgabe2/data/WebSiteGross");
        System.out.println("Anzahl Seiten: \t" + webSiteGraph.getNumberOfVertexes());
        System.out.println("Anzahl Links: \t" + webSiteGraph.getNumberOfEdges());
        //System.out.println(webSiteGraph);

        // Starke Zusammenhangskomponenten berechnen und ausgeben
        StrongComponents<String> sc = new StrongComponents<>(webSiteGraph);
        System.out.println(sc.numberOfComp());
        //System.out.println(sc);

        // Page Rank ermitteln und Top-100 ausgeben
        pageRank(webSiteGraph);
    }

    /**
     * Liest aus dem Verzeichnis dirName alle Web-Seiten und
     * baut aus den Links einen gerichteten Graphen.
     *
     * @param dirName Name eines Verzeichnis
     * @return gerichteter Graph mit Namen der Web-Seiten als Knoten und Links als gerichtete Kanten.
     */
    private static DirectedGraph buildGraphFromWebSite(String dirName) throws IOException {
        File webSite = new File(dirName);
        DirectedGraph<String> webSiteGraph = new AdjacencyListDirectedGraph();

        for (File f : webSite.listFiles()) {
            String from = f.getName();
            LineNumberReader in = new LineNumberReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("href")) {
                    String[] s_arr = line.split("\"");
                    String to = s_arr[1];
                    webSiteGraph.addEdge(from, to);
                }
            }
        }
        return webSiteGraph;
    }

    /**
     * pageRank ermittelt Gewichte (Ranks) von Web-Seiten
     * aufgrund ihrer Link-Struktur und gibt sie aus.
     *
     * @param g gerichteter Graph mit Web-Seiten als Knoten und Links als Kanten.
     */
    private static <V> void pageRank(DirectedGraph<V> g) {
        int nI = 10;
        double alpha = 0.5;
        int N = g.getNumberOfVertexes();

        if (N == 0) {
            System.out.println("Graph ist leer.");
            return;
        }

        // Definiere und initialisiere rankTable:
        Map<V, Double> rankTable = new HashMap<>();
        Set<V> vertexSet = g.getVertexSet();

        // Initialisierung: rank(P)  = 1/N fuer alle Seiten P.
        for (V vertex : vertexSet) {
            rankTable.put(vertex, 1.0 / N);
        }


        // Iteration:
        for (int i = 0; i < nI; i++) {
            Map<V, Double> newRankTable = new HashMap<>();
            for (V p : vertexSet) {
                double sumIncomingRanks = 0.0;
                for (V q : g.getPredecessorVertexSet(p)) { // q sind Seiten, die auf p linken
                    int outDegreeQ = g.getOutDegree(q);
                    if (outDegreeQ > 0) {
                        sumIncomingRanks += rankTable.get(q) / outDegreeQ;
                    }
                }
                double newRank = (1 - alpha) / N + alpha * sumIncomingRanks;
                newRankTable.put(p, newRank);
            }
            rankTable = newRankTable; // Ranks fur die nächste Iteration aktualisieren
        }

        boolean isSmallWebsite = N < 100;

        if (isSmallWebsite) {
            // Rank Table ausgeben (nur für data/WebSiteKlein):
            System.out.println("\nPage Ranks (für kleine Webseite):");
            List<Map.Entry<V, Double>> sortedRankList = new ArrayList<>(rankTable.entrySet());
            if (!sortedRankList.isEmpty() && sortedRankList.get(0).getKey() instanceof Comparable) {
                try {
                    // Korrigierte Zeile: (Comparable<V>) wurde zu (Comparable) geändert
                    sortedRankList.sort(Comparator.comparing(entry -> (Comparable) entry.getKey()));
                } catch (ClassCastException e) {
                    System.out.println("(Ausgabe nicht nach Seitennamen sortiert, da Typ nicht vergleichbar)");
                }
            }
            for (Map.Entry<V, Double> entry : sortedRankList) {
                System.out.printf("%s: %.8f%n", entry.getKey(), entry.getValue());
            }
        } else {
            // Nach Ranks sortieren Top 100 ausgeben (nur für data/WebSiteGross):
            System.out.println("\nPage Ranks (für große Webseite):");
            List<Map.Entry<V, Double>> sortedRankList = new ArrayList<>(rankTable.entrySet());
            sortedRankList.sort(Map.Entry.<V, Double>comparingByValue().reversed());

            System.out.println("\nTop 100 gerankte Seiten:");
            for (int i = 0; i < Math.min(100, sortedRankList.size()); i++) {
                Map.Entry<V, Double> entry = sortedRankList.get(i);
                System.out.printf("%d. %s: %.8f%n", i + 1, entry.getKey(), entry.getValue());
            }

            // Top-Seite mit ihren Vorgängern und Ranks ausgeben (nur für data/WebSiteGross):
            if (!sortedRankList.isEmpty()) {
                Map.Entry<V, Double> topEntry = sortedRankList.get(0);
                V topPage = topEntry.getKey();
                double topRank = topEntry.getValue();
                System.out.printf("\nTop-Seite (%s) mit Rank %.8f und ihren Vorgängern:%n", topPage, topRank);

                Set<V> predecessors = g.getPredecessorVertexSet(topPage);
                if (predecessors.isEmpty()) {
                    System.out.println("Die Top-Seite hat keine direkten Vorgänger im Graphen.");
                } else {
                    List<Map.Entry<V, Double>> predecessorRanks = new ArrayList<>();
                    for (V pred : predecessors) {
                        predecessorRanks.add(Map.entry(pred, rankTable.getOrDefault(pred, 0.0)));
                    }
                    predecessorRanks.sort(Map.Entry.<V, Double>comparingByValue().reversed());
                    for (Map.Entry<V, Double> predEntry : predecessorRanks) {
                        System.out.printf("- %s (Rank: %.8f)%n", predEntry.getKey(), predEntry.getValue());
                    }
                }
            }
        }
    }
}
