package controller.user;

import controller.PassengerManagerController;
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
import model.Ticket;
import model.datamanagment.FlightManager;
import model.datamanagment.TicketManager;
import model.tda.ListException;
import model.tda.SinglyLinkedList;
import model.tda.StackException;
import util.Utility;

import java.util.ArrayList;
import java.util.List;

public class HistoryController
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
    private TextField tfFilter;private FilteredList<Flight> filteredFlights;
    private Passenger passenger;
    private TicketManager ticketManager;

    public void setPassenger(Passenger passenger) throws ListException {
        this.passenger = passenger;
        label.setText(label.getText()+passenger.getName());
        ticketManager = new TicketManager();
        List<Flight> history =new ArrayList<>();
        FlightManager flightManager = new FlightManager();
        for (Ticket t:ticketManager.getTickets()){
            if (t.getPassenger().equals(passenger)){
                Flight flight = flightManager.getFlight(t.getFlID());
                history.add(flight);
            }
        }
        try {
            if (!history.isEmpty()){
                int paisesVisitados = Utility.contarPaisesVisitados(history);
                labelCantVuelos.setText(labelCantVuelos.getText()+history.size());
                paisesVisitadosLbl.setText(paisesVisitadosLbl.getText()+paisesVisitados);
                initFilteredTV(history);
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