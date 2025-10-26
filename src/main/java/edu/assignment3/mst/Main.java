package edu.assignment3.mst;

import edu.assignment3.mst.algo.*;
import edu.assignment3.mst.io.JsonIO;
import edu.assignment3.mst.model.*;


import java.nio.file.Path;


public class Main {
    public static void main(String[] args) throws Exception {
        String input = args.length>0? args[0] : "data/ass3_input_medium.json";
        String output = args.length>1? args[1] : "output/results_medium.json";


        var items = JsonIO.readAnyGraphs(Path.of(input));
        ResultDTO all = new ResultDTO();
        for (var it : items){
            Graph g = it.graph;
            ResultDTO.PerGraph pg = new ResultDTO.PerGraph();
            pg.id = it.id; pg.name = it.name; pg.vertices = g.V; pg.edges = g.E();
            pg.prim = Prim.run(g);
            pg.kruskal = Kruskal.run(g);
            pg.equalCost = pg.prim.mstCost == pg.kruskal.mstCost;
            pg.connected = pg.prim.mstCost != Long.MAX_VALUE && pg.kruskal.mstCost != Long.MAX_VALUE;
            all.results.add(pg);
        }
        JsonIO.writeResults(Path.of(output), all);
        System.out.println("Saved results → "+output);
    }
}
