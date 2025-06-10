package model.datamanagment;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Airport;
import model.User;
import model.tda.SinglyLinkedList;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class AirportManager{
    private SinglyLinkedList airports;
    private final String filePath ="C:\\Users\\DanielSV\\Documents\\2025\\Proyecto Algoritmos y Estructuras de Datos\\cretaAirlines\\src\\main\\java\\data\\airports.json";

    public AirportManager() {
        airports = new SinglyLinkedList();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAirports(Airport airport) {
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

    public SinglyLinkedList getAirports() {
        return airports;
    }

}
