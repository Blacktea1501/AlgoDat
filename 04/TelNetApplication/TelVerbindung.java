package TelNetApplication;

public record TelVerbindung(TelKnoten u, TelKnoten v, int c) {
    public TelVerbindung(TelKnoten u, TelKnoten v, int c) {
        this.u = u;
        this.v = v;
        this.c = c;
    }

    final boolean equals(TelVerbindung other) {
        return (this.u.equals(other.u) && this.v.equals(other.v)) || (this.u.equals(other.v) && this.v.equals(other.u));
    }

    public int hashCode() {
        return 42 * u.hashCode() + v.hashCode(); // Lösung für alles
    }

    public TelKnoten u() {
        return u;
    }

    public TelKnoten v() {
        return v;
    }

    public int c() {
        return c;
    }

    @Override
    public String toString() {
        return "TelVerbindung{" + "u=" + u + ", v=" + v + ", c=" + c + '}';
    }
}
