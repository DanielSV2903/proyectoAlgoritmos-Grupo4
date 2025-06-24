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

    public String boardPassengers(Flight flight) throws QueueException, ListException {
        String originCode = flight.getOrigin().getCode();
        LinkedQueue queue = airportQueueManager.getQueue(originCode);

        StringBuilder report = new StringBuilder();
        int passengersBoarded = 0;

        report.append("Iniciando embarque para el vuelo: ").append(flight.getFlightID()).append(" desde ").append(originCode).append("\n");

        while (flight.hasAvailableSeats() && !queue.isEmpty()) {
            Passenger passenger = (Passenger) queue.deQueue();
            boolean boarded = flight.addPassenger(passenger);

            if (boarded) {
                report.append("Pasajero embarcado: ").append(passenger.getName()).append(" al vuelo ").append(flight.getFlightID()).append("\n");
                passengersBoarded++;
            } else {
                report.append("No se pudo embarcar al pasajero: ").append(passenger.getName()).append("\n");
            }
        }

        if (!flight.hasAvailableSeats()) {
            report.append("El vuelo ").append(flight.getFlightID()).append(" está lleno.\n");
        }

        if (queue.isEmpty()) {
            report.append("Todos los pasajeros en la cola del aeropuerto ").append(originCode).append(" han sido abordados.\n");
        }

        report.append("Resumen: ").append(passengersBoarded).append(" pasajeros embarcados\n");
        report.append("Ocupación final: ").append(flight.getOccupancy()).append("/").append(flight.getCapacity()).append("\n");

        // Mantener salida en consola si es necesario
        System.out.println(report.toString());

        return report.toString();
    }
}