package TelNetApplication;

public record TelKnoten(int x, int y) {

    public TelKnoten (int x, int y){
        this.x = x;
        this.y = y;
    }

    boolean equals(TelKnoten other) {
        return this.x == other.x && this.y == other.y;
    }

    public int hashCode() {
        return 42 * Integer.hashCode(x) + Integer.hashCode(y); // Lösung für alles
    }

    public int x() {
        return x;
    }

   public int y() {
       return y;
   }

    @Override
   public String toString() {
       return "TelKnoten{" + "x=" + x + ", y=" + y + '}';
   }

}

