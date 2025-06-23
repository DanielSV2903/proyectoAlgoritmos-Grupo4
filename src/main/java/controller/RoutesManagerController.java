package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import model.Route;
import model.datamanagment.RouteManager;
import model.tda.SinglyLinkedList;
import model.tda.ListException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RoutesManagerController {
    @javafx.fxml.FXML
    private TableView<Route> routeTableView;
    @javafx.fxml.FXML
    private TableColumn<Route, String> idOriginCol;
    @javafx.fxml.FXML
    private TableColumn<Route, String> distanceCol;
    @javafx.fxml.FXML
    private TableColumn<Route, String> idRouteColumn;
    @javafx.fxml.FXML
    private TableColumn<Route, String> idDestinyCol;
    @javafx.fxml.FXML
    private TableColumn<Route, String> layoversCol;

    private RouteManager routeManager;

    @FXML
    public void initialize() {
        routeManager = new RouteManager();
        updateTableView();
    }

    private String formatStops(SinglyLinkedList list) {
        StringBuilder sb = new StringBuilder();
        try {
            if (list.size() == 1) sb.append("No presenta escalas");
            else {
                for (int i = 1; i <= list.size(); i++) {
                    sb.append(list.getNode(i));
                    if (i < list.size()) sb.append(" → ");
                }
            }
        } catch (ListException e) {
            return "Error";
        }
        return sb.toString();
    }

    @javafx.fxml.FXML
    public void simulationOnAction(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void removeRouteOnAction(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void createRouteOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cretaairlines/createRoute.fxml"));
            Parent root = loader.load();
            CreateRouteController createRouteController = loader.getController();
            createRouteController.setRoutesController(this);
            Stage stage = new Stage(); // Nueva ventana
            stage.setTitle("Visualización de rutas");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateRouteOnAction(ActionEvent actionEvent) {
        Route selectedRoute = (Route) routeTableView.getSelectionModel().getSelectedItem();
        if (selectedRoute == null) {
            showAlert(Alert.AlertType.WARNING, "Debe seleccionar una ruta primero.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedRoute.getDistance()));
        dialog.setTitle("Actualizar Distancia");
        dialog.setHeaderText("Actualizar distancia de la ruta");
        dialog.setContentText("Nueva distancia:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int newDistance = Integer.parseInt(input);
                routeManager.updateRouteDistance(selectedRoute.getRoute_id(), newDistance);
                updateTableView();
                showAlert(Alert.AlertType.INFORMATION, "Distancia actualizada correctamente.");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "La distancia debe ser un número válido.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error al actualizar: " + e.getMessage());
            }
        });
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Mensaje del sistema");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void updateTableView(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File("src/main/java/data/routes.json");
            List<Route> routes = mapper.readValue(file, new TypeReference<List<Route>>() {});
            ObservableList<Route> routeData = FXCollections.observableArrayList(routes);

            // Configurar columnas
            idRouteColumn.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getRoute_id()).asObject().asString());

            idOriginCol.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getOrigin_airport_id()));

            idDestinyCol.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getDestination_airport_id()));

            distanceCol.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getDistance()).asObject().asString());

            layoversCol.setCellValueFactory(cellData ->
                    new SimpleStringProperty(formatStops(cellData.getValue().getDestination_list())));

            // Poner datos en la tabla
            routeTableView.setItems(routeData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
