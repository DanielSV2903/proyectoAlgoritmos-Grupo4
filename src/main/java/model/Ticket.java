package model;

import java.time.LocalDateTime;

public class Ticket {
    private int id;
    private Airport origin;
    private Airport destination;
    private int numSeats;
    private Passenger passenger;
    private LocalDateTime departureTime;
    private int flID;
    public Ticket() {}

    public Ticket(Airport origin, Airport destination, Passenger passenger,LocalDateTime departureTime,int numSeats,int flID) {
        this.origin = origin;
        this.destination = destination;
        this.numSeats = numSeats;
        this.passenger = passenger;
        this.departureTime = departureTime;
        this.flID = flID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getFlID() {
        return flID;
    }

    public void setFlID(int flID) {
        this.flID = flID;
    }
}
