package model;

import model.tda.LinkedStack;

public class Plane {
    private String planeName;
    private String id;
    private LinkedStack flightHistory;

    public Plane(String planeName, String id, LinkedStack flightHistory) {
        this.planeName = planeName;
        this.id = id;
        this.flightHistory = flightHistory;
    }

    public Plane() {
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedStack getFlightHistory() {
        return flightHistory;
    }

    public void setFlightHistory(LinkedStack flightHistory) {
        this.flightHistory = flightHistory;
    }
}


