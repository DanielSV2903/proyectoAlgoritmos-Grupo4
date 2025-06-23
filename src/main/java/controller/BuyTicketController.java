package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.Airport;
import model.datamanagment.DataCenter;
import model.tda.DoublyLinkedList;
import model.tda.ListException;
import util.Utility;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class BuyTicketController
{
    @javafx.fxml.FXML
    private ComboBox<Airport> cbDestiny;
    @javafx.fxml.FXML
    private ComboBox<LocalTime> hourCB;
    @javafx.fxml.FXML
    private ComboBox<Airport> cbOrigin;
    @javafx.fxml.FXML
    private DatePicker dateDP;
    @javafx.fxml.FXML
    private TextField tfCantidad;
    @javafx.fxml.FXML
    private BorderPane bp;
    private Alert alert;

    @javafx.fxml.FXML
    public void initialize() throws ListException {
        hourCB.getItems().addAll(Utility.getDepartureHours());
        DoublyLinkedList airports= DataCenter.getAirports();

        for (int i=0;i<airports.size();i++){
            Airport airport= (Airport) airports.getNode(i).data;
            cbDestiny.getItems().add(airport);
            cbOrigin.getItems().add(airport);
        }
    }

    @javafx.fxml.FXML
    public void cancelOnAction(ActionEvent actionEvent) {
        tfCantidad.clear();
        dateDP.getEditor().clear();
        cbOrigin.getEditor().clear();
        cbDestiny.getEditor().clear();
        hourCB.getEditor().clear();
    }

    @javafx.fxml.FXML
    public void confirmOnAction(ActionEvent actionEvent) {
        int cantidad = Integer.parseInt(tfCantidad.getText());
        Airport destiny = cbDestiny.getSelectionModel().getSelectedItem();
        Airport origin = cbOrigin.getSelectionModel().getSelectedItem();
        LocalTime time = hourCB.getSelectionModel().getSelectedItem();
        LocalDateTime localDateTime= LocalDateTime.of(dateDP.getValue(), time);
        if(validarVueloexistente(origin,destiny)){
            if (camposDisponibles()){
                alert.setTitle("Compra de ticketes");
                showAlert(Alert.AlertType.INFORMATION,"Tiquete comprado con exito");
            }else {
                alert.setTitle("Compra de ticketes");
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setContentText("El vuelo para la fecha "+localDateTime+ " esta lleno desea un tickete para el proximo a las "+proximoDispo());
            }
        }

    }

    private String proximoDispo() {
        return "";
    }

    private boolean camposDisponibles() {
        return false;
    }

    private boolean validarVueloexistente(Airport origin, Airport destiny) {
        return false;
    }
    private void showAlert(Alert.AlertType type, String msg){
        alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}