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
            throw new IllegalStateException("Not computed yet. Call computeOptTelNet() first.");
        } else {
            StdDraw.setCanvasSize(Math.max(512, xMax + 50), Math.max(512, yMax + 50));
            StdDraw.setXscale(0, xMax);
            StdDraw.setYscale(0, yMax);
            StdDraw.clear(StdDraw.LIGHT_GRAY);
            StdDraw.setPenRadius(0.005);

            StdDraw.setPenColor(StdDraw.RED);
            for (TelVerbindung v : this.verbindungListe) {
                TelKnoten k1 = v.v(); // Annahme: TelVerbindung.getKnoten1()
                TelKnoten k2 = v.u(); // Annahme: TelVerbindung.getKnoten2()
                // Annahme: TelKnoten hat getX() und getY()
                StdDraw.line(k1.x(), k1.y(), k2.x(), k2.y());
            }
        }
        // Zeichne immer die Knoten
        StdDraw.setPenColor(StdDraw.BLUE);
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
        TelNet telNet = new TelNet(7);

        telNet.addTelKnoten(1, 1);
        telNet.addTelKnoten(3, 1);
        telNet.addTelKnoten(4, 2);
        telNet.addTelKnoten(3, 4);
        telNet.addTelKnoten(2, 6);
        telNet.addTelKnoten(4, 7);
        telNet.addTelKnoten(7, 6);

        telNet.computeOptTelNet();

        System.out.println(telNet);

        telNet.drawOptTelNet(7, 7);

        TelNet randomstuff = new TelNet(100);
        randomstuff.generateRandomTelNet(1000,1000,1000);
        randomstuff.computeOptTelNet();

        System.out.println(randomstuff);

        randomstuff.drawOptTelNet(1000, 1000);
    }

}
