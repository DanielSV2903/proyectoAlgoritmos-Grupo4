package model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import model.serializers.SinglyLinkedListDeserializer;
import model.serializers.SinglyLinkedListSerializer;
import model.tda.SinglyLinkedList;

import java.util.Objects;

public class Airport {
    private String name;
    private String code;
    private String city;
    private String country;
    private boolean status;
    private double mapX;
    private double mapY;
    @JsonSerialize(using = SinglyLinkedListSerializer.class)
    @JsonDeserialize(using = SinglyLinkedListDeserializer.class)
    private SinglyLinkedList departures_board;

    public Airport(String code,String name, String city, String country) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.country = country;
        this.status = true;
        this.departures_board = new SinglyLinkedList();
    }
    public Airport(String code,String name, String city, String country, boolean status) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.country = country;
        this.status = status;
        this.departures_board = new SinglyLinkedList();
    }

    public Airport() {
    }

    public Airport(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getMapX() { return mapX; }

    public void setMapX(double mapX) { this.mapX = mapX; }

    public double getMapY() { return mapY; }

    public void setMapY(double mapY) { this.mapY = mapY; }


    public SinglyLinkedList getDepartures_board() {
        return departures_board;
    }
    public void addDepartureToBoard(String departure) {
        if (departures_board==null) {
            departures_board = new SinglyLinkedList();
        }
        departures_board.add(departure);
    }

    public void setDepartures_board(SinglyLinkedList departures_board) {
        this.departures_board = departures_board;
    }

    @Override
    public String toString() {
        String status="Inactive";
        if(this.status)
            status="Active";
        return "Airport{"+code+','+name+','+city+','+country+','+status+'}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airport airport)) return false;
        return Objects.equals(code, airport.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
