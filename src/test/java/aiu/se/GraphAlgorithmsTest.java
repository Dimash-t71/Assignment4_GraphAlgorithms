package aiu.se;

import graph.scc.TarjanSCC;
import graph.topo.TopoSort;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GraphAlgorithmsTest {

    @Test
    public void testTarjanSCC_SimpleGraph() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, List.of(1));
        graph.put(1, List.of(2));
        graph.put(2, List.of(0, 3));
        graph.put(3, List.of());

        TarjanSCC tarjan = new TarjanSCC();
        List<List<Integer>> sccs = tarjan.run(graph);

        // Проверяем, что есть 2 компоненты
        assertEquals(2, sccs.size());
        // Проверяем, что одна компонента содержит 0,1,2
        assertTrue(sccs.stream().anyMatch(c -> c.containsAll(List.of(0,1,2))));
    }

    @Test
    public void testTopoSort_SimpleGraph() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, List.of(1, 2));
        graph.put(1, List.of(3));
        graph.put(2, List.of(3));
        graph.put(3, List.of());

        TopoSort topo = new TopoSort();
        List<Integer> order = topo.run(graph);

        // Проверяем, что топологический порядок содержит все вершины
        assertEquals(4, order.size());
        // Проверяем корректность порядка: 3 должен идти после 1 и 2
        assertTrue(order.indexOf(3) > order.indexOf(1));
        assertTrue(order.indexOf(3) > order.indexOf(2));
    }
}