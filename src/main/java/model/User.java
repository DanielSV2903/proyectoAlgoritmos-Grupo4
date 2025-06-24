package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String role;
    private Passenger passenger;
    private List<Integer> tickets;

    public User(int id, String name, String password, String email, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.passenger = new Passenger();
        this.tickets = new ArrayList<>();
    }
    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = "USER";
        this.passenger = new Passenger();
        this.tickets = new ArrayList<>();
    }


    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public List<Integer> getTickets() {
        return tickets;
    }
    public void addTicket(int ticketID) {
        this.tickets.add(ticketID);
    }

    public void setTickets(List<Integer> tickets) {
        this.tickets = tickets;
    }
}
