import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;


/**
 *
 * @author Zihan Xu
 * @version 1.0
 */
public class GraphAlgorithms {

    /**
     * @throws IllegalArgumentException if any input is null, or if
     *         {@code start} doesn't exist in the graph
     * @param start the Vertex you are starting at
     * @param graph the Graph we are searching
     * @param <T> the data type representing the vertices in the graph.
     * @return a List of vertices in the order that you visited them
     */
    public static <T> List<Vertex<T>> breadthFirstSearch(Vertex<T> start,
                                                         Graph<T> graph) {
        if (start == null) {
            throw new IllegalArgumentException(
                    "Start is null. Cannot search.\n");
        }
        if (graph == null) {
            throw new IllegalArgumentException(
                    "Graph is null. Cannot search.\n");
        }
        Map<Vertex<T>, List<VertexDistancePair<T>>> adjacencyList =
                graph.getAdjacencyList();
        if (!adjacencyList.containsKey(start)) {
            throw new IllegalArgumentException(
                    "Graph does not contain given start key. Cannot search\n");
        }
        Queue<Vertex<T>> verticesQueue = new LinkedList<>();
        List<Vertex<T>> visitedList = new ArrayList<>();
        Vertex<T> currentVertex = start;
        verticesQueue.add(start);
        while (!verticesQueue.isEmpty()) {
            for (VertexDistancePair<T> vertexPair
                    : adjacencyList.get(currentVertex)) {
                if (!visitedList.contains(vertexPair.getVertex())) {
                    verticesQueue.add(vertexPair.getVertex());
                }
            }
            Vertex<T> vertex = verticesQueue.remove();
            if (!visitedList.contains(vertex)) {
                visitedList.add(vertex);
                currentVertex = vertex;
            }
        }
        return visitedList;
    }

}
