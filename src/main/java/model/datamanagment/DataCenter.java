package model.datamanagment;

import model.tda.AVL;
import model.tda.CircularDoublyLinkedList;
import model.tda.DoublyLinkedList;
import model.tda.LinkedQueue;
import model.tda.graph.DirectedSinglyLinkedListGraph;

public class DataCenter {
    private DoublyLinkedList airports;
    private AVL passengers;
    private CircularDoublyLinkedList flights;
    private DirectedSinglyLinkedListGraph routes;
    private AirportManager airportManager;
    private FlightManager flightManager;
    private PassengerManager passengerManager;
    private RouteManager routeManager;
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
    private void load() {
        airports = airportManager.getAirports();
        passengers = passengerManager.getPassengers();
        flights = flightManager.getFlights();
        routes=routeManager.getAirportsGraph();
    }
    public static void enQueueOperation(String operation) {
        operationsQueue.enQueue(operation);
        System.out.println(operation);
    }
}
