package graph.topo;

import java.util.*;

/**
 * Topological Sort using DFS.
 * Works only for Directed Acyclic Graphs (DAGs).
 */
public class TopoSort {

    private Map<Integer, List<Integer>> graph;
    private boolean[] visited;
    private Stack<Integer> stack;

    public List<Integer> run(Map<Integer, List<Integer>> graph) {
        this.graph = graph;
        int n = graph.size();
        visited = new boolean[n];
        stack = new Stack<>();

        for (int node : graph.keySet()) {
            if (!visited[node])
                dfs(node);
        }

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private void dfs(int node) {
        visited[node] = true;
        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited[neighbor])
                dfs(neighbor);
        }
        stack.push(node);
    }
}