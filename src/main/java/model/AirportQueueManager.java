package model;

import model.tda.LinkedQueue;
import model.tda.QueueException;

import java.util.HashMap;

public class AirportQueueManager {
    private final HashMap<String, LinkedQueue> airportQueues;

    public AirportQueueManager() {
        this.airportQueues = new HashMap<>();
    }

    public void addAirport(Airport airport) {
        if (!airportQueues.containsKey(airport.getCode())) {
            airportQueues.put(airport.getCode(), new LinkedQueue());
        }
    }

    public void addPassengerToQueue(String airportCode, Passenger passenger) throws QueueException {
        if (!airportQueues.containsKey(airportCode)) {
            throw new QueueException("El aeropuerto no existe en las colas.");
        }
        airportQueues.get(airportCode).enQueue(passenger);
    }

    public LinkedQueue getQueue(String airportCode) {
        return airportQueues.get(airportCode);
    }

    public boolean hasPassengers(String airportCode) throws QueueException {
        return airportQueues.containsKey(airportCode) && !airportQueues.get(airportCode).isEmpty();
    }

    public Passenger dequeuePassenger(String airportCode) throws QueueException {
        if (!airportQueues.containsKey(airportCode) || airportQueues.get(airportCode).isEmpty()) {
            throw new QueueException("No hay pasajeros en la cola de este aeropuerto.");
        }
        return (Passenger) airportQueues.get(airportCode).deQueue();
    }

}
