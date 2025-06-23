package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jdk.jshell.execution.Util;
import model.Airport;
import model.Route;
import model.RouteResult;
import model.datamanagment.AirportManager;
import model.datamanagment.DataCenter;
import model.datamanagment.RouteManager;
import model.tda.DoublyLinkedList;
import util.Utility;

import java.util.List;

public class CreateRouteController {
    @javafx.fxml.FXML
    private ComboBox<Airport> originCB;
    @javafx.fxml.FXML
    private TextField distanceTf;
    @javafx.fxml.FXML
    private ComboBox<Airport> destinyCB;

    private RoutesManagerController routesManagerController;
    private RouteManager routeManager; // asegurate de tenerlo inicializado
    private AirportManager airportManager;

    public void setRoutesController(RoutesManagerController routesManagerController){
        this.routesManagerController = routesManagerController;
    }

    @FXML
    public void initialize() {
        routeManager = new RouteManager();
        airportManager = new AirportManager();
        // Llenar ComboBox con IDs reales

        DoublyLinkedList airports = airportManager.getAirports();

        try {

            for (int i = 1; i <= airports.size(); i++) {
                Airport airport = (Airport) airports.getNode(i).data;

                originCB.getItems().add(airport);
                destinyCB.getItems().add(airport);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void createRouteOnAction(ActionEvent actionEvent) {
        try {
            Airport origin = originCB.getValue();
            Airport destination = destinyCB.getValue();

            // Validaciones básicas
            if (origin == null || destination == null || origin.equals(destination)) {
                System.out.println("Origen y destino deben ser distintos y válidos.");
                return;
            }

            int distance = Integer.parseInt(distanceTf.getText());

            // Obtener el path completo desde Dijkstra
            RouteResult routeResult = routeManager.getShortestRouteBetweenAirports(origin, destination);

            // Generar ID aleatorio para la nueva ruta
            int routeId = Utility.random(1000000, 9999999);
            Route newRoute;

            if (routeResult != null && !routeResult.getPath().isEmpty()) {
                newRoute = new Route(routeId, origin.getCode(), destination.getCode(), routeResult.getDistance());
                List<Object> path = routeResult.getPath();
                // Agregar solo las escalas intermedias
                for (int i = 1; i < path.size() - 1; i++) {
                    newRoute.addDestination(path.get(i).toString());
                }
            }

            routeManager.addRoute(origin.getCode(), destination.getCode(), distance); // método que guarda en JSON

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ruta creada");
            alert.setHeaderText(null);
            alert.setContentText("La ruta fue creada existosamente");
            alert.showAndWait();

            routesManagerController.updateTableView();
            ((Stage) distanceTf.getScene().getWindow()).close();
            DataCenter.enQueueOperation("Ruta agregada");
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
