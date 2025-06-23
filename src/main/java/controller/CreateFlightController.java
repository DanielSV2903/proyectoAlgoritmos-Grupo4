package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Airport;
import model.Flight;
import model.datamanagment.AirportManager;
import model.datamanagment.DataCenter;
import model.datamanagment.FlightManager;
import model.tda.DoublyLinkedList;
import model.tda.ListException;
import model.tda.SinglyLinkedList;
import util.Utility;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

public class CreateFlightController
{
    @javafx.fxml.FXML
    private TextField tfCapacity;
    @javafx.fxml.FXML
    private ComboBox<Airport> originCB;
    @javafx.fxml.FXML
    private DatePicker  datepicker;
    @javafx.fxml.FXML
    private ComboBox<Airport> destinyCB;
    @javafx.fxml.FXML
    private BorderPane bp;
    private FlightManagerController flightManagerController;
    @javafx.fxml.FXML
    private ComboBox<LocalTime> timeCB;

    AirportManager airportManager;

    public void setFlightManagerController(FlightManagerController controller) {
        this.flightManagerController = controller;
    }

    private void clearFields() {
        this.timeCB.getSelectionModel().clearSelection();
        this.originCB.getSelectionModel().clearSelection();
        this.datepicker.getEditor().clear();
        this.tfCapacity.clear();
        this.destinyCB.getSelectionModel().clearSelection();
    }

    @javafx.fxml.FXML
    public void initialize() {
        airportManager = new AirportManager();
        timeCB.getItems().addAll(Utility.getDepartureHours());
        DoublyLinkedList airports = airportManager.getAirports();
        try {
            for (int i = 1; i <= airports.size(); i++) {
                Airport airport= (Airport) airports.getNode(i).data;
                originCB.getItems().add(airport);
                destinyCB.getItems().add(airport);
            }
        }catch (ListException e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void cancelOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    @javafx.fxml.FXML
    public void exitOnAction(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void createFlightOnAction(ActionEvent actionEvent) {
        try {
            Alert alert =new Alert(Alert.AlertType.INFORMATION);
            if (validarEntradas()){
            Airport origin = originCB.getSelectionModel().getSelectedItem();
            Airport destiny = destinyCB.getSelectionModel().getSelectedItem();
            int capacity = Integer.parseInt(tfCapacity.getText());
            LocalTime time = timeCB.getSelectionModel().getSelectedItem();
            LocalDateTime departureTine = datepicker.getValue().atTime(time);
            int id=1;
            if (!flightManagerController.getFlightList().isEmpty()){
                Flight aux= (Flight) flightManagerController.getFlightList().getLast();
                id = aux.getFlightID()+1;//TODO
                 }
                Flight flight = new Flight(id, origin, destiny, departureTine, capacity,0);
            flightManagerController.addFlight(flight);
                alert.setContentText("El vuelo fue programado existosamente");
                DataCenter.enQueueOperation("Vuelo programado");
                alert.showAndWait();
            flightManagerController.updateTV();
            clearFields();
            ((Stage) originCB.getScene().getWindow()).close();
            }else if (!this.originCB.getSelectionModel().getSelectedItem().getStatus()||!destinyCB.getSelectionModel().getSelectedItem().getStatus()){
                alert =new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se puede crear el flight\n uno de los aeropuertos esta inactivo");
                alert.showAndWait();
            }else if (this.destinyCB.getSelectionModel().getSelectedItem().getCode()==originCB.getSelectionModel().getSelectedItem().getCode()){
                alert =new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se puede crear el flight\n El origen y destino son iguales");
                alert.showAndWait();

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean validarEntradas() {
        return this.timeCB.getSelectionModel().getSelectedItem()!=null && this.originCB.getSelectionModel().getSelectedItem()!=null
                && this.destinyCB.getSelectionModel().getSelectedItem()!=null
                && !this.tfCapacity.getText().isEmpty()&& this.datepicker.getValue()!=null&&destinyCB.getSelectionModel().getSelectedItem()!=originCB.getSelectionModel().getSelectedItem()
                &&this.originCB.getSelectionModel().getSelectedItem().getStatus()&&destinyCB.getSelectionModel().getSelectedItem().getStatus();
    }
}