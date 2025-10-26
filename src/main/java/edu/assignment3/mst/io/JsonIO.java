package edu.assignment3.mst.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import edu.assignment3.mst.model.*;


import java.io.*;
import java.nio.file.*;
import java.util.*;


public class JsonIO {
    private static final ObjectMapper M = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);


    // Читать входной файл любого типа и вернуть список готовых Graph + метаданные (id/name)
    public static List<Item> readAnyGraphs(Path path) throws IOException {
        JsonNode root;
        try(InputStream in = Files.newInputStream(path)) { root = M.readTree(in); }
        if(root==null || !root.has("graphs")) throw new IllegalArgumentException("No 'graphs' array in input");
        ArrayNode arr = (ArrayNode) root.get("graphs");
        List<Item> out = new ArrayList<>();
        int autoId = 1;
        for(JsonNode g : arr){
// detect type: nodes[from/to/weight] OR vertices/edges(u,v,w)
            if(g.has("nodes") && g.has("edges")){
// nodes: ["V0","V1",...], edges: {from:"V0", to:"V1", weight:int}
                List<String> nodes = M.convertValue(g.get("nodes"), new TypeReference<List<String>>(){});
                Map<String,Integer> idx = new HashMap<>();
                for(int i=0;i<nodes.size();i++) idx.put(nodes.get(i), i);
                List<Edge> edges = new ArrayList<>();
                for(JsonNode e : g.get("edges")){
                    String from = e.get("from").asText();
                    String to = e.get("to").asText();
                    int w = e.get("weight").asInt();
                    Integer u = idx.get(from), v = idx.get(to);
                    if(u==null||v==null) throw new IllegalArgumentException("Unknown node in edge: "+from+"-"+to);
                    edges.add(new Edge(u,v,w));
                }
                Graph graph = new Graph(nodes.size(), edges);
                Item item = new Item();
                item.id = g.has("id")? g.get("id").asInt() : autoId++;
                item.name = g.has("label")? g.get("label").asText() : null;
                item.graph = graph;
                out.add(item);
            } else if(g.has("vertices") && g.has("edges")){
                int V = g.get("vertices").asInt();
                List<Edge> edges = new ArrayList<>();
                for(JsonNode e : g.get("edges")){
                    edges.add(new Edge(e.get("u").asInt(), e.get("v").asInt(), e.get("w").asInt()));
                }
                Graph graph = new Graph(V, edges);
                Item item = new Item();
                item.id = g.has("id")? g.get("id").asInt() : autoId++;
                item.name = g.has("name")? g.get("name").asText() : null;
                item.graph = graph;
                out.add(item);
            } else {
                throw new IllegalArgumentException("Unsupported graph item format");
            }
        }
        return out;
    }


    public static void writeResults(Path path, ResultDTO dto) throws IOException {
        Files.createDirectories(path.getParent());
        try(OutputStream out = Files.newOutputStream(path)) { M.writeValue(out, dto); }
    }


    public static class Item { public int id; public String name; public Graph graph; }
}