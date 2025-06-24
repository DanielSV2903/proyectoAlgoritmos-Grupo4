package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Flight;
import model.Passenger;
import model.tda.ListException;
import model.tda.SinglyLinkedList;
import model.tda.StackException;
import util.Utility;

import java.util.ArrayList;
import java.util.List;

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
    @javafx.fxml.FXML
    private Label labelCantVuelos;
    @javafx.fxml.FXML
    private Label paisesVisitadosLbl;
    @javafx.fxml.FXML
    private TextField tfFilter;
    private FilteredList<Flight> filteredFlights;
    private PassengerManagerController passengerManagerController;
    private Passenger passenger;

    public void setPassengerManager(PassengerManagerController passengerManagerController) {
        this.passengerManagerController = passengerManagerController;
    }

    public void setPassenger(Passenger passenger) throws ListException {
        this.passenger = passenger;
        label.setText(label.getText()+passenger.getName());
         SinglyLinkedList history =passenger.getFlight_history();
         List<Flight> flightList=new ArrayList<>();
        try {
            if (!history.isEmpty()){
                for (int i=1;i<=history.size();i++){
                    Flight flight= (Flight) history.getNode(i).data;
                    flightList.add(flight);
                }
                int paisesVisitados = Utility.contarPaisesVisitados(history);
                labelCantVuelos.setText(labelCantVuelos.getText()+history.size());
                paisesVisitadosLbl.setText(paisesVisitadosLbl.getText()+paisesVisitados);
                initFilteredTV(flightList);
            }
        } catch (ListException | StackException e) {
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
    private void initFilteredTV(List<Flight> flightList) {
        filteredFlights = new FilteredList<>(
                FXCollections.observableArrayList(flightList),
                p -> true
        );
        flightTableView.setItems(filteredFlights);

        tfFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredFlights.setPredicate(flight -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return String.valueOf(flight.getFlightID()).contains(newValue);
            });
        });
    }
}