package assignment3.mst.model;

import java.util.Objects;

public class Edge implements Comparable<Edge> {
    public final int u;
    public final int v;
    public final int w;

    public Edge(int u, int v,  int w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.w, other.w);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Edge)) return false;
        Edge e  = (Edge) other;

        //some connection regardles of direction
        return ((u == e.u && v == e.v) || (u == e.v && v == e.u)) &&  w == e.w;
    }

    @Override
    public int hashCode() {
        int a = Math.min(u, v);
        int b = Math.max(u, v);
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return "(" + u + ", " + v + ", " + w + ")";
    }
}
