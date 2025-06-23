package controller;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import model.Flight;
import model.Passenger;
import model.Route;
import model.datamanagment.DataCenter;
import util.Utility;

public class TicketController
{
    @javafx.fxml.FXML
    private Label seatLBL;
    @javafx.fxml.FXML
    private Label departureLBL;
    @javafx.fxml.FXML
    private Label nationalityID;
    @javafx.fxml.FXML
    private Label nameLBL;
    @javafx.fxml.FXML
    private Label idLBL;
    @javafx.fxml.FXML
    private Label routeLBL;
    @javafx.fxml.FXML
    private Label gateLBL;
    private Passenger passenger;
    private Route route;
    private Flight flight;
    @javafx.fxml.FXML
    public void initialize() {
        DataCenter.enQueueOperation("Ticket comprado");
    }
public void setData(Passenger passenger, Route route, Flight flight) {
        this.passenger = passenger;
        this.route = route;
        this.flight = flight;
    this.nationalityID.setText(passenger.getNationality());
    this.nameLBL.setText(this.passenger.getName());
    String priority="";
    this.seatLBL.setText(Utility.getRandomSeat(priority,flight));
    this.departureLBL.setText(flight.getDepartureTime().toString());
    this.idLBL.setText(String.valueOf(passenger.getId()));
    this.routeLBL.setText(route.toString());
    this.gateLBL.setText(String.valueOf(Utility.random(6)));
}
}