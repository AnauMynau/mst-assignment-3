package assignment3.mst.algo;

import assignment3.mst.model.Edge;
import assignment3.mst.model.Graph;
import assignment3.mst.model.ResultDTO;

import java.util.*;

public class Prim {
    public static ResultDTO.AlgoResult run(Graph g ) {
        long t0 = System.nanoTime();
        ResultDTO.AlgoResult out = new ResultDTO.AlgoResult();
        if (g.V == 0) { out.mstCost = 0; out.timeMs = 0; return out; }

        boolean[] vis = new boolean[g.V];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        //int[] = {weight, from, to
        vis[0] = true;
        for (Edge e: g.adj.get(0)) {
            pq.add(new int[]{e.w, e.u, e.v});
            out.ops.edgeConsidered++;
        }

        int taken = 0;
        long total = 0;

        while (!pq.isEmpty() && taken < g.V -1) {
            int[] cur = pq.poll();
            out.ops.pqPops++;
            int w = cur[0], from = cur[1], to = cur[2];
            if (vis[to]) {
                continue;
            }
            vis[to] = true;
            taken++;
            total += w;

            out.mstEdges.add(new ResultDTO.EdgeTriple(from, to, w));
            for (Edge e :  g.adj.get(to)) {
                if (!vis[e.v]) {
                    pq.add(new int[]{e.w, e.u, e.v});
                    out.ops.pqPushes++;
                    out.ops.edgeConsidered++;
                }
            }
        }
        if (taken != g.V - 1) {
        // Graph was disconnected — no full MST
            out.mstCost = Long.MAX_VALUE;
        } else {
            out.mstCost = total;
        }
        long t1 = System.nanoTime();
        out.timeMs = (t1 - t0) / 1_000_000;
        return out;
    }
}
