package edu.assignment3.mst.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.assignment3.mst.model.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonIO {
    private static final ObjectMapper M = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    /* ---------- JSON in/out ---------- */

    public static InputData readInput(Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path)) {
            return M.readValue(in, InputData.class);
        }
    }

    public static void writeResults(Path path, ResultDTO dto) throws IOException {
        Files.createDirectories(path.getParent());
        try (OutputStream out = Files.newOutputStream(path)) {
            M.writeValue(out, dto);
        }
    }

    // опционально — если будешь генерировать входы
    public static void writeInput(Path path, InputData in) throws IOException {
        Files.createDirectories(path.getParent());
        try (OutputStream out = Files.newOutputStream(path)) {
            M.writeValue(out, in);
        }
    }

    /* ---------- Преобразование GraphItem -> Graph ---------- */

    public static Graph toGraph(InputData.GraphItem gi) {
        List<Edge> edges = new ArrayList<>();
        for (InputData.EdgeItem e : gi.edges) {
            edges.add(new Edge(e.u, e.v, e.w));
        }
        return new Graph(gi.vertices, edges);
    }

    /* ---------- Универсальный ридер для визуализации/прогона ---------- */

    // Читает любой корректный InputData и отдаёт удобные элементы {id, name, graph}
    public static List<Item> readAnyGraphs(Path path) throws IOException {
        InputData data = readInput(path);
        List<Item> out = new ArrayList<>();
        int id = 1;
        for (InputData.GraphItem gi : data.graphs) {
            Graph g = toGraph(gi);
            String name = (gi.name != null && !gi.name.isBlank()) ? gi.name : ("graph-" + id);
            out.add(new Item(id++, name, g));
        }
        return out;
    }

    // Удобная «DTOшка» для чтения графов
    public static class Item {
        public final int id;
        public final String name;
        public final Graph graph;

        public Item(int id, String name, Graph graph) {
            this.id = id;
            this.name = name;
            this.graph = graph;
        }
    }
}
