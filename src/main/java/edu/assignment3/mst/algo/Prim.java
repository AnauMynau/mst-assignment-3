package edu.assignment3.mst.algo;

import edu.assignment3.mst.model.Edge;
import edu.assignment3.mst.model.Graph;
import edu.assignment3.mst.model.ResultDTO;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Prim {
    public static ResultDTO.Algo run(Graph g) {
        long t0 = System.nanoTime();
        ResultDTO.Algo out = new ResultDTO.Algo();
        if (g.V == 0) { out.mstCost = 0; out.timeMs = 0; return out; }

        boolean[] in = new boolean[g.V];
        // (w, from, to)
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        // стартуем с вершины 0
        in[0] = true;
        for (Edge e : g.adj.get(0)) {
            pq.add(new int[]{e.w, e.u, e.v});
            out.ops.pqPushes++; out.ops.edgeConsidered++;
        }

        int taken = 0;
        long total = 0;

        while (!pq.isEmpty() && taken < g.V - 1) {
            int[] cur = pq.poll(); out.ops.pqPops++;
            int w = cur[0], u = cur[1], v = cur[2];

            // берем только ребро, ведущее ВНЕ текущего остова
            if (in[v]) continue;

            in[v] = true;
            taken++;
            total += w;

            ResultDTO.EdgeTriple et = new ResultDTO.EdgeTriple();
            et.u = u; et.v = v; et.w = w;
            out.mstEdges.add(et);

            // обновляем фронтир: из новой вершины v кидаем рёбра на «снаружи»
            for (Edge e : g.adj.get(v)) {
                if (!in[e.v]) {
                    pq.add(new int[]{e.w, e.u, e.v});
                    out.ops.pqPushes++; out.ops.edgeConsidered++;
                }
            }
        }

        out.mstCost = (taken == g.V - 1) ? total : Long.MAX_VALUE;
        out.timeMs = (System.nanoTime() - t0) / 1_000_000;
        return out;
    }
}
