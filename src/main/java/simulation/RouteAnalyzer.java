package simulation;

import model.Airport;
import model.Flight;
import model.tda.CircularDoublyLinkedList;
import model.tda.ListException;

import java.util.*;

public class RouteAnalyzer {
    private final CircularDoublyLinkedList flights;

    public RouteAnalyzer(CircularDoublyLinkedList flights) {
        this.flights = flights;
    }

    public List<Flight> findBestRoute(Airport origin, Airport destination) throws ListException {
        Map<String, List<Flight>> routeMap = new HashMap<>();
        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<Airport> queue = new PriorityQueue<>(
                Comparator.comparingInt(a -> distances.getOrDefault(a.getCode(), Integer.MAX_VALUE))
        );

        // Inicializar distancias
        distances.put(origin.getCode(), 0);
        queue.offer(origin);

        while (!queue.isEmpty()) {
            Airport current = queue.poll();

            if (current.getCode().equals(destination.getCode())) {
                return reconstructRoute(routeMap, origin, destination);
            }

            // Encontrar vuelos desde el aeropuerto actual
            List<Flight> availableFlights = getAvailableFlights(current);
            for (Flight flight : availableFlights) {
                Airport next = flight.getDestination();
                int newDistance = distances.get(current.getCode()) + 1;

                if (newDistance < distances.getOrDefault(next.getCode(), Integer.MAX_VALUE)) {
                    distances.put(next.getCode(), newDistance);
                    List<Flight> route = routeMap.getOrDefault(current.getCode(), new ArrayList<>());
                    route.add(flight);
                    routeMap.put(next.getCode(), route);
                    queue.offer(next);
                }
            }
        }

        return new ArrayList<>(); // No se encontró ruta
    }

    private List<Flight> getAvailableFlights(Airport airport) throws ListException {
        List<Flight> availableFlights = new ArrayList<>();
        for (int i = 1; i <= flights.size(); i++) {
            Flight flight = (Flight) flights.getNode(i).data;
            if (flight.getOrigin().getCode().equals(airport.getCode())) {
                availableFlights.add(flight);
            }
        }
        return availableFlights;
    }

    private List<Flight> reconstructRoute(Map<String, List<Flight>> routeMap,
                                          Airport origin, Airport destination) {
        List<Flight> route = new ArrayList<>();
        String currentCode = destination.getCode();

        while (!currentCode.equals(origin.getCode())) {
            List<Flight> flights = routeMap.get(currentCode);
            if (flights == null || flights.isEmpty()) break;

            Flight flight = flights.get(flights.size() - 1);
            route.add(0, flight);
            currentCode = flight.getOrigin().getCode();
        }

        return route;
    }
}
