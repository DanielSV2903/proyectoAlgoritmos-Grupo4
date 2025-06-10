package model;

import model.tda.SinglyLinkedList;

import java.time.LocalDateTime;

public class Flight {

    private int flightID;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private int capacity;
    private int occupancy;
    private SinglyLinkedList passengers;

    public Flight(int flightID, String origin, String destination, LocalDateTime departureTime, int capacity, int occupancy) {
        this.flightID = flightID;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.occupancy = occupancy;
        this.passengers = new SinglyLinkedList();
    }

    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public SinglyLinkedList getPassengers() {
        return passengers;
    }

    public void setPassengers(SinglyLinkedList passengers) {
        this.passengers = passengers;
    }
}
