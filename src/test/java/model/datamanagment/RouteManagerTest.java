package model.datamanagment;

import model.Airport;
import model.Route;
import model.tda.ListException;
import model.tda.graph.GraphException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class RouteManagerTest {


    @Test
    void addRoute() {
        RouteManager rm = new RouteManager();
        try {
           rm.addRoute("1","201",15000);
           rm.addRoute("2","201",20000);
           rm.addRoute("JFK","CAI",60000);
        } catch (ListException | GraphException e) {
            throw new RuntimeException(e);
        }
    }
}