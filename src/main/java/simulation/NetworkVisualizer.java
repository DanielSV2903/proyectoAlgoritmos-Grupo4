package simulation;


import model.Airport;
import model.Flight;
import model.tda.CircularDoublyLinkedList;
import model.tda.DoublyLinkedList;
import model.tda.ListException;

public class NetworkVisualizer {
    public String generateNetworkMap(DoublyLinkedList airports, CircularDoublyLinkedList flights)
            throws ListException {
        StringBuilder map = new StringBuilder();
        map.append("=== Red Aérea ===\n\n");

        // Mostrar aeropuertos
        map.append("Aeropuertos:\n");
        for (int i = 1; i <= airports.size(); i++) {
            Airport airport = (Airport) airports.getNode(i).data;
            map.append(String.format("* %s (%s) - %s, %s\n",
                    airport.getName(),
                    airport.getCode(),
                    airport.getCity(),
                    airport.getCountry()));
        }

        // Mostrar conexiones
        map.append("\nConexiones:\n");
        for (int i = 1; i <= flights.size(); i++) {
            Flight flight = (Flight) flights.getNode(i).data;
            map.append(String.format("* Vuelo %d: %s (%s) -> %s (%s) | %s | %d/%d pasajeros\n",
                    flight.getFlightID(),
                    flight.getOrigin().getCity(),
                    flight.getOrigin().getCode(),
                    flight.getDestination().getCity(),
                    flight.getDestination().getCode(),
                    flight.getDepartureTime(),
                    flight.getOccupancy(),
                    flight.getCapacity()));
        }

        return map.toString();
    }
}
