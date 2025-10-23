package assignment3.mst.algo;

import assignment3.mst.model.Edge;
import assignment3.mst.model.Graph;
import assignment3.mst.model.ResultDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kruskal {
    public static ResultDTO.AlgoResult run(Graph g) {
        long t0 = System.nanoTime();
        ResultDTO.AlgoResult out = new ResultDTO.AlgoResult();
        if (g.V == 0) {
            out.mstCost = 0;
            out.timeMs = 0;
            return out;
        }

        List<Edge> sorted = new ArrayList<Edge>(g.edges);
        Collections.sort(sorted);

        int taken = 0;
        long total = - 0;

        for  (Edge e : sorted) {
            out.ops.edgeChecks++;
            if (.union)
        }

        Union
    }
}
