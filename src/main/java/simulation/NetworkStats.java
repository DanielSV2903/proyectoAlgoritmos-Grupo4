package simulation;

import model.Flight;
import model.Airport;
import model.tda.CircularDoublyLinkedList;
import model.tda.DoublyLinkedList;
import model.tda.ListException;

public class NetworkStats {
    private int totalFlights;
    private int totalPassengers;
    private double averageOccupancy;
    private int totalConnections;
    
    public void updateStats(CircularDoublyLinkedList flights, DoublyLinkedList airports) {
        try {
            this.totalFlights = flights.size();
            this.totalPassengers = calculateTotalPassengers(flights);
            this.averageOccupancy = calculateAverageOccupancy(flights);
            this.totalConnections = calculateTotalConnections(airports);
        } catch (ListException e) {
            e.printStackTrace();
        }
    }
    
    private int calculateTotalPassengers(CircularDoublyLinkedList flights) throws ListException {
        int total = 0;
        for (int i = 1; i <= flights.size(); i++) {
            Flight flight = (Flight) flights.getNode(i).data;
            total += flight.getOccupancy();
        }
        return total;
    }
    
    private double calculateAverageOccupancy(CircularDoublyLinkedList flights) throws ListException {
        if (flights.isEmpty()) return 0;
        double totalOccupancy = 0;
        for (int i = 1; i <= flights.size(); i++) {
            Flight flight = (Flight) flights.getNode(i).data;
            totalOccupancy += ((double) flight.getOccupancy() / flight.getCapacity()) * 100;
        }
        return totalOccupancy / flights.size();
    }
    
    private int calculateTotalConnections(DoublyLinkedList airports) throws ListException {
        return airports.size() * (airports.size() - 1) / 2;
    }
    
    // Getters para las estadísticas

    public int getTotalFlights() {
        return totalFlights;
    }

    public void setTotalFlights(int totalFlights) {
        this.totalFlights = totalFlights;
    }

    public int getTotalPassengers() {
        return totalPassengers;
    }

    public void setTotalPassengers(int totalPassengers) {
        this.totalPassengers = totalPassengers;
    }

    public double getAverageOccupancy() {
        return averageOccupancy;
    }

    public void setAverageOccupancy(double averageOccupancy) {
        this.averageOccupancy = averageOccupancy;
    }

    public int getTotalConnections() {
        return totalConnections;
    }

    public void setTotalConnections(int totalConnections) {
        this.totalConnections = totalConnections;
    }

    @Override
    public String toString() {
        return "NetworkStats{" +
                "totalFlights=" + totalFlights +
                ", totalPassengers=" + totalPassengers +
                ", averageOccupancy=" + averageOccupancy +
                ", totalConnections=" + totalConnections +
                '}';
    }
}