package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import jdk.jshell.execution.Util;
import model.Route;
import model.RouteResult;
import model.datamanagment.RouteManager;
import util.Utility;

import java.util.List;

public class CreateRouteController {
    @javafx.fxml.FXML
    private ComboBox<String> originCB;
    @javafx.fxml.FXML
    private TextField distanceTf;
    @javafx.fxml.FXML
    private ComboBox<String> destinyCB;

    private RouteManager routeManager; // asegurate de tenerlo inicializado

    @FXML
    public void initialize() {
        routeManager = new RouteManager();
        // Llenar ComboBox con IDs reales
        try {
            List<String> ids = routeManager.getAirportIds(); // o desde AirportManager
            originCB.getItems().add(String.valueOf(ids));
            destinyCB.getItems().add(String.valueOf(ids));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void createRouteOnAction(ActionEvent actionEvent) {
        try {
            String origin = originCB.getValue();
            String destination = destinyCB.getValue();

            // Validaciones básicas
            if (origin == null || destination == null || origin.equals(destination)) {
                System.out.println("Origen y destino deben ser distintos y válidos.");
                return;
            }

            RouteResult result = routeManager.getShortestRouteBetweenAirports(origin, destination);
            int distance = result.getDistance();

            // Obtener el path completo desde Dijkstra
            RouteResult routeResult = routeManager.getShortestRouteBetweenAirports(origin, destination);

            if (routeResult == null || routeResult.getPath().isEmpty()) {
                System.out.println("No hay ruta disponible.");
                return;
            }

            // Generar ID aleatorio para la nueva ruta
            int routeId = Utility.random(1000000, 9999999);

            Route newRoute = new Route(routeId, origin, destination, routeResult.getDistance());

            List<Object> path = routeResult.getPath();

            // Agregar solo las escalas intermedias
            for (int i = 1; i < path.size() - 1; i++) {
                newRoute.addDestination(path.get(i).toString());
            }

            routeManager.addRoute(origin, destination, routeResult.getDistance()); // método que guarda en JSON

            System.out.println("Ruta agregada automáticamente con escalas.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al agregar ruta: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void cancelOnAction(ActionEvent actionEvent) {
    }
}
