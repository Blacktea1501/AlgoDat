package floyd;
import directedGraph.*;

public class FloydTest {
    public static void main(String[] args) {
        example1();     // Kleines Beispiel
        example2();     // Negativer Zyklus
        example3();     // Alle kürzesten Wege im Allgäu
        example4();     // Zentraler Knotenpunkt im Allgäu
    }

    public static void example1() {
        DirectedGraph<Integer> g = GraphExamples.smallGraphExample();
        Floyd<Integer> floyd = new Floyd<>(g);
        floyd.print();

        System.out.println(floyd.getDist(4,1));
        System.out.println(floyd.getShortestPath(4,1));

        System.out.println(floyd.getDist(1,4));
        System.out.println(floyd.getShortestPath(1,4));

        floyd.saveAsCsv("data/example1.csv");
    }

    public static void example2() {
        DirectedGraph<Integer> g = GraphExamples.smallGraphExample();
        g.addEdge(1, 2, -2); // negative cycle
        Floyd<Integer> floyd = new Floyd<>(g);
    }

    public static void example3() {
        DirectedGraph<String> g = GraphExamples.allgaeuGraphExample();
        Floyd<String> floyd = new Floyd<>(g);

        System.out.println(floyd.getDist("Kimratshofen","Pfronten")); // 54 km
        System.out.println(floyd.getShortestPath("Kimratshofen","Pfronten"));

        System.out.println(floyd.getDist("Pfronten","Kimratshofen")); // 54 km
        System.out.println(floyd.getShortestPath("Pfronten","Kimratshofen"));

        System.out.println(floyd.getDist("Börwang","Pfronten")); // 44 km
        System.out.println(floyd.getShortestPath("Börwang","Pfronten"));

        floyd.saveAsCsv("data/allgaeu.csv");
    }

    public static void example4() {
        // Ermittelte die 5 relevantesten Städte mit der Anzahl der kürzesten Wege, die durch sie führen
        DirectedGraph<String> g = GraphExamples.allgaeuGraphExample();
        Floyd<String> floyd = new Floyd<>(g);
        Relevance<String> relevance = new Relevance<>(floyd);
        for (String v : relevance.getAllNodesSortedByRelevance().subList(0, 5))
            System.out.println(v + " " + relevance.getRelevance(v));
        // Ausgabe:
        // Durach 186
        // Kempten 182
        // Oy 166
        // Waltenhofen 152
        // Nesselwang 116
    }
}