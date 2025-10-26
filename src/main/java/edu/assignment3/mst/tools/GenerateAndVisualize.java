package edu.assignment3.mst.tools;

import edu.assignment3.mst.algo.Kruskal;
import edu.assignment3.mst.algo.Prim;
import edu.assignment3.mst.io.JsonIO;
import edu.assignment3.mst.model.*;
import edu.assignment3.mst.viz.SimpleScatterRenderer;

import java.nio.file.Path;
import java.util.*;

/**
 * Генерирует 4 набора графов (small/medium/large/extralarge),
 * сохраняет в data/ass3_input_*.json и СРАЗУ визуализирует MST в PNG (output/viz).
 *
 * Запуск по умолчанию:
 *   java -cp target/classes edu.assignment3.mst.tools.GenerateAndVisualize
 *
 * Параметры (необязательны):
 *   java -cp target/classes edu.assignment3.mst.tools.GenerateAndVisualize <seed> <edgeFactor>
 * где:
 *   seed       — сид генератора (long), по умолчанию 42
 *   edgeFactor — плотность (≈ edgeFactor * V рёбер), по умолчанию 2.3
 */
public class GenerateAndVisualize {

    private static Random RNG = new Random(42);
    private static double EDGE_FACTOR = 2.3; // среднее число рёбер на вершину (плюс дерево)

    public static void main(String[] args) throws Exception {
        if (args.length >= 1) RNG = new Random(Long.parseLong(args[0]));
        if (args.length >= 2) EDGE_FACTOR = Double.parseDouble(args[1]);

        // 1) генерируем и записываем 4 файла
        generatePack(5,   5,    30,  "data/ass3_input_small.json");
        generatePack(10,  30,   300, "data/ass3_input_medium.json");
        generatePack(10,  300,  1000,"data/ass3_input_large.json");
        generatePack(3,   1000, 2000,"data/ass3_input_extralarge.json");

        // 2) визуализируем для small/medium полностью, для больших — только MST
        visualizeFrom("data/ass3_input_small.json",       "output/viz");
        visualizeFrom("data/ass3_input_medium.json",      "output/viz");
        visualizeFrom("data/ass3_input_large.json",       "output/viz");
        visualizeFrom("data/ass3_input_extralarge.json",  "output/viz");

        System.out.println("✅ Done. JSON in /data, PNG in /output/viz");
    }

    /* ========== ГЕНЕРАЦИЯ НАБОРОВ ГРАФОВ ========== */

    private static void generatePack(int count, int vMin, int vMax, String outFile) throws Exception {
        InputData data = new InputData();
        data.graphs = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            int n = randInt(vMin, vMax);

            // цель по числу рёбер: дерево (n-1) + добавочные рёбра ~ EDGE_FACTOR * n
            int targetEdges = Math.min(n * (n - 1) / 2, (int) Math.round(EDGE_FACTOR * n));

            InputData.GraphItem gi = makeConnectedGraph(i, "g-" + i, n, targetEdges, 1, 99);
            data.graphs.add(gi);
        }

        JsonIO.writeInput(Path.of(outFile), data);
        System.out.println("  → " + outFile + " (graphs=" + count + ")");
    }

    /** Делает связный граф: случайное остовное дерево + добавочные рёбра до targetEdges. */
    private static InputData.GraphItem makeConnectedGraph(int id, String name, int n, int targetEdges,
                                                          int wMin, int wMax) {
        InputData.GraphItem gi = new InputData.GraphItem();
        gi.id = id;
        gi.name = name;
        gi.vertices = n;
        gi.edges = new ArrayList<>();

        // остов: случайное дерево
        int[] order = perm(n);
        Set<Long> used = new HashSet<>();
        for (int i = 1; i < n; i++) {
            int u = order[i];
            int v = order[randInt(0, i - 1)];
            addEdgeUnique(gi, used, u, v, randInt(wMin, wMax));
        }

        // добавочные рёбра (без дубликатов, без петель)
        int maxEdges = n * (n - 1) / 2;
        int need = Math.max(0, Math.min(targetEdges, maxEdges) - (n - 1));
        while (need > 0) {
            int u = randInt(0, n - 1);
            int v = randInt(0, n - 1);
            if (u == v) continue;
            if (addEdgeUnique(gi, used, u, v, randInt(wMin, wMax))) need--;
        }

        return gi;
    }

    private static boolean addEdgeUnique(InputData.GraphItem gi, Set<Long> used, int u, int v, int w) {
        int a = Math.min(u, v), b = Math.max(u, v);
        long key = (((long) a) << 32) | (b & 0xffffffffL);
        if (used.contains(key)) return false;
        used.add(key);

        InputData.EdgeItem e = new InputData.EdgeItem();
        e.u = a; e.v = b; e.w = w;
        gi.edges.add(e);
        return true;
    }

    private static int[] perm(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        for (int i = n - 1; i > 0; i--) {
            int j = RNG.nextInt(i + 1);
            int t = a[i]; a[i] = a[j]; a[j] = t;
        }
        return a;
    }

    private static int randInt(int lo, int hi) { return lo + RNG.nextInt(hi - lo + 1); }

    /* ========== ВИЗУАЛИЗАЦИЯ ========== */

    private static void visualizeFrom(String inputJson, String outDir) throws Exception {
        InputData data = JsonIO.readInput(Path.of(inputJson));
        for (InputData.GraphItem gi : data.graphs) {
            Graph g = JsonIO.toGraph(gi);
            ResultDTO.AlgoResult prim    = Prim.run(g);
            ResultDTO.AlgoResult kruskal = Kruskal.run(g);

            // small/medium — рисуем оба MST поверх всех рёбер
            // large/extralarge — только MST (чтобы не получалась каша)
            Path out = Path.of(outDir, gi.name + "_mst.png");
            if (g.V <= 120) {
                SimpleScatterRenderer.drawBoth(
                        g, prim.mstEdges, kruskal.mstEdges,
                        "MST — Prim vs Kruskal — " + gi.name,
                        out
                );
            } else {
                SimpleScatterRenderer.drawOnlyMST(
                        g, kruskal.mstEdges,
                        "MST (Kruskal) — " + gi.name,
                        out
                );
            }
        }
    }
}
