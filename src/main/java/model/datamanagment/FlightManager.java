package model.datamanagment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Airport;
import model.Flight;
import model.tda.CircularDoublyLinkedList;
import model.tda.SinglyLinkedList;

import java.io.File;
import java.util.List;

public class FlightManager {
    private CircularDoublyLinkedList flights;
    private final String filePath="C:\\Users\\DanielSV\\Documents\\2025\\Proyecto Algoritmos y Estructuras de Datos\\cretaAirlines\\src\\main\\java\\data\\flights.json";

    public FlightManager() {
        flights = new CircularDoublyLinkedList();
        loadFlights();
    }
    public void loadFlights() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(filePath);
            if (!file.exists()) return;
            List<Flight> flightList = mapper.readValue(file, new TypeReference<>() {});
            for (Flight flight : flightList) {
                flights.add(flight);
                System.out.println("Aeropuerto cargado desde JSON:");
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

    public void addAirports(Airport airport) {
        flights.add(airport);
        saveAirports();
    }

    private void saveAirports() {
        File file = new File(filePath);
        if(file.exists())
            file.delete();
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Flight> list = flights.toTypedList();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CircularDoublyLinkedList getFlights() {
        return flights;
    }
}
