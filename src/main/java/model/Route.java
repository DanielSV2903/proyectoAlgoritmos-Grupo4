package model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import model.serializers.SinglyLinkedListDeserializer;
import model.serializers.SinglyLinkedListSerializer;
import model.tda.SinglyLinkedList;

public class Route {
    /*
    Route (origin_airport(int), destination_list(SinglyLinkedList))
     */
    private  int route_id;
    private String origin_airport_id;
    private String destination_airport_id;
    private int distance;
    @JsonSerialize(using = SinglyLinkedListSerializer.class)
    @JsonDeserialize(using = SinglyLinkedListDeserializer.class)
    private SinglyLinkedList destination_list;

    public Route(int id,String origin_airport_id, String destination_airport_id, int distance) {
        this.route_id = id;
        this.origin_airport_id = origin_airport_id;
        this.destination_airport_id = destination_airport_id;
        this.distance = distance;
        this.destination_list = new SinglyLinkedList();
    }

    public Route() {
    }

    public String getOrigin_airport_id() {
        return origin_airport_id;
    }

    public void setOrigin_airport_id(String origin_airport_id) {
        this.origin_airport_id = origin_airport_id;
    }

    public String getDestination_airport_id() {
        return destination_airport_id;
    }

    public void setDestination_airport_id(String destination_airport_id) {
        this.destination_airport_id = destination_airport_id;
    }

    public SinglyLinkedList getDestination_list() {
        return destination_list;
    }

    public void setDestination_list(SinglyLinkedList destination_list) {
        this.destination_list = destination_list;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void addDestination(String destinationAirpotId) {
        destination_list.add(destinationAirpotId);
    }

    @Override
    public String toString() {
        return "Route{" +
                "route_id=" + route_id +
                ", origin_airport_id='" + origin_airport_id + '\'' +
                ", destination_airport_id='" + destination_airport_id + '\'' +
                ", distance=" + distance +
                ", destination_list=" + destination_list +
                '}';
    }
}
