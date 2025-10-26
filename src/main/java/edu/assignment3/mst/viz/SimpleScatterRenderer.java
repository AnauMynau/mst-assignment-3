package edu.assignment3.mst.viz;

import edu.assignment3.mst.model.Edge;
import edu.assignment3.mst.model.Graph;
import edu.assignment3.mst.model.ResultDTO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

/**
 * Простая «рассыпушка» для маленьких графов:
 * - вершины зелёные, подписи V0..Vn
 * - ВСЕ рёбра серым
 * - рёбра MST — красным пунктиром поверх
 * - веса рёбер — синим возле середины.
 *
 * Делай это только для small (например, V <= 30), иначе получится каша.
 */
public final class SimpleScatterRenderer {

    private SimpleScatterRenderer(){}

    public static void drawAllVsMST(Graph g,
                                    List<ResultDTO.EdgeTriple> mstEdges,
                                    String caption,
                                    Path outPng,
                                    long seed) throws IOException {
        final int W = 1000, H = 700;
        final int PAD = 60;

        Files.createDirectories(outPng.getParent());
        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // фон + заголовок
        g2.setColor(new Color(0xFAFAFA)); g2.fillRect(0,0,W,H);
        g2.setColor(Color.BLACK); g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString(caption, 20, 28);

        // позиции вершин (стабильные для одного seed)
        Map<Integer, Point> pos = randomLayout(g.V, W, H, PAD, seed);

        // Множество MST-ребёр (по ключу (min(u,v),max(u,v)))
        Set<Long> mst = new HashSet<>();
        for (ResultDTO.EdgeTriple e : mstEdges) mst.add(key(e.u, e.v));

        // 1) все рёбра серым
        Stroke thin = new BasicStroke(1.5f);
        g2.setStroke(thin);
        g2.setColor(new Color(0x555555));
        for (Edge e : g.edges) {
            Point a = pos.get(e.u), b = pos.get(e.v);
            g2.drawLine(a.x, a.y, b.x, b.y);
        }

        // 2) веса синим около середины
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.setColor(new Color(0x1E56C5));
        for (Edge e : g.edges) {
            Point a = pos.get(e.u), b = pos.get(e.v);
            int mx = (a.x + b.x)/2, my = (a.y + b.y)/2;
            String s = String.valueOf(e.w);
            int tw = g2.getFontMetrics().stringWidth(s);
            g2.drawString(s, mx - tw/2, my - 3);
        }

        // 3) MST красным пунктиром потолще поверх
        float[] dash = {9f, 6f};
        Stroke dashSt = new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, dash, 0f);
        g2.setStroke(dashSt);
        g2.setColor(new Color(0xD13C3C));
        for (Edge e : g.edges) {
            if (!mst.contains(key(e.u, e.v))) continue;
            Point a = pos.get(e.u), b = pos.get(e.v);
            g2.drawLine(a.x, a.y, b.x, b.y);
        }

        // 4) вершины — зелёные кружки + подписи V#
        for (int v = 0; v < g.V; v++) {
            Point p = pos.get(v);
            int R = 10;
            Shape c = new Ellipse2D.Double(p.x - R, p.y - R, 2*R, 2*R);
            g2.setColor(new Color(0x21C441)); // fill
            g2.fill(c);
            g2.setColor(Color.BLACK); // stroke
            g2.setStroke(new BasicStroke(1.8f));
            g2.draw(c);

            String label = "V" + v;
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            int tw = g2.getFontMetrics().stringWidth(label);
            g2.setColor(Color.BLACK);
            g2.drawString(label, p.x - tw/2, p.y - 12);
        }

        // легенда
        int y = H - 20;
        // серая линия
        g2.setColor(new Color(0x555555)); g2.setStroke(new BasicStroke(2f));
        g2.drawLine(24, y, 60, y); g2.setColor(Color.BLACK); g2.drawString("All edges", 66, y+4);
        // красная пунктирная линия
        g2.setColor(new Color(0xD13C3C)); g2.setStroke(dashSt);
        g2.drawLine(150, y, 186, y); g2.setColor(Color.BLACK); g2.drawString("MST", 192, y+4);

        g2.dispose();
        ImageIO.write(img, "PNG", outPng.toFile());
    }

    /* ---------- утилиты ---------- */

    /** Простая случайная раскладка + чуть-чуть репульсии, чтобы точки не слепались. */
    private static Map<Integer, Point> randomLayout(int n, int W, int H, int pad, long seed) {
        Random rnd = new Random(seed);
        Map<Integer, Point> pos = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int x = pad + rnd.nextInt(Math.max(1, W - 2*pad));
            int y = pad + 30 + rnd.nextInt(Math.max(1, H - 2*pad - 30));
            pos.put(i, new Point(x, y));
        }
        // мини-пружинки (несколько итераций отталкивания)
        for (int it = 0; it < 60; it++) {
            for (int i = 0; i < n; i++) {
                Point a = pos.get(i);
                double dx = 0, dy = 0;
                for (int j = 0; j < n; j++) if (i != j) {
                    Point b = pos.get(j);
                    double vx = a.x - b.x, vy = a.y - b.y;
                    double d2 = vx*vx + vy*vy + 1e-3;
                    double f = 1200.0 / d2; // сила расползания
                    dx += vx * f; dy += vy * f;
                }
                a.x = clamp((int)Math.round(a.x + dx*0.003), pad, W - pad);
                a.y = clamp((int)Math.round(a.y + dy*0.003), pad + 30, H - pad);
            }
        }
        return pos;
    }

    private static int clamp(int x, int lo, int hi) { return Math.max(lo, Math.min(hi, x)); }
    private static long key(int u, int v) { int a = Math.min(u, v), b = Math.max(u, v); return (((long)a) << 32) | (b & 0xffffffffL); }

    public static void main(String[] args) throws Exception {
        // путь к small JSON
        Path in = Path.of("data/ass3_input_small.json");
        Path out = Path.of("output/test_viz");

        edu.assignment3.mst.model.InputData data =
                edu.assignment3.mst.io.JsonIO.readInput(in);

        for (edu.assignment3.mst.model.InputData.GraphItem gi : data.graphs) {
            edu.assignment3.mst.model.Graph g =
                    edu.assignment3.mst.io.JsonIO.toGraph(gi);

            edu.assignment3.mst.model.ResultDTO.AlgoResult kruskal =
                    edu.assignment3.mst.algo.Kruskal.run(g);

            Path outFile = out.resolve(gi.name + "_simple.png");
            long seed = 42 + gi.id;
            SimpleScatterRenderer.drawAllVsMST(
                    g, kruskal.mstEdges,
                    "Simple MST — " + gi.name,
                    outFile,
                    seed
            );
        }

        System.out.println("✅ Saved simple visualizations to " + out);
    }

    public static void drawBoth(Graph g, List<ResultDTO.EdgeTriple> mstEdges, List<ResultDTO.EdgeTriple> mstEdges1, String s, Path out) {
    }

    public static void drawOnlyMST(Graph g, List<ResultDTO.EdgeTriple> mstEdges, String s, Path out) {
    }
}
