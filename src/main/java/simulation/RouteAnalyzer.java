package simulation;

import model.Airport;
import model.Flight;
import model.Route;
import model.datamanagment.RouteManager;
import model.tda.CircularDoublyLinkedList;
import model.tda.ListException;
import model.tda.SinglyLinkedList;

import java.util.*;

public class RouteAnalyzer {
    private final CircularDoublyLinkedList flights;

    public RouteAnalyzer(CircularDoublyLinkedList flights) {
        this.flights = flights;
    }

    public Route findBestRoute(Airport origin, Airport destination) throws ListException {
        Route route=null;
        RouteManager rm = new RouteManager();
        SinglyLinkedList routes = rm.getRoutes();
        for (int i = 1; i <= routes.size(); i++) {
            Route r=(Route) routes.getNode(i).data;
            if (r.getOrigin_airport_id().equals(origin.getCode())&&r.getDestination_airport_id().equals(destination.getCode())) {
                route=r;
            }
        }
        return route;
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
