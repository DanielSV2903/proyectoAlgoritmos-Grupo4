package model.datamanagment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Airport;
import model.Flight;
import model.User;
import model.serializers.PassengerSinglyLinkedListDeserializer;
import model.serializers.SinglyLinkedListDeserializer;
import model.tda.DoublyLinkedList;
import model.tda.ListException;
import model.tda.SinglyLinkedList;
import util.Utility;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class AirportManager{
    @JsonDeserialize(using = SinglyLinkedListDeserializer.class)
    private DoublyLinkedList airports;
    private final String filePath ="src/main/java/data/airports.json";

    public AirportManager() {
        airports = new DoublyLinkedList();
         loadAirports();
    }

    public void loadAirports() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(filePath);
            if (!file.exists()) return;
            List<Airport> airportList = mapper.readValue(file, new TypeReference<>() {});
            for (Airport a : airportList) {
                airports.add(a);
                System.out.println("Aeropuerto cargado desde JSON:");
                System.out.println("  Nombre: " + a.getName());
                System.out.println("  City: " + a.getCity());
                System.out.println("  Codigo: " + a.getCode());
                System.out.println("  Pais: " + a.getCountry());
                loadConections(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadConections(Airport airport){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Agregar el módulo aquí
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            File file = new File("src/main/java/data/flights.json");
            if (!file.exists()) {
                System.out.println("No se encontró el archivo de flights.json");
            }
            List<Flight> flightList = mapper.readValue(file, new TypeReference<List<Flight>>() {});
            if (flightList == null || flightList.isEmpty()) {
                System.out.println("No tiene vuelos asociados.");
            } else {
                for (Flight f : flightList) {
                    Airport fOrigin = f.getOrigin();
                    Airport fDestination = f.getDestination();
                    if (Utility.compare(fOrigin, airport) == 0 || Utility.compare(fDestination, airport) == 0){
                        System.out.println("Vuelos relacionados: ");
                        System.out.println("ID Vuelo: " + f.getFlightID() + " - Origen del vuelo: " + f.getOrigin() + " - Destino del vuelo: " + f.getDestination());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addAirports(Airport airport) throws ListException {
        if (!airports.isEmpty()&&airports.contains(airport))
            throw new ListException("El aeropuerto ya existe");
        airports.add(airport);
        saveAirports();
    }
    public void updateAirport(Airport airport) throws ListException {
        airports.remove(airport);
        airports.add(airport);
        saveAirports();
    }

    public void saveAirports() {
        File file = new File(filePath);
        if(file.exists())
            file.delete();
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Airport> list = airports.toTypedList();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DoublyLinkedList getAirports() {
        return airports;
    }

    public void removeAirport(Airport a) throws ListException {
        airports.remove(a);
        saveAirports();
    }
}
