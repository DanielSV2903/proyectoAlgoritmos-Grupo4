package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Route;
import model.datamanagment.DataCenter;
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
        DataCenter.enQueueOperation("Gestionando rutas");
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

    //Generación de escalas
    private String formatStops(SinglyLinkedList list) {
        StringBuilder sb = new StringBuilder();
        try {
            //En caso de ser ruta directa, no muestra escalas
            if (list.size() == 1) sb.append("No presenta escalas");
            else {
                for (int i = 1; i <= list.size(); i++) {
                    sb.append(list.getNode(i).data);
                    if (i < list.size()) sb.append(" → ");
                }
            }
        } catch (ListException e) {
            return "Error";
        }
        return sb.toString();
    }

    @javafx.fxml.FXML
    public void createRouteOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cretaairlines/createRoute.fxml"));
            Parent root = loader.load();
            CreateRouteController createRouteController = loader.getController();
            createRouteController.setRoutesController(this);
            // Crear un nuevo Stage
            Stage modalStage = new Stage();
            modalStage.setTitle("Visualización de rutas");
            modalStage.setScene(new Scene(root));

            // 1. Bloquea toda la aplicación
            modalStage.initModality(Modality.APPLICATION_MODAL);

            // 2. Establece la ventana principal como dueña (importantísimo)
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            modalStage.initOwner(mainStage);

            // 3. Muestra la ventana y bloquea hasta que se cierre
            modalStage.showAndWait();
            DataCenter.enQueueOperation("Ruta creada");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateRouteOnAction(ActionEvent actionEvent) {

        //Se selecciona un item del table view
        Route selectedRoute = (Route) routeTableView.getSelectionModel().getSelectedItem();
        if (selectedRoute == null) {
            showAlert(Alert.AlertType.WARNING, "Debe seleccionar una ruta primero.");
            return;
        }


        //Se genera un cuadro de texto para modificar la distancia del objeto
        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedRoute.getDistance()));
        dialog.setTitle("Actualizar Distancia");
        dialog.setHeaderText("Actualizar distancia de la ruta");
        dialog.setContentText("Nueva distancia: ");

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
