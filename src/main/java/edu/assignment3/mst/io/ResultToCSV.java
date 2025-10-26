package edu.assignment3.mst.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.assignment3.mst.model.ResultDTO;


import java.io.*;
import java.nio.file.*;
import java.util.*;


public class ResultToCSV {
    public static void main(String[] args) throws Exception {
        String in = args.length>0? args[0] : "output/results_medium.json";
        String out = args.length>1? args[1] : "output/results_summary.csv";
        ObjectMapper M = new ObjectMapper();
        ResultDTO dto = M.readValue(Files.newInputStream(Path.of(in)), ResultDTO.class);
        Files.createDirectories(Path.of(out).getParent());
        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Path.of(out)))){
            pw.println("id,algorithm,vertices,execution_time,operation_count,total_cost");
            for(ResultDTO.PerGraph pg : dto.results){
                pw.printf("%d,Prim,%d,%d,%d,%d%n", pg.id, pg.vertices, pg.prim.timeMs, pg.prim.opCount(), pg.prim.mstCost);
                pw.printf("%d,Kruskal,%d,%d,%d,%d%n", pg.id, pg.vertices, pg.kruskal.timeMs, pg.kruskal.opCount(), pg.kruskal.mstCost);
            }
        }
        System.out.println("Saved CSV → "+out);
    }
}