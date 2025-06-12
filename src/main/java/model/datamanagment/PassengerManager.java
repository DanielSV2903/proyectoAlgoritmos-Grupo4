package model.datamanagment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Flight;
import model.Passenger;
import model.serializers.SinglyLinkedListDeserializer;
import model.tda.*;

import java.io.File;
import java.util.List;

public class PassengerManager {
    private AVL passengers;
    private final String filePath="src/main/java/data/passengers.json";
    public PassengerManager() {
        passengers = new AVL();
        loadPassengers();
    }
    public void loadPassengers() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            SimpleModule module = new SimpleModule();
            module.addDeserializer(SinglyLinkedList.class, new SinglyLinkedListDeserializer());
            mapper.registerModule(module);
            File file = new File(filePath);
            if (!file.exists()) return;
            List<Passenger> passengerList = mapper.readValue(file, new TypeReference<>() {});
            for (Passenger passenger : passengerList) {
                passengers.add(passenger);
                System.out.println("Pasajero cargado desde JSON:");
                System.out.println("  Codigo: " + passenger.getId());
                System.out.println("  Nombre: " + passenger.getName());
                System.out.println("  Nacionalidad: " + passenger.getNationality());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addPassenger(Passenger passenger) throws TreeException {
        if (passengers.contains(passenger))
            throw new TreeException("El pasagero ya fue registrado");
        passengers.add(passenger);
        saveFlights();
    }
    public void removePassenger(Passenger passenger) throws TreeException {
        passengers.remove(passenger);
        saveFlights();
    }
    public void updatePassenger(Passenger passenger) throws TreeException {
        if (!passengers.contains(passenger)) {
            throw new TreeException("El pasajero no existe");
        }
        passengers.remove(passenger);
        passengers.add(passenger);
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
            List<Passenger> list = (List<Passenger>) passengers.toTypedList(Passenger.class);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AVL getPassengers() {
        return passengers;
    }

    public void setPassengers(AVL passengers) {
        this.passengers = passengers;
    }
}
