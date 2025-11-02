 Assignment 4 — Graph Algorithms (Tarjan SCC & Topological Sort)
 Objective

The goal of this assignment was to analyze directed graphs and process city-service tasks with dependencies by implementing two algorithms:
		Tarjan’s Strongly Connected Components (SCC) algorithm
		Topological Sorting for Directed Acyclic Graphs (DAGs)
The main aim was to detect cyclic dependencies, compress them into SCCs, and then perform optimal scheduling using topological ordering.

Description

In this task, a city’s maintenance scheduling system is represented as a directed graph:
	•	Vertices — tasks or locations
	•	Edges — dependencies between tasks
	•	Weights — not required 
 
  Implemented algorithms
	1.	Tarjan’s SCC
		Detects all strongly connected components using DFS and low-link values.
		Complexity: O(V + E).
		Used to find cyclic dependencies between tasks.
	2.	Topological Sort
		Orders tasks linearly so that all dependencies are respected.
		Implemented via DFS.
		Complexity: O(V + E).
    Both algorithms were tested on multiple datasets of varying sizes.

Summary of Algorithm Results
graph_id,num_nodes,num_edges,tarjan_time_ms,toposort_time_ms
1,6,6,0,342333,0,024875
2,6,7,0,012625,0,010459
3,7,8,0,009709,0,008208
4,10,14,0,015208,0,011125
5,12,18,0,067542,0,019042
6,15,20,0,022167,0,018750
7,22,34,0,033416,0,024708
8,24,36,0,033291,0,026292
9,26,40,0,030500,0,024666
Analysis and Comparison

Theory
	  Tarjan’s SCC uses DFS and recursion; identifies cyclic subgraphs efficiently.
		TopoSort requires the graph to be acyclic and gives the correct task execution order.

Observed Performance
	1.	Both algorithms ran successfully on all graphs.
	2.	Tarjan’s algorithm is slightly slower because it tracks DFS indices and low-links.
	3.	Topological Sort runs faster and scales linearly with graph size.
	4.	For graphs with cycles, TopoSort was skipped or applied after SCC compression.

Testing

JUnit 5 tests verify:
	1.	Tarjan correctly finds SCCs for known graphs.
	2.	Topological order is valid (i.e. no violations of dependencies).
	3.	Algorithms return results in reasonable time for large graphs.
	4.	Output files (output.json, results.csv) are generated correctly.

 Files in the Repository
	1.	src/main/java/graph/scc/TarjanSCC.java — Tarjan’s implementation
	2.	src/main/java/graph/topo/TopoSort.java — Topological Sort
	3.	src/main/java/aiu/se/Main.java — Main program and I/O logic
	4.	src/main/resources/tasks.json — input data
	5.	src/main/resources/output.json — JSON results
	6.	src/main/resources/results.csv — CSV performance summary
	7.	src/test/java/aiu/se/GraphAlgorithmsTest.java — JUnit tests
	8.	pom.xml — Maven configuration

Conclusion

Both Tarjan and Topological Sort algorithms were implemented successfully and analyzed on graphs of different sizes.
	•	Tarjan effectively detects cycles and connected components.
	•	Topological Sort efficiently orders acyclic tasks.
	•	Their combined use provides a complete solution for city service scheduling and dependency analysis.

All assignment requirements were met successfully. 
 References	1.	Cormen, Leiserson, Rivest, and Stein — Introduction to Algorithms (CLRS) 2.	GeeksforGeeks — Tarjan’s Algorithm for SCC and Topological Sorting	3.	Oracle Java Docs — Collections, Stack, and PriorityQueue APIs











