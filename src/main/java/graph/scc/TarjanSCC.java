package graph.scc;

import java.util.*;

/**
 * Tarjan's Algorithm for finding Strongly Connected Components (SCC)
 * Uses one DFS traversal and a stack to detect components.
 */
public class TarjanSCC {

    private Map<Integer, List<Integer>> graph;   // adjacency list
    private boolean[] onStack;                   // tracks which nodes are on the stack
    private Stack<Integer> stack;                // active nodes
    private int[] ids;                           // unique id for each node
    private int[] low;                           // lowest id reachable from this node
    private int id;                              // id counter
    private List<List<Integer>> sccs;            // list of strongly connected components

    /**
     * Main entry point — runs Tarjan's algorithm.
     * @param graph adjacency list representation
     * @return list of strongly connected components
     */
    public List<List<Integer>> run(Map<Integer, List<Integer>> graph) {
        this.graph = graph;
        int n = graph.size();

        onStack = new boolean[n];
        stack = new Stack<>();
        ids = new int[n];
        low = new int[n];
        sccs = new ArrayList<>();
        Arrays.fill(ids, -1); // mark all as unvisited
        id = 0;

        for (int i = 0; i < n; i++) {
            if (ids[i] == -1) dfs(i);
        }

        return sccs;
    }

    /**
     * Recursive DFS that tracks low-link values.
     */
    private void dfs(int at) {
        stack.push(at);
        onStack[at] = true;
        ids[at] = low[at] = id++;

        for (int to : graph.getOrDefault(at, Collections.emptyList())) {
            if (ids[to] == -1) dfs(to);
            if (onStack[to]) low[at] = Math.min(low[at], low[to]);
        }

        // Found root node — start a new SCC
        if (ids[at] == low[at]) {
            List<Integer> component = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                onStack[node] = false;
                component.add(node);
                low[node] = ids[at];
                if (node == at) break;
            }
            sccs.add(component);
        }
    }
}