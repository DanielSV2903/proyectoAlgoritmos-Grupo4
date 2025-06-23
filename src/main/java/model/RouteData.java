package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RouteData {
    private final String filePath = "src/main/java/data/routes.json";

    public static List<Route> generateRandomRoutes(int count, List<Airport> airports) {
        List<Route> routeList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int id = 100 + i;
            String origin = airports.get(random.nextInt(airports.size())).getCode();
            String destination = airports.get(random.nextInt(airports.size())).getCode();
            int distance = 500 + random.nextInt(3500);

            Route route = new Route(id, origin, destination, distance);

            int hops = 1 + random.nextInt(3);
            for (int j = 0; j < hops; j++) {
                String stop = airports.get(random.nextInt(airports.size())).getCode();
                route.addDestination(stop);
            }

            routeList.add(route);
        }

        return routeList;
    }

    public static void writeRoutesToJSON(String filepath, List<Route> newRoutes) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        List<Route> existingRoutes = readRoutesFromJSON(filepath);
        Map<Integer, Route> routeMap = new HashMap<>();

        for (Route r : existingRoutes) {
            routeMap.put(r.getRoute_id(), r);
        }
        for (Route r : newRoutes) {
            routeMap.putIfAbsent(r.getRoute_id(), r);
        }

        List<Route> mergedList = new ArrayList<>(routeMap.values());
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filepath), mergedList);
    }

    public static List<Route> readRoutesFromJSON(String filepath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(new File(filepath), new com.fasterxml.jackson.core.type.TypeReference<List<Route>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<Airport> readAirportsFromJSON(String filepath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(filepath), new com.fasterxml.jackson.core.type.TypeReference<List<Airport>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
