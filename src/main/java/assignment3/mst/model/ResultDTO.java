package assignment3.mst.model;

import java.util.ArrayList;
import java.util.List;

public class ResultDTO {

    public static class PerGraph {
        public String graph;
        public int vertices;
        public int edges;
        public AlgoResult prim;
        public AlgoResult kruskal;
        public boolean equalCost;
        public boolean connected;
    }

    public static class AlgoResult {
        public long mstCost;
        public long timeMs;
        public List<EdgeTriple> mstEdges = new ArrayList<>();
        public Ops ops = new Ops();
    }

    public static class EdgeTriple {
        public int u; public  int v; public int w;
        public EdgeTriple() {}
        public EdgeTriple(int u, int v, int w) {
            this.u = u; this.v = v; this.w = w;
        }
    }

    public static class Ops {
        public long pqPushes; //Prim
        public long pqPops;     //Prim
        public long edgeConsidered;     //Prin
        public long sortComparisons;    // optional, not measured precisely
        public long edgeCheck;      // Kruslal - edges inspected
        public long unions;     //Kruskal - succesful unions
        public long finds;      //Kruskal - find operations
    }
}
