package edu.assignment3.mst.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class InputData {
    public List<GraphItem> graphs;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GraphItem {
        public int id;

        // понимаем разные названия имени графа
        @JsonAlias({"label", "title", "graph"})
        public String name;

        public int vertices;
        public List<EdgeItem> edges;
    }

    public static class EdgeItem {
        public int u; public int v; public int w;
    }
}
