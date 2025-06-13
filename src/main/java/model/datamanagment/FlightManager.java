package model.datamanagment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Airport;
import model.Flight;
import model.tda.CircularDoublyLinkedList;
import model.tda.ListException;
import model.tda.SinglyLinkedList;

import java.io.File;
import java.util.List;

public class FlightManager {
    private CircularDoublyLinkedList flights;
    private final String filePath="src/main/java/data/flights.json";

    public FlightManager() {
        flights = new CircularDoublyLinkedList();
        loadFlights();
    }
    public void loadFlights() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            File file = new File(filePath);
            if (!file.exists()) return;
            List<Flight> flightList = mapper.readValue(file, new TypeReference<>() {});
            for (Flight flight : flightList) {
                flights.add(flight);
                System.out.println("Vuelo cargado desde JSON:");
                System.out.println("  Codigo: " + flight.getFlightID());
                System.out.println("  Origen: " + flight.getOrigin());
                System.out.println("  Destino: " + flight.getDestination());
                System.out.println("  Hora de salida: " + flight.getDepartureTime());
                System.out.println("  Capacidad: " + flight.getCapacity());
                System.out.println("  Cantidad de pasajeros: " + flight.getOccupancy());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addFlight(Flight flight) throws ListException {
        if (flights.contains(flight))
            throw new ListException("El vuelo ya esta registrado");
        flights.add(flight);
        saveFlights();
    }
    public void removeFlight(Flight flight) throws ListException {
        flights.remove(flight);
        saveFlights();
    }
    public void updateFlight(Flight flight) throws ListException {
        flights.remove(flight);
        flights.add(flight);
        saveFlights();
    }


    private void saveFlights() {
        File file = new File(filePath);
        if(file.exists())
            file.delete();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            List<Flight> list = flights.toTypedList();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CircularDoublyLinkedList getFlights() {
        return flights;
    }
    public void setFlights(CircularDoublyLinkedList flights) {
        this.flights = flights;
        saveFlights();
    }
}
