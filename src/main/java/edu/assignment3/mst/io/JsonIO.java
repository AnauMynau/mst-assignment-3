package edu.assignment3.mst.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.assignment3.mst.model.*;


import java.io.*;
import java.nio.file.*;
import java.util.*;


public class JsonIO {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);


    public static InputData readInput(Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path)) {
            return MAPPER.readValue(in, InputData.class);
        }
    }


    public static void writeResults(Path path, ResultDTO dto) throws IOException {
        Files.createDirectories(path.getParent());
        try (OutputStream out = Files.newOutputStream(path)) {
            MAPPER.writeValue(out, dto);
        }
    }


    public static Graph toGraph(InputData.GraphItem gi) {
        List<Edge> edges = new ArrayList<>();
        for (InputData.EdgeItem ei : gi.edges) {
            edges.add(new Edge(ei.u, ei.v, ei.w));
        }
        return new Graph(gi.vertices, edges);
    }
}