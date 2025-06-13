package model.datamanagment;

import model.Airport;
import model.Route;
import model.tda.ListException;
import model.tda.SinglyLinkedList;
import model.tda.graph.DirectedSinglyLinkedListGraph;
import model.tda.graph.GraphException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RouteManager {
    private DirectedSinglyLinkedListGraph airportsGraph;
    private SinglyLinkedList vertexList;

    public RouteManager() {
        this.airportsGraph = new DirectedSinglyLinkedListGraph();
        this.vertexList = new SinglyLinkedList();
    }

    public void addRoute(int originAirportId, int destinationAirpotId, int distance) throws ListException, GraphException {

        //verifiaciones que no sé si sean necesarias
        if (!airportsGraph.containsVertex(originAirportId))
            airportsGraph.addVertex(originAirportId);

        if (!airportsGraph.containsVertex(destinationAirpotId))
            airportsGraph.addVertex(destinationAirpotId);

        airportsGraph.addEdgeWeight(originAirportId, destinationAirpotId, distance);
    }

    public void modifyRoute(int originAirportId, int destinationAirpotId, int distance) throws ListException, GraphException {

        if (!airportsGraph.containsEdge(originAirportId, destinationAirpotId))
            throw new GraphException("La ruta entre los aeropuertos no existe");

        airportsGraph.addEdgeWeight(originAirportId, destinationAirpotId, distance);
    }

    public List<Integer> calculateShortestRoute(int originAirportId, int destinationAirportId) throws GraphException, ListException {
        if (!airportsGraph.containsVertex(originAirportId) || !airportsGraph.containsVertex(destinationAirportId)) {
            throw new GraphException("Origen o destino inválidos en el grafo.");
        }

        // Ejecutar el algoritmo de Dijkstra para obtener distancias mínimas
        Map<Object, Integer> distances = airportsGraph.dijkstra(originAirportId);
        Map<Object, Object> previous = new HashMap<>(); // Predecesores para reconstruir ruta

        // Inicialización del mapa de predecesores y distancias
        vertexList = airportsGraph.getVertexList();
        for (int i = 1; i <= vertexList.size(); i++) {
            Object vertex = vertexList.getNode(i).data;
            previous.put(vertex, null); // Sin predecesor inicial
        }

        // Reconstruir el camino más corto
        List<Integer> path = new LinkedList<>();
        for (Object at = destinationAirportId; at != null; at = previous.get(at)) {
            path.add(0, (Integer) at); // Insertar al inicio para generar el orden correcto
        }

        // Verificar si no se encontró una ruta válida
        if (path.size() == 1 && !path.get(0).equals(originAirportId)) {
            throw new GraphException("No existe una ruta entre los aeropuertos proporcionados.");
        }

        return path;
    }
}

