package TelNetApplication;

import java.util.*;

public class TelNet {

    private int lbg; // Leitungs-Begrenz-Wert
    private List<TelKnoten> knotenListe;
    private List<TelVerbindung> verbindungListe;

    /** * Konstruktor für TelNet.
     * @param lbg Leitungs-Begrenz-Wert
     */
    public TelNet(int lbg) {
        this.lbg = lbg;
        this.knotenListe = new java.util.ArrayList<>();
        this.verbindungListe = null; // wird von computeOptTelNet() gesetzt
    }

    boolean addTelKnoten(int x, int y) {
        TelKnoten knoten = new TelKnoten(x, y);
        // Überprüfen, ob der Knoten bereits existiert
        for (TelKnoten existingKnoten : knotenListe) {
            if (existingKnoten.equals(knoten)) {
                return false; // Knoten existiert bereits
            }
        }

        knotenListe.add(knoten);
        this.verbindungListe = null; // Verbindungen müssen neu berechnet werden
        return true; // Knoten erfolgreich hinzugefügt
    }

    boolean computeOptTelNet(){
        if (knotenListe.size() <= 1) {
            this.verbindungListe = new ArrayList<>();
            return true; // Keine Knoten, also keine Verbindungen
        }

        Set<TelKnoten> knotenSet = new HashSet<>(knotenListe);
        UnionFind<TelKnoten> wald = new UnionFind<>(knotenSet);


        PriorityQueue<TelVerbindung> kantenSchlange = new PriorityQueue<>(Comparator.comparingInt(TelVerbindung::c));
        for (int i = 0; i < knotenListe.size(); i++){
            for (int j = i + 1; j < knotenListe.size(); j++){
                TelKnoten k1 = knotenListe.get(i);
                TelKnoten k2 = knotenListe.get(j);
                kantenSchlange.add(new TelVerbindung(k1, k2, cost(k1, k2)));
            }
        }
        List <TelVerbindung> mst = new ArrayList<>();

        // Kruskal Algorithmus
        while (wald.size() != 1 && !kantenSchlange.isEmpty()){
            // normal delMin()
           TelVerbindung currentKante = kantenSchlange.poll();
           TelKnoten k1 = currentKante.u();
           TelKnoten k2 = currentKante.v();

           TelKnoten t1 = wald.find(k1);
           TelKnoten t2 = wald.find(k2);

           if (!t1.equals(t2)) { // wenn nicht im selben Set (bildet keinen Kreis/Zyklus)
               wald.union(t1, t2);
               mst.add(currentKante);
           }
        }

        if(wald.size() != 1) {
            // es existiert kein minimaler Spannbaum, da nicht alle Knoten verbunden sind
            return false;
        }

        verbindungListe = mst;
        return true;
    }

    // Using ../StdDraw.java for drawing
    void drawOptTelNet(int xMax, int yMax){
        if (this.verbindungListe == null) {
            System.out.println("Optimales Netz (MST) wurde noch nicht erfolgreich berechnet oder ist nicht vorhanden.");
            System.out.println("Bitte zuerst computeOptTelNet() aufrufen und sicherstellen, dass es 'true' zurückgibt.");
            // Zeichne nur die Knoten, wenn kein MST vorhanden ist
            StdDraw.setCanvasSize(Math.max(512, xMax + 50), Math.max(512, yMax + 50));
            StdDraw.setXscale(0, xMax);
            StdDraw.setYscale(0, yMax);
            StdDraw.clear(StdDraw.LIGHT_GRAY);
        } else {
            StdDraw.setCanvasSize(Math.max(512, xMax + 50), Math.max(512, yMax + 50));
            StdDraw.setXscale(0, xMax);
            StdDraw.setYscale(0, yMax);
            StdDraw.clear(StdDraw.LIGHT_GRAY);
            StdDraw.setPenRadius(0.005);

            StdDraw.setPenColor(StdDraw.BLUE);
            for (TelVerbindung v : this.verbindungListe) {
                TelKnoten k1 = v.v(); // Annahme: TelVerbindung.getKnoten1()
                TelKnoten k2 = v.u(); // Annahme: TelVerbindung.getKnoten2()
                // Annahme: TelKnoten hat getX() und getY()
                StdDraw.line(k1.x(), k1.y(), k2.x(), k2.y());
            }
        }
        // Zeichne immer die Knoten
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.015);
        for (TelKnoten k : knotenListe) {
            StdDraw.point(k.x(), k.y());
        }
        StdDraw.show();

    }

    void generateRandomTelNet(int n, int xMax, int yMax){
        this.knotenListe.clear();
        java.util.Random rand = new java.util.Random();
        for(int i = 0; i < n; i++) {
            int x = rand.nextInt(xMax + 1);
            int y = rand.nextInt(yMax + 1);
            addTelKnoten(x, y);
        }
        this.verbindungListe = null; // Verbindungen müssen neu berechnet werden
    }

    List<TelVerbindung> getOptTelNet(){
        if (this.verbindungListe == null) {
            computeOptTelNet(); // Berechne die optimalen Verbindungen, falls noch nicht geschehen
        }
        return verbindungListe;
    }

    int cost(TelKnoten u, TelKnoten v){
        int distanz = Math.abs(u.x() - v.x()) + Math.abs(u.y() - v.y());
        return distanz > this.lbg ? Integer.MAX_VALUE : distanz; // Kosten sind unendlich, wenn die Verbindung nicht erlaubt ist
    }

    int getOptTelNetKosten(){
        if (this.verbindungListe == null) {
            throw new IllegalStateException("Not computed yet. Call computeOptTelNet() first.");
        }

        int kosten = 0;

        for (TelVerbindung verbindung : verbindungListe) {
            kosten += verbindung.c();
        }

        return kosten;
    }

    int size(){
        return knotenListe.size();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Telefonnetz Status:\n");
        sb.append("  Leitungs-Begrenz-Wert (LBG): ").append(lbg).append("\n");
        sb.append("  Anzahl Knoten: ").append(knotenListe.size()).append("\n");
        sb.append("  Knoten:\n");
        if (knotenListe.isEmpty()) {
            sb.append("    Keine Knoten im Netz.\n");
        } else {
            for (TelKnoten knoten : knotenListe) {
                sb.append("    ").append(knoten.toString()).append("\n"); // Annahme: TelKnoten hat toString()
            }
        }

        if (this.verbindungListe == null) {
            sb.append("  Optimales Netz (MST): Wurde noch nicht erfolgreich berechnet oder ist nicht vorhanden.\n");
            sb.append("  (computeOptTelNet() aufrufen und prüfen, ob es 'true' zurückgibt)\n");
        } else {
            sb.append("  Optimales Netz (MST) - ").append(this.verbindungListe.size()).append(" Verbindungen:\n");
            if (this.verbindungListe.isEmpty() && !knotenListe.isEmpty()) {
                sb.append("    Keine Verbindungen im optimalen Netz (möglicherweise LBG zu klein oder nur 1 Knoten).\n");
            } else if (this.verbindungListe.isEmpty() && knotenListe.isEmpty()){
                sb.append("    Keine Verbindungen, da keine Knoten vorhanden.\n");
            } else {
                for (TelVerbindung verbindung : this.verbindungListe) {
                    sb.append("    ").append(verbindung.toString()).append("\n"); // Annahme: TelVerbindung hat toString()
                }
            }
            sb.append("  Gesamtkosten des optimalen Netzes: ").append(getOptTelNetKosten()).append("\n");
        }
        return sb.toString();

    }

    public static void main(String[] args) {
        TelNet meinNetz = new TelNet(150); // LBG = 150

        meinNetz.addTelKnoten(10, 10);
        meinNetz.addTelKnoten(100, 10);
        meinNetz.addTelKnoten(10, 100);
        meinNetz.addTelKnoten(100, 100);
        meinNetz.addTelKnoten(50, 50);
        // meinNetz.addTelKnoten(300, 300); // Ein isolierter Knoten, wenn LBG=150 zu klein ist

        System.out.println("--- Initiales Netz ---");
        System.out.println(meinNetz);

        System.out.println("\n--- Berechne optimales Netz ---");
        boolean erfolg = meinNetz.computeOptTelNet();
        System.out.println("Berechnung erfolgreich: " + erfolg);
        System.out.println(meinNetz);
        meinNetz.drawOptTelNet(200, 200);

        System.out.println("\n--- Test mit LBG, das Trennung erzwingt ---");
        TelNet getrenntesNetz = new TelNet(50); // Sehr kleiner LBG
        getrenntesNetz.addTelKnoten(0,0);
        getrenntesNetz.addTelKnoten(10,0); // Abstand 10 <= 50
        getrenntesNetz.addTelKnoten(100,100); // Abstand zu (0,0) ist ~141
        getrenntesNetz.addTelKnoten(100,110); // Abstand 10 <= 50

        erfolg = getrenntesNetz.computeOptTelNet();
        System.out.println("Berechnung (getrenntesNetz) erfolgreich: " + erfolg); // Sollte false sein, wenn >1 Komponente
        System.out.println(getrenntesNetz); // verbindungListe sollte null sein oder nur Teilbaum für (0,0)-(10,0) etc.
        // Gemäß der Logik: verbindungListe bleibt null, da forest.size() != 1
        getrenntesNetz.drawOptTelNet(120, 120);


        System.out.println("\n--- Test mit einem Knoten ---");
        TelNet einKnotenNetz = new TelNet(100);
        einKnotenNetz.addTelKnoten(5,5);
        erfolg = einKnotenNetz.computeOptTelNet();
        System.out.println("Berechnung (ein Knoten) erfolgreich: " + erfolg); // Sollte true sein
        System.out.println(einKnotenNetz);
        einKnotenNetz.drawOptTelNet(20,20);

        System.out.println("\n--- Test mit leerem Netz ---");
        TelNet leeresNetz = new TelNet(100);
        erfolg = leeresNetz.computeOptTelNet();
        System.out.println("Berechnung (leeres Netz) erfolgreich: " + erfolg); // Sollte true sein
        System.out.println(leeresNetz);
        leeresNetz.drawOptTelNet(20,20);

    }

}