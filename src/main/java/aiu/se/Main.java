package aiu.se;

import com.google.gson.*;
import graph.scc.TarjanSCC;
import graph.topo.TopoSort;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String inputPath = "src/main/resources/tasks.json";
        String outputPath = "src/main/resources/output.json";

        try {
            // 1️⃣ Считываем входной JSON
            String jsonText = Files.readString(Path.of(inputPath));
            JsonObject jsonObj = JsonParser.parseString(jsonText).getAsJsonObject();
            JsonArray graphs = jsonObj.getAsJsonArray("graphs");

            JsonArray resultsArray = new JsonArray();

            // 2️⃣ Перебираем все графы
            for (JsonElement gElem : graphs) {
                JsonObject gObj = gElem.getAsJsonObject();
                int id = gObj.get("id").getAsInt();
                JsonArray edges = gObj.getAsJsonArray("edges");
                JsonArray nodes = gObj.getAsJsonArray("nodes");

                // 3️⃣ Строим граф в формате Map<Integer, List<Integer>>
                Map<Integer, List<Integer>> graph = new HashMap<>();
                for (JsonElement nodeElem : nodes) {
                    graph.put(nodeElem.getAsInt(), new ArrayList<>());
                }
                for (JsonElement edgeElem : edges) {
                    JsonObject e = edgeElem.getAsJsonObject();
                    int from = e.get("from").getAsInt();
                    int to = e.get("to").getAsInt();
                    graph.get(from).add(to);
                }

                // 4️⃣ Запускаем Tarjan (поиск сильно связанных компонент)
                TarjanSCC tarjan = new TarjanSCC();
                double startTarjan = System.nanoTime();
                List<List<Integer>> sccs = tarjan.run(graph);
                double endTarjan = System.nanoTime();
                double timeTarjanMs = (endTarjan - startTarjan) / 1_000_000.0;

                // 5️⃣ Запускаем TopoSort (топологическую сортировку)
                TopoSort topo = new TopoSort();
                double startTopo = System.nanoTime();
                List<Integer> topoOrder = topo.run(graph);
                double endTopo = System.nanoTime();
                double timeTopoMs = (endTopo - startTopo) / 1_000_000.0;

                // 6️⃣ Формируем JSON-результат
                JsonObject resultObj = new JsonObject();
                resultObj.addProperty("graph_id", id);
                resultObj.addProperty("num_nodes", nodes.size());
                resultObj.addProperty("num_edges", edges.size());

                JsonObject tarjanObj = new JsonObject();
                tarjanObj.addProperty("executionTimeMs", timeTarjanMs);
                tarjanObj.add("SCCs", gson.toJsonTree(sccs));
                resultObj.add("Tarjan", tarjanObj);

                JsonObject topoObj = new JsonObject();
                topoObj.addProperty("executionTimeMs", timeTopoMs);
                topoObj.add("TopoOrder", gson.toJsonTree(topoOrder));
                resultObj.add("TopoSort", topoObj);

                // Добавляем результат в общий массив
                resultsArray.add(resultObj);
            }

            // 7️⃣ Записываем всё в выходной JSON
            JsonObject output = new JsonObject();
            output.add("results", resultsArray);
            try (FileWriter writer = new FileWriter(outputPath)) {
                gson.toJson(output, writer);
            }

            System.out.println("✅ Results written to " + outputPath);

            // 8️⃣ Экспорт результатов в CSV-файл
            try (FileWriter csvWriter = new FileWriter("src/main/resources/results.csv")) {
                csvWriter.append("graph_id,num_nodes,num_edges,tarjan_time_ms,toposort_time_ms\n");
                JsonArray parsedResults = JsonParser.parseString(Files.readString(Path.of(outputPath)))
                        .getAsJsonObject()
                        .getAsJsonArray("results");

                for (JsonElement elem : parsedResults) {
                    JsonObject obj = elem.getAsJsonObject();
                    int id = obj.get("graph_id").getAsInt();
                    int nodes = obj.get("num_nodes").getAsInt();
                    int edges = obj.get("num_edges").getAsInt();

                    JsonObject tarjan = obj.getAsJsonObject("Tarjan");
                    JsonObject topo = obj.getAsJsonObject("TopoSort");

                    double t1 = tarjan.get("executionTimeMs").getAsDouble();
                    double t2 = topo.get("executionTimeMs").getAsDouble();

                    csvWriter.append(String.format("%d,%d,%d,%.6f,%.6f\n", id, nodes, edges, t1, t2));
                }
                System.out.println("✅ CSV file written to src/main/resources/results.csv");
            } catch (IOException e) {
                System.err.println("❌ Error writing CSV: " + e.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}