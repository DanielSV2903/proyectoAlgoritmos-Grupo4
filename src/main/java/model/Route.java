package model;

import model.tda.SinglyLinkedList;

public class Route {
    /*
    Route (origin_airport(int), destination_list(SinglyLinkedList))
     */
    private int origin_airport_id;
    private int destination_airport_id;
    private SinglyLinkedList destination_list;

    public Route(int origin_airport_id, int destination_airport_id) {
        this.origin_airport_id = origin_airport_id;
        this.destination_airport_id = destination_airport_id;
        this.destination_list = new SinglyLinkedList();
    }

    public Route(int origin_airport_id) {
        this.origin_airport_id = origin_airport_id;
        this.destination_list = new SinglyLinkedList();
    }

    public int getOrigin_airport_id() {
        return origin_airport_id;
    }

    public void setOrigin_airport_id(int origin_airport_id) {
        this.origin_airport_id = origin_airport_id;
    }

    public int getDestination_airport_id() {
        return destination_airport_id;
    }

    public void setDestination_airport_id(int destination_airport_id) {
        this.destination_airport_id = destination_airport_id;
    }

    public SinglyLinkedList getDestination_list() {
        return destination_list;
    }

    public void setDestination_list(SinglyLinkedList destination_list) {
        this.destination_list = destination_list;
    }
}
