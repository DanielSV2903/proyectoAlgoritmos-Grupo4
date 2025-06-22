package controller.flightradar;

import javafx.geometry.Point2D;
import java.util.List;

public class FlightRadar {
    private String flightId;
    private List<Point2D> path; // puntos de coordenadas por los que se moverá

    public FlightRadar(String flightId, List<Point2D> path) {
        this.flightId = flightId;
        this.path = path;
    }

    public String getFlightId() { return flightId; }
    public List<Point2D> getPath() { return path; }
}