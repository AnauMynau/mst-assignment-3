package assignment3.mst.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    public final int V;
    public final List<Edge> edges;
    public final List<List<Edge>> adj;

    public Graph(int V, List<Edge> edges) {
        this.V = V;
        this.edges = List.copyOf(edges);
        this.adj = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        for (Edge e : edges) {
            if (e.u < 0 || e.u >= V || e.v < 0 || e.v >= V) {
                throw new IllegalArgumentException("Edge endpoint out of range: " + e);
            }
            adj.get(e.u).add(e);
            adj.get(e.v).add(new Edge(e.u, e.v, e.w)); //undirected mirror
        }
    }
    public int E() { return edges.size();}
}

