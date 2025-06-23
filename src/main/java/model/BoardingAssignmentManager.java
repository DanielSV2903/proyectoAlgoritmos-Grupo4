package model;

import javafx.scene.control.Alert;
import model.Flight;
import model.Passenger;
import model.datamanagment.FlightManager;
import model.tda.CircularDoublyLinkedList;
import model.tda.ListException;
import model.tda.Node;
import model.tda.QueueException;

public class BoardingAssignmentManager {

    private final AirportQueueManager airportQueueManager;
    private final FlightManager flightManager;

    public BoardingAssignmentManager(AirportQueueManager airportQueueManager, FlightManager flightManager) {
        this.airportQueueManager = airportQueueManager;
        this.flightManager = flightManager;
    }

    public void assignPassengersToQueues() throws ListException, QueueException {
        CircularDoublyLinkedList flights = flightManager.getFlights();

        for (int i = 1; i <= flights.size(); i++) {
            Flight flight = (Flight) flights.getNode(i).data;
            String originCode = flight.getOrigin().getCode();

            airportQueueManager.addAirport(flight.getOrigin());

            // ✅ Verificar si la lista de pasajeros NO está vacía antes de iterar
            if (!flight.getPassengers().isEmpty()) {
                for (int j = 1; j <= flight.getPassengers().size(); j++) {
                    Passenger passenger = (Passenger) flight.getPassengers().getNode(j).data;
                    airportQueueManager.addPassengerToQueue(originCode, passenger);
                }
            }
        }

        System.out.println("Pasajeros asignados correctamente a las colas de abordaje.");
    }
}