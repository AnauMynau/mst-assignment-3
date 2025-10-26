package edu.assignment3.mst.algo;


import edu.assignment3.mst.model.*;
import java.util.*;


public class Prim {
    public static ResultDTO.Algo run(Graph g){
        long t0=System.nanoTime();
        ResultDTO.Algo out=new ResultDTO.Algo();
        if(g.V==0){ out.mstCost=0; out.timeMs=0; return out; }


        boolean[] vis=new boolean[g.V];
        PriorityQueue<int[]> pq=new PriorityQueue<>(Comparator.comparingInt(a->a[0]));
        vis[0]=true;
        for(Edge e: g.adj.get(0)) { pq.add(new int[]{e.w,e.u,e.v}); out.ops.pqPushes++; out.ops.edgeConsidered++; }
        int taken=0; long total=0;
        while(!pq.isEmpty() && taken<g.V-1){
            int[] cur=pq.poll(); out.ops.pqPops++;
            int w=cur[0], from=cur[1], to=cur[2];
            if(vis[to]) continue;
            vis[to]=true; taken++; total+=w;
            ResultDTO.EdgeTriple t=new ResultDTO.EdgeTriple(); t.u=from; t.v=to; t.w=w; out.mstEdges.add(t);
            for(Edge e: g.adj.get(to)) if(!vis[e.v]){ pq.add(new int[]{e.w,e.u,e.v}); out.ops.pqPushes++; out.ops.edgeConsidered++; }
        }
        out.mstCost = (taken==g.V-1)? total : Long.MAX_VALUE;
        out.timeMs=(System.nanoTime()-t0)/1_000_000;
        return out;
    }
}