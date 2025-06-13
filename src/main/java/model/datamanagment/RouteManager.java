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
import model.tda.graph.EdgeWeight;
import model.tda.graph.GraphException;
import model.tda.graph.Vertex;

import java.io.File;
import java.util.*;

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
        airports = airportManager.getAirports();
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
            route.setRoute_id(routes.size()+1);
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
        int id=0;
        if (!routes.isEmpty())
            id=routes.size();
        Route route = new Route(id+1,originAirportId, destinationAirpotId, distance);
        route.addDestination(destinationAirpotId);
        if (airportsGraph.isEmpty()||!airportsGraph.containsVertex(originAirportId))
            airportsGraph.addVertex(originAirportId);

        if (!airportsGraph.containsVertex(destinationAirpotId))
            airportsGraph.addVertex(destinationAirpotId);

        airportsGraph.addEdgeWeight(originAirportId, destinationAirpotId, distance);

        routes.add(route);
        saveRoutes();
    }

    public void updateRoute(Route updatedRoute) throws ListException {
        if (!routes.contains(updatedRoute))
            throw new ListException("Route with origin ID " + updatedRoute.getOrigin_airport_id() + " not found");
        routes.remove(updatedRoute);
        routes.add(updatedRoute);
        saveRoutes();
    }
    //Revisar estos metodos

    public void generateRandomRoutes() throws ListException, GraphException {
        List<Airport> airportList = airports.toTypedList();
        Random random = new Random();

        for (Airport airport : airportList) {
            airportsGraph.addVertex(airport);
        }

        Set<Airport> connected = new HashSet<>();
        Set<Airport> unconnected = new HashSet<>(airportList);

        Airport start = airportList.get(random.nextInt(airportList.size()));
        connected.add(start);
        unconnected.remove(start);

        while (!unconnected.isEmpty()) {
            Airport from = getRandomElement(connected, random);
            Airport to = getRandomElement(unconnected, random);

            int weight = 1 + random.nextInt(20);
            airportsGraph.addEdgeWeight(from, to, weight);
            connected.add(to);
            unconnected.remove(to);
        }
        int extraEdges = airportList.size(); // Puedes ajustar cuántas aristas extra deseas
        for (int i = 0; i < extraEdges; i++) {
            Airport origin = airportList.get(random.nextInt(airportList.size()));
            Airport destination = airportList.get(random.nextInt(airportList.size()));

            if (!origin.getCode().equals(destination.getCode()) &&
                    !airportsGraph.containsEdge(origin, destination)) {
                int weight = 1 + random.nextInt(20);
                airportsGraph.addEdgeWeight(origin, destination, weight);
            }
        }
    }

    private Airport getRandomElement(Set<Airport> set, Random random) {
        int index = random.nextInt(set.size());
        int i = 0;
        for (Airport airport : set) {
            if (i == index) return airport;
            i++;
        }
        return null; // nunca se debería llegar aquí
    }
//    public void generateRandomRoutes() throws ListException, GraphException {
//        List<Airport> airportList = airports.toTypedList();
//        for (Airport airport : airportList) {
//            airportsGraph.addVertex(airport);
//        }
//        Random random = new java.util.Random();
//
//        for (Airport origin : airportList) {
//            //Mezclar la lista para obtener destinos aleatorios
//            List<Airport> shuffledDestinations = new ArrayList<>(airportList);
//            Collections.shuffle(shuffledDestinations, random);
//
//            //Número aleatorio de conexiones
//            int connections = 1 + random.nextInt(Math.min(3, shuffledDestinations.size() - 1));
//
//            int count = 0;
//            for (Airport destination : shuffledDestinations) {
//                if (!origin.getCode().equals(destination.getCode()) && !airportsGraph.containsEdge(origin, destination)) {
//                    int weight = 1 + random.nextInt(20); // Peso entre 1 y 20
//                    airportsGraph.addEdgeWeight(origin, destination, weight);
//                    count++;
//                }
//                if (count >= connections) break;
//            }
//        }
//    }


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

