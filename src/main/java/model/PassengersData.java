package model;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class PassengersData {
    private static final String filepath = "src/main/java/data/passengers.json";
    private static final String[] names = {
            "Alejandro", "Valentina", "Mateo", "Camila", "Daniel", "Isabella", "Lucas", "Sofía", "Sebastián", "Mariana",
            "Gabriel", "Antonella", "Emiliano", "Victoria", "Samuel", "Nicole", "Diego", "Renata", "David", "Fernanda",
            "Andrés", "Ximena", "Tomás", "Mía", "Juan", "Salomé", "Adrián", "Romina", "Felipe", "Zoe",
            "Bruno", "Jazmín", "Liam", "Emilia", "Iván", "Carla", "Ángel", "Amanda", "Axel", "Danna",
            "Gael", "Paula", "Maximiliano", "Carolina", "Benjamín", "Alejandra", "Isaac", "Daniela", "Santiago", "Julieta"
    };
    private static final String[] lastNames = {
            "González", "Rodríguez", "García", "Martínez", "López", "Hernández", "Pérez", "Sánchez", "Ramírez", "Cruz",
            "Flores", "Morales", "Vargas", "Jiménez", "Rojas", "Navarro", "Torres", "Castillo", "Ortega", "Romero",
            "Suárez", "Herrera", "Silva", "Álvarez", "Mendoza", "Ibarra", "Reyes", "Delgado", "Cortés", "Peña",
            "León", "Chávez", "Arias", "Pineda", "Acosta", "Aguilar", "Molina", "Salazar", "Luna", "Campos",
            "Carrillo", "Fuentes", "Medina", "Zamora", "Rosales", "Quiroz", "Espinoza", "Núñez", "Cordero", "Montoya"
    };
    private static final String[] nacionalities = {
            "Costarricense", "Mexicano", "Colombiano", "Argentino", "Peruano",
            "Chileno", "Ecuatoriano", "Venezolano", "Español", "Guatemalteco",
            "Panameño", "Dominicano", "Uruguayo", "Salvadoreño", "Hondureño"
    };

    public static List<Passenger> getRandomNames(int count) {
        Random random = new Random();
        List<Passenger> passengersList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int id = 10  + i;

            String name = names[random.nextInt(names.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String nacionalidad = nacionalities[random.nextInt(nacionalities.length)];

            String fullName = name + " " + lastName;

            passengersList.add(new Passenger(id, fullName, nacionalidad));
        }

        return passengersList;
    }

    public static void writePassengersToJSON(String filepath, List<Passenger> newPassengers) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        List<Passenger> existingPassengers = readPassengersFromJSON(filepath);
        Map<Integer, Passenger> passengerMap = new HashMap<>();

        for (Passenger p : existingPassengers) {
            passengerMap.put(p.getId(), p);
        }

        for (Passenger p : newPassengers) {
            passengerMap.putIfAbsent(p.getId(), p);
        }

        List<Passenger> mergedList = new ArrayList<>(passengerMap.values());
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filepath), mergedList);
    }


    public static List<Passenger> readPassengersFromJSON(String filepath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(filepath), new com.fasterxml.jackson.core.type.TypeReference<List<Passenger>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


}
