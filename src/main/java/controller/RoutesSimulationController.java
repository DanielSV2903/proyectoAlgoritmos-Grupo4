package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import model.Airport;
import model.RouteResult;
import model.datamanagment.AirportManager;
import model.datamanagment.RouteManager;
import model.tda.DoublyLinkedList;
import model.tda.ListException;

import java.util.List;

public class RoutesSimulationController {

    @javafx.fxml.FXML
    private ComboBox<Airport> destinationComboBox;
    @javafx.fxml.FXML
    private ComboBox<Airport> originComboBox;
    @javafx.fxml.FXML
    private Pane routePane;

    private Image airportIcon;
    private RouteManager routeManager;
    private AirportManager airportManager;

    @FXML
    public void initialize() {
        airportManager = new AirportManager();
        // Cargar ícono desde resources
        airportIcon = new Image(getClass().getResourceAsStream("/com/cretaairlines/img/airport.png"));

        // Instanciar el gestor de rutas
        routeManager = new RouteManager();

        DoublyLinkedList airports = airportManager.getAirports();
        // Llenar los ComboBox con IDs de aeropuertos
        try {
            for (int i = 1; i <= airports.size(); i++) {
                Airport airport = (Airport) airports.getNode(i).data;
                originComboBox.getItems().add(airport);
                destinationComboBox.getItems().add(airport);
            }
        } catch (ListException e) {
            System.out.println("Error al cargar aeropuertos: " + e.getMessage());
        }
    }


    @javafx.fxml.FXML
    public void searchRouteOnAction(ActionEvent actionEvent) {
        routePane.getChildren().clear();

        Airport origin = originComboBox.getValue();
        Airport destination = destinationComboBox.getValue();

        if (origin == null || destination == null) {
            System.out.println("Debe seleccionar origen y destino.");
            return;
        }

        try {
            RouteResult result = routeManager.getShortestRouteBetweenAirports(origin, destination);
            List<Object> path = result.getPath();

            double xStart = 50, y = 100, spacing = 150;

            for (int i = 0; i < path.size(); i++) {
                Object id = path.get(i);

                ImageView icon = new ImageView(airportIcon);
                icon.setFitWidth(40);
                icon.setFitHeight(40);
                icon.setLayoutX(xStart + i * spacing);
                icon.setLayoutY(y);

                Label label = new Label(id.toString());
                label.setLayoutX(xStart + i * spacing);
                label.setLayoutY(y + 45);

                routePane.getChildren().addAll(icon, label);

                if (i < path.size() - 1) {
                    Line line = new Line();
                    line.setStartX(xStart + i * spacing + 40);
                    line.setStartY(y + 20);
                    line.setEndX(xStart + (i + 1) * spacing);
                    line.setEndY(y + 20);
                    routePane.getChildren().add(line);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar la ruta: " + e.getMessage());
        }
    }
}
