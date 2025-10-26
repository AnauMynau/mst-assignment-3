package edu.assignment3.mst.algo;

import edu.assignment3.mst.model.Edge;
import edu.assignment3.mst.model.Graph;
import edu.assignment3.mst.model.ResultDTO;
import edu.assignment3.mst.util.UnionFind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kruskal {
    public static ResultDTO.AlgoResult run(Graph g){
        long t0=System.nanoTime();
        ResultDTO.AlgoResult out=new ResultDTO.AlgoResult();
        if(g.V==0){ out.mstCost=0; out.timeMs=0; return out; }
        List<Edge> sorted=new ArrayList<>(g.edges); Collections.sort(sorted);
        UnionFind uf=new UnionFind(g.V);
        int taken=0; long total=0;
        for(Edge e: sorted){
            out.ops.edgeChecks++;
            if(uf.union(e.u,e.v)){
                taken++; total+=e.w; ResultDTO.EdgeTriple t=new ResultDTO.EdgeTriple(); t.u=e.u; t.v=e.v; t.w=e.w; out.mstEdges.add(t);
                if(taken==g.V-1) break;
            }
        }
        out.ops.finds=uf.finds; out.ops.unions=uf.unions;
        out.mstCost=(taken==g.V-1)? total : Long.MAX_VALUE;
        out.timeMs=(System.nanoTime()-t0)/1_000_000;
        return out;
    }
}