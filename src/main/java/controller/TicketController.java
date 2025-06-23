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
public void setData(Passenger passenger, Flight flight) {
        this.passenger = passenger;
        this.flight = flight;
    this.nationalityID.setText(passenger.getNationality());
    this.nameLBL.setText(this.passenger.getName());

    this.seatLBL.setText(Utility.getRandomSeat(passenger.getPriority(),flight));
    this.departureLBL.setText(Utility.formatedDate(flight.getDepartureTime()));
    this.idLBL.setText(String.valueOf(passenger.getId()));
    this.routeLBL.setText(flight.getOrigin().getCity()+"-"+flight.getDestination().getCity());
    this.gateLBL.setText(String.valueOf(Utility.random(6)));
}
}