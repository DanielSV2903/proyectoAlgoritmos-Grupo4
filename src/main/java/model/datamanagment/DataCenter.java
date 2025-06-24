package model.datamanagment;

import controller.LoginController;
import model.Passenger;
import model.User;
import model.tda.*;
import model.tda.graph.DirectedSinglyLinkedListGraph;

public class DataCenter {
    private  DoublyLinkedList airports;
    private  AVL passengers;
    private  CircularDoublyLinkedList flights;
    private  DirectedSinglyLinkedListGraph routes;
    private  AirportManager airportManager;
    private  FlightManager flightManager;
    private  PassengerManager passengerManager;
    private  RouteManager routeManager;
    private static LinkedQueue operationsQueue;

    static {
        operationsQueue = new LinkedQueue();
    }

    public DataCenter() {
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
    private  void load() {
        airports = airportManager.getAirports();
        passengers = passengerManager.getPassengers();
        flights = flightManager.getFlights();
        routes=routeManager.getAirportsGraph();
    }
    public static void enQueueOperation(String operation) {
        operationsQueue.enQueue(operation);
        System.out.println(operation);
    }

    public  DoublyLinkedList getAirports() {
        return airports;
    }

    public  AVL getPassengers() {
        return passengers;
    }

    public  CircularDoublyLinkedList getFlights() {
        return flights;
    }

    public  DirectedSinglyLinkedListGraph getRoutes() {
        return routes;
    }

    public  Passenger getUserPassenger() throws ListException {
        Passenger passenger = new Passenger();
          User user =LoginController.getCurrentUser();
          passenger.setId(user.getId());
          passenger.setName(user.getName());
          passenger.setPriority(3);
        return passenger;
    }
}
