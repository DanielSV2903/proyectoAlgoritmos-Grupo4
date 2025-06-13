package model.datamanagment;

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
            rm.addRoute("1","2",14);
        } catch (ListException e) {
            throw new RuntimeException(e);
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
    }
}