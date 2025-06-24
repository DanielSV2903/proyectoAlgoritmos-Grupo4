package model;

import java.util.List;

public class RouteResult {

    //Clase utilizada para cargar las rutas de Dijkstra

    private int distance;
    private List<Object> path;

    public RouteResult(int distance, List<Object> path) {
        this.distance = distance;
        this.path = path;
    }

    public int getDistance() {
        return distance;
    }

    public List<Object> getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "Distance: " + distance + ", Path: " + path;
    }
}
