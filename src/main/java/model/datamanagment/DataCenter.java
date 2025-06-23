package model.datamanagment;

import model.tda.AVL;
import model.tda.CircularDoublyLinkedList;
import model.tda.DoublyLinkedList;
import model.tda.LinkedQueue;
import model.tda.graph.DirectedSinglyLinkedListGraph;

public class DataCenter {
    private static DoublyLinkedList airports;
    private static AVL passengers;
    private static CircularDoublyLinkedList flights;
    private static DirectedSinglyLinkedListGraph routes;
    private static AirportManager airportManager;
    private static FlightManager flightManager;
    private static PassengerManager passengerManager;
    private static RouteManager routeManager;
    private static LinkedQueue operationsQueue;

    static {
        operationsQueue = new LinkedQueue();
        airportManager = new AirportManager();
        flightManager = new FlightManager();
        passengerManager = new PassengerManager();
        routeManager = new RouteManager();
        airports = new DoublyLinkedList();
        passengers = new AVL();
        flights = new CircularDoublyLinkedList();
        routes = new DirectedSinglyLinkedListGraph();
        load();
    }

    public DataCenter() {
    }
    private static void load() {
        airports = airportManager.getAirports();
        passengers = passengerManager.getPassengers();
        flights = flightManager.getFlights();
        routes=routeManager.getAirportsGraph();
    }
    public static void enQueueOperation(String operation) {
        operationsQueue.enQueue(operation);
        System.out.println(operation);
    }

    public static DoublyLinkedList getAirports() {
        return airports;
    }

    public static AVL getPassengers() {
        return passengers;
    }

    public static CircularDoublyLinkedList getFlights() {
        return flights;
    }

    public static DirectedSinglyLinkedListGraph getRoutes() {
        return routes;
    }
}
