package model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import model.serializers.FlightSinglyLinkedListDeserializer;
import model.serializers.SinglyLinkedListDeserializer;
import model.serializers.SinglyLinkedListSerializer;
import model.tda.ListException;
import model.tda.SinglyLinkedList;

import java.util.Objects;

public class Passenger {
    private int id;
    private String name;
    private String nationality;
    @JsonSerialize(using = SinglyLinkedListSerializer.class)
    @JsonDeserialize(using = FlightSinglyLinkedListDeserializer.class)
    private SinglyLinkedList flight_history;

    public Passenger(int id, String name, String nationality) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.flight_history = new SinglyLinkedList();
    }

    public Passenger(int id) {
        this.id = id;
        this.flight_history = new SinglyLinkedList();
    }

    public Passenger() {
        this.flight_history = new SinglyLinkedList();
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public SinglyLinkedList getFlight_history() {
        return flight_history;
    }

    public void setFlight_history(SinglyLinkedList flight_history) {
        this.flight_history = flight_history;
    }
    public void addFlight_ToHistory(Flight flight) throws ListException {
        if (this.flight_history.isEmpty())
            this.flight_history.add(flight);
        if (!this.flight_history.contains(flight)) {
            this.flight_history.add(flight);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passenger passenger)) return false;
        return id == passenger.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

