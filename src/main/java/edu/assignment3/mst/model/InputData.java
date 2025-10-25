package edu.assignment3.mst.model;

import java.util.List;

public class InputData {
    public List<GraphItem> graphs;


    public static class GraphItem {
        public String name;
        public int vertices;
        public List<EdgeItem> edges;
    }


    public static class EdgeItem {
        public int u; public int v; public int w;
    }
}