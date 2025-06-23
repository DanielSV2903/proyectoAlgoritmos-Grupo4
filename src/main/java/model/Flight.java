package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import model.serializers.PassengerSinglyLinkedListDeserializer;
import model.serializers.SinglyLinkedListDeserializer;
import model.serializers.SinglyLinkedListSerializer;
import model.tda.ListException;
import model.tda.SinglyLinkedList;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flight {

    private int flightID;
    private Airport origin;
    private Airport destination;
    private LocalDateTime departureTime;
    private int capacity;
    private int occupancy;

    @JsonSerialize(using = SinglyLinkedListSerializer.class)
    @JsonDeserialize(using = PassengerSinglyLinkedListDeserializer.class)
    private SinglyLinkedList passengers;

    public Flight(int flightID, Airport origin, Airport destination, LocalDateTime departureTime, int capacity, int occupancy) {
        this.flightID = flightID;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.occupancy = occupancy;
        this.passengers = new SinglyLinkedList();
    }
    public Flight(int flightID, Airport origin, Airport destination, LocalDateTime departureTime, int capacity) throws ListException {
        this.flightID = flightID;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.passengers = new SinglyLinkedList();
        if (passengers.isEmpty())
            this.occupancy = 0;
        else
            this.occupancy = passengers.size();
    }

    public Flight() {
    }

    public Flight(int flightID) {
        this.flightID = flightID;
    }

    public Flight(Airport origin, Airport destination, LocalDateTime departureTime) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
    }

    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public Airport getOrigin() {
        return origin;
    }

    public void setOrigin(Airport origin) {
        this.origin = origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public void setDestination(Airport destination) {
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

    public boolean addPassenger(Passenger passenger) throws ListException {
        if (!hasAvailableSeats()) return false;
        if (!passengers.isEmpty() && passengers.contains(passenger)) return false;

        passengers.add(passenger);
        occupancy++;
        return true;
    }



    @Override
    public String toString() {
        return "Flight{" +
                "flightID=" + flightID +
                ", origin=" + origin +
                ", destination=" + destination +
                ", departureTime=" + departureTime +
                ", capacity=" + capacity +
                ", occupancy=" + occupancy +
                ", passengers=" + passengers +
                '}';
    }

    public boolean hasAvailableSeats() {
        return occupancy < capacity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Flight other = (Flight) obj;
        return Objects.equals(this.flightID, other.flightID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightID);
    }
}
