package ucr.lab.proyectoayed.model;

import ucr.lab.proyectoayed.model.tda.SinglyLinkedList;

public class AirportManager {
    private final SinglyLinkedList airports = new SinglyLinkedList();
    private final String filePath = "src/main/java/ucr/lab/proyectoayed/data/airports.json";

    public void loadAirports() {

    }

    public void addAirports() {

    }

    public void saveAirports(Airport airport) {
        airports.add(airport);
    }

    public SinglyLinkedList getAirports() {
        return airports;
    }

}
