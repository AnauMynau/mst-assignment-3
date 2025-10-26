package edu.assignment3.mst.model;

import java.util.*;

public class Graph {
    public final int V;
    public final List<Edge> edges;
    public final List<List<Edge>> adj;

    public Graph(int V, List<Edge> inputEdges) {
        this.V = V;

        Map<Long, Integer> minWeightByPair = new HashMap<>();

        for (Edge e : inputEdges) {
            if (e.u < 0 || e.u >= V || e.v < 0 || e.v >= V) {
                throw new IllegalArgumentException("Edge endpoint out of range: " + e);
            }
            int a = Math.min(e.u, e.v);
            int b = Math.max(e.u, e.v);
            long key = ((long) a << 32) | (b & 0xffffffffL);
            minWeightByPair.merge(key, e.w, Math::min);
        }

        List<Edge> norm = new ArrayList<>(minWeightByPair.size());
        for (Map.Entry<Long, Integer> en : minWeightByPair.entrySet()) {
            int a = (int) (en.getKey() >> 32);
            int b = (int) (long) (en.getKey() & 0xffffffffL);
            norm.add(new Edge(a, b, en.getValue()));
        }
        this.edges = List.copyOf(norm);

        this.adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
        for (Edge e : this.edges) {
            adj.get(e.u).add(new Edge(e.u, e.v, e.w));
            adj.get(e.v).add(new Edge(e.v, e.u, e.w));
        }
    }

    public int E() { return edges.size(); }
}
