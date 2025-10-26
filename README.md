# Assignment 3: Minimum Spanning Tree (Prim & Kruskal Algorithms)

###  Course
Design and Analysis of Algorithms  
Astana IT University — Fall 2025  

###  Author
**Inayatulla**  
Group: SE-2423  

---

## 📘 Project Overview

This project implements and compares two classical algorithms for finding the **Minimum Spanning Tree (MST)** in a weighted, undirected graph:  
**Prim’s algorithm** and **Kruskal’s algorithm**.

Both algorithms were implemented in **Java (JDK 17)** using clean, modular code and JSON data input/output.  
The project also includes performance measurements (execution time, operation count, MST cost) and automated tests.

---

## Project Structure

mst-assignment-3/
│
├─ data 
│ ├─ ass3_input_small.json
│ ├─ ass3_input_medium.json
│ ├─ ass3_input_large.json
│ └─ ass3_input_extralarge.json
│
├─ output/
│ ├─ results_small.json
│ ├─ results_medium.json
│ ├─ results_large.json
│ ├─ results_extralarge.json
│ ├─ results_summary.csv
│ ├─ execution_time_vs_vertices.png
│ └─ operation_count_vs_vertices.png
│
├─ src/
│ ├─ main/java/edu/assignment3/mst/
│ │ ├─ algo/Prim.java
│ │ ├─ algo/Kruskal.java
│ │ ├─ io/JsonIO.java
│ │ ├─ io/ResultToCSV.java
│ │ ├─ model/Graph.java
│ │ ├─ model/Edge.java
│ │ ├─ model/ResultDTO.java
│ │ ├─ util/UnionFind.java
│ │ └─ Main.java
│ └─ test/java/edu/assignment3/mst/MSTTests.java
│
└─ pom.xml


---

## 🚀 How to Run

### Build and Test
```bash
mvn clean test


Run Program

Run the main class with different dataset sizes:

java -cp target/classes edu.assignment3.mst.Main data/ass3_input_small.json      output/results_small.json
java -cp target/classes edu.assignment3.mst.Main data/ass3_input_medium.json     output/results_medium.json
java -cp target/classes edu.assignment3.mst.Main data/ass3_input_large.json      output/results_large.json
java -cp target/classes edu.assignment3.mst.Main data/ass3_input_extralarge.json output/results_extralarge.json
```

Generate Summary CSV
```
java -cp target/classes edu.assignment3.mst.io.ResultToCSV output/results_medium.json output/results_summary.csv
```


Input & Output Format
🔹 Input (Example: ass3_input_small.json)
```
{
  "graphs": [
    {
      "id": 1,
      "vertices": 5,
      "edges": [
        {"u": 0, "v": 1, "w": 2},
        {"u": 0, "v": 3, "w": 6},
        {"u": 1, "v": 2, "w": 3},
        {"u": 1, "v": 4, "w": 5},
        {"u": 2, "v": 4, "w": 7}
      ]
    }
  ]
}
```
🔹 Output (Example: results_small.json)
```
{
  "results": [
    {
      "graph": "small-1",
      "vertices": 5,
      "edges": 7,
      "prim": {
        "mstCost": 16,
        "timeMs": 0,
        "ops": {"pqPushes": 10, "pqPops": 6, "edgeConsidered": 10}
      },
      "kruskal": {
        "mstCost": 16,
        "timeMs": 0,
        "ops": {"edgeChecks": 7, "unions": 4, "finds": 8}
      },
      "equalCost": true,
      "connected": true
    }
  ]
}
```
🔹 Summary CSV (Example: results_summary.csv)
```
id,algorithm,vertices,execution_time,operation_count,total_cost
6,Prim,229,13,765,10925
6,Kruskal,229,3,2367,6629
...

```
Analysis and Discussion
1️⃣ Execution Time vs Number of Vertices

The first chart compares how the execution time of Prim’s and Kruskal’s algorithms changes as the number of vertices increases.

Both algorithms show a gradual growth in execution time with larger graphs.
However, Kruskal’s algorithm tends to perform slightly faster on sparse graphs,
because it mainly depends on sorting all edges and applying the Union–Find structure efficiently.

Meanwhile, Prim’s algorithm performs better on dense graphs,
where the priority queue helps to efficiently select the smallest edge among many candidates.

📊 Conclusion:

Execution time grows approximately linearly with the number of vertices.
Kruskal is faster for sparse graphs, Prim is faster for dense graphs.

2️⃣ Operation Count vs Number of Vertices

The second chart shows the number of internal operations (push/pop for Prim, union/find for Kruskal) versus the number of vertices.

Kruskal performs noticeably more operations overall, since it sorts all edges and checks connectivity for every pair during the Union–Find steps.
Prim, on the other hand, performs fewer operations because it expands the MST incrementally using a priority queue.

📊 Conclusion:

Kruskal performs more operations but often runs faster due to its simple structure and edge-sorting efficiency.
Prim has fewer operations on average but depends on heap performance.

3️⃣ General Summary

| Algorithm             | ️ Best For Graph Type     | ️ Time Complexity                                                               |  Space Complexity |  Key Characteristics |
|:------------------------|:--------------------------|:--------------------------------------------------------------------------------|:--------------------|:------------------------|
| **Prim’s Algorithm**    | Dense graphs (many edges) | **O(E log V)**                                                                  | **O(V + E)** | Builds MST incrementally using a **priority queue** (min-heap). Works best when graph is stored as adjacency list or matrix. |
| **Kruskal’s Algorithm** | Sparse graphs (few edges) | **O(E log E)**                                                                  | **O(E)** | Sorts edges by weight and uses **Union–Find (Disjoint Set)** to avoid cycles. Ideal for edge list representation. |
| **Result Accuracy**     | —                         |  Both produce the same MST total cost                                           | — | Confirms algorithm correctness on all test graphs. |
| **Performance Trend**   | —                         |  *Prim* → faster on dense graphs  <br>  *Kruskal* → faster on sparse graphs | — | Differences depend on graph density and data structure choice. |


📈 Both algorithms produce identical MST cost values, confirming correctness

4️⃣ Visual Results
Execution Time vs Number of Vertices

Operation Count vs Number of Vertices

Conclusion

This project successfully implemented and compared Prim’s and Kruskal’s algorithms for finding a Minimum Spanning Tree (MST).
The results confirm theoretical expectations:

Both algorithms yield the same MST cost on connected graphs.

Kruskal is generally faster on sparse graphs, while Prim is better for dense graphs.

The number of operations increases with the number of vertices, but execution time remains low even for large inputs (up to 2000 vertices).

The project demonstrates how algorithmic complexity and data structures affect performance in practical graph problems.

💻 Technologies Used

Language: Java 17

Build Tool: Apache Maven

Testing: JUnit 5

Libraries: Jackson Databind (for JSON I/O)

Visualization: Microsoft Excel / matplotlib

IDE: IntelliJ IDEA

