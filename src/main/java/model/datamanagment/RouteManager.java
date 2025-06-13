package model.datamanagment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Airport;
import model.Route;
import model.serializers.SinglyLinkedListDeserializer;
import model.tda.DoublyLinkedList;
import model.tda.ListException;
import model.tda.SinglyLinkedList;
import model.tda.graph.DirectedSinglyLinkedListGraph;
import model.tda.graph.GraphException;
import model.tda.graph.Vertex;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RouteManager {
    private DirectedSinglyLinkedListGraph airportsGraph;
    private AirportManager airportManager;
    private DoublyLinkedList airports;
    private SinglyLinkedList routes; // Lista de rutas
        private SinglyLinkedList vertexList;
    private final String filePath = "src/main/java/data/routes.json";

    public RouteManager() {
        this.routes = new SinglyLinkedList();
        this.airportsGraph = new DirectedSinglyLinkedListGraph();
        this.airportManager = new AirportManager();
        loadRoutes();
    }

    public void loadRoutes() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            SimpleModule module = new SimpleModule();
//            module.addDeserializer(SinglyLinkedList.class, new SinglyLinkedListDeserializer());
            mapper.registerModule(module);

            File file = new File(filePath);
            if (!file.exists()) return;

            List<Route> routeList = mapper.readValue(file, new TypeReference<>() {});
            for (Route route : routeList) {
                routes.add(route);

                String origin = route.getOrigin_airport_id();
                String destination = route.getDestination_airport_id();
                int distance = route.getDistance();

                    airportsGraph.addVertex(origin);
                    airportsGraph.addVertex(destination);

                airportsGraph.addEdgeWeight(origin, destination, distance);

                System.out.println("Ruta cargada: " + origin + " -> " + destination + " (" + distance + " km)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveRoutes() {
        File file = new File(filePath);
        if (file.exists())
            file.delete();

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            List<Route> list = routes.toTypedList(); // Asegúrate de que este método esté correctamente implementado
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRoute(Route route) throws ListException {
        if (routes.isEmpty() || !routes.contains(route)) {
            routes.add(route);
            saveRoutes();
        }
    }

    public void removeRoute(Route route) throws ListException {
        if (!routes.contains(route))
            throw new ListException("Route " + route + " not found");
        routes.remove(route);
        saveRoutes();
    }

    public void addRoute(String originAirportId, String destinationAirpotId, int distance) throws ListException, GraphException {
        Route route = new Route(originAirportId, destinationAirpotId, distance);

        if (airportsGraph.isEmpty()||!airportsGraph.containsVertex(originAirportId))
            airportsGraph.addVertex(originAirportId);

        if (!airportsGraph.containsVertex(destinationAirpotId))
            airportsGraph.addVertex(destinationAirpotId);

        airportsGraph.addEdgeWeight(originAirportId, destinationAirpotId, distance);

        routes.add(route);
        saveRoutes();
    }

    public List<Integer> calculateShortestRoute(String originAirportId, String destinationAirportId) throws GraphException, ListException {
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

    public void updateRoute(Route updatedRoute) throws ListException {
        if (!routes.contains(updatedRoute))
            throw new ListException("Route with origin ID " + updatedRoute.getOrigin_airport_id() + " not found");
        routes.remove(updatedRoute);
        routes.add(updatedRoute);
        saveRoutes();
    }

    public SinglyLinkedList getRoutes() {
        return routes;
    }

    public void setRoutes(SinglyLinkedList routes) {
        this.routes = routes;
    }

    public DirectedSinglyLinkedListGraph getAirportsGraph() {
        return airportsGraph;
    }

    public void setAirportsGraph(DirectedSinglyLinkedListGraph airportsGraph) {
        this.airportsGraph = airportsGraph;
    }

}

