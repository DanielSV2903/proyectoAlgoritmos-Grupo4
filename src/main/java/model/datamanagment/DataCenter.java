package model.datamanagment;

import controller.LoginController;
import model.Passenger;
import model.User;
import model.tda.*;
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

    public static Passenger getUserPassenger() throws ListException {
        Passenger passenger = new Passenger();
          User user =LoginController.getCurrentUser();
          passenger.setId(user.getId());
          passenger.setName(user.getName());
          passenger.setPriority(3);
        return passenger;
    }
}
