package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Flight;
import model.Passenger;
import model.datamanagment.FlightManager;
import model.datamanagment.PassengerManager;
import model.tda.CircularDoublyLinkedList;
import model.tda.ListException;
import model.tda.SinglyLinkedList;

public class FlightHistoryController
{
    @javafx.fxml.FXML
    private TableColumn<Flight,Integer> codeColumn;
    @javafx.fxml.FXML
    private TableColumn<Flight,String> departureTimeCol;
    @javafx.fxml.FXML
    private TableColumn<Flight,String> destinyCol;
    @javafx.fxml.FXML
    private TableView<Flight> flightTableView;
    @javafx.fxml.FXML
    private TableColumn<Flight,String> originCol;
    @javafx.fxml.FXML
    private Label label;
    private PassengerManagerController passengerManagerController;
    private Passenger passenger;

    public void setPassengerManager(PassengerManagerController passengerManagerController) {
        this.passengerManagerController = passengerManagerController;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
        label.setText(label.getText()+passenger.getName());
         SinglyLinkedList history =passenger.getFlight_history();
        try {
            if (!history.isEmpty()){
                flightTableView.getItems().clear();
                for (int i=1;i<=history.size();i++){
                    Flight flight=(Flight)history.getNode(i).data;
                    flightTableView.getItems().add(flight);
                }
            }
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void initialize() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("flightID"));
        originCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrigin().getCity()));
        destinyCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDestination().getCity()));
        departureTimeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDepartureTime().toString()));
    }
}