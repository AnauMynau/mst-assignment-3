package edu.assignment3.mst.model;

import java.util.ArrayList;
import java.util.List;

public class ResultDTO {

    public List<PerGraph> results = new ArrayList<>();

    public static class PerGraph {
        public int id; // всегда заполнен
        public String name; // может быть null
        public int vertices;
        public int edges;
        public AlgoResult prim;
        public AlgoResult kruskal;
        public boolean equalCost;
        public boolean connected;
    }


    public static class AlgoResult {
        public long mstCost; // Long.MAX_VALUE если несвязный
        public List<EdgeTriple> mstEdges = new ArrayList<>();
        public long timeMs; // миллисекунды
        public Ops ops = new Ops(); // счётчики операций
        public long opCount() { // агрегированная метрика для CSV
            return ops.pqPushes + ops.pqPops + ops.edgeConsidered + ops.edgeChecks + ops.unions + ops.finds;
        }
    }


    public static class EdgeTriple { public int u; public int v; public int w; }


    public static class Ops {
        public long pqPushes; // Prim
        public long pqPops; // Prim
        public long edgeConsidered; // Prim
        public long edgeChecks; // Kruskal
        public long unions; // Kruskal
        public long finds;
    }
}
