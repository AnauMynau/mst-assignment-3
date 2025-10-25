package edu.assignment3.mst;

import edu.assignment3.mst.algo.Kruskal;
import edu.assignment3.mst.algo.Prim;
import edu.assignment3.mst.io.JsonIO;
import edu.assignment3.mst.model.Edge;
import edu.assignment3.mst.model.Graph;
import edu.assignment3.mst.model.InputData;
import edu.assignment3.mst.model.ResultDTO;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTests {
    @Test
    void smallGraph_correctness() throws Exception {
        InputData data = JsonIO.readInput(Path.of("data/input_small.json"));
        InputData.GraphItem gi = data.graphs.get(0);
        Graph g = JsonIO.toGraph(gi);


        ResultDTO.AlgoResult p = Prim.run(g);
        ResultDTO.AlgoResult k = Kruskal.run(g);


        assertEquals(p.mstCost, k.mstCost, "Prim vs Kruskal cost");


        if (p.mstCost != Long.MAX_VALUE) {
            assertEquals(g.V - 1, p.mstEdges.size(), "Prim edges count");
            assertEquals(g.V - 1, k.mstEdges.size(), "Kruskal edges count");
        }
    }


    @Test
    void disconnectedGraph_detected() {
        Graph g = new Graph(4, List.of(
                new Edge(0,1,1),
                new Edge(2,3,1)
        ));
        ResultDTO.AlgoResult p = Prim.run(g);
        ResultDTO.AlgoResult k = Kruskal.run(g);
        assertEquals(Long.MAX_VALUE, p.mstCost);
        assertEquals(Long.MAX_VALUE, k.mstCost);
    }
}