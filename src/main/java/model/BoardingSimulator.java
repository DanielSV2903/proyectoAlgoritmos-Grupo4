package model;

import model.Flight;
import model.Passenger;
import model.tda.LinkedQueue;
import model.tda.ListException;
import model.tda.QueueException;

public class BoardingSimulator {

    private final AirportQueueManager airportQueueManager;

    public BoardingSimulator(AirportQueueManager airportQueueManager) {
        this.airportQueueManager = airportQueueManager;
    }

    public void boardPassengers(Flight flight) throws QueueException, ListException {
        String originCode = flight.getOrigin().getCode();
        LinkedQueue queue = airportQueueManager.getQueue(originCode);

        System.out.println("Iniciando embarque para el vuelo: " + flight.getFlightID() + " desde " + originCode);

        while (flight.hasAvailableSeats() && !queue.isEmpty()) {
            Passenger passenger = (Passenger) queue.deQueue();
            boolean boarded = flight.addPassenger(passenger);

            if (boarded) {
                System.out.println("Pasajero embarcado: " + passenger.getName() + " al vuelo " + flight.getFlightID());
            } else {
                System.out.println("No se pudo embarcar al pasajero: " + passenger.getName());
            }
        }

        if (!flight.hasAvailableSeats()) {
            System.out.println("El vuelo " + flight.getFlightID() + " está lleno.");
        }

        if (queue.isEmpty()) {
            System.out.println("Todos los pasajeros en la cola del aeropuerto " + originCode + " han sido abordados.");
        }
    }
}
