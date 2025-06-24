package controller;

import com.cretaairlines.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import model.datamanagment.DataCenter;
import model.datamanagment.FlightManager;
import model.datamanagment.TicketManager;
import model.tda.CircularDoublyLinkedList;
import model.tda.DoublyLinkedList;
import model.tda.ListException;
import util.Utility;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class BuyTicketController
{
    @javafx.fxml.FXML
    private ComboBox<LocalTime> hourCB;
    @javafx.fxml.FXML
    private DatePicker dateDP;
    @javafx.fxml.FXML
    private TextField tfCantidad;
    @javafx.fxml.FXML
    private BorderPane bp;
    private Alert alert;
    private Passenger passenger;
    private TicketManager ticketManager;
    private DataCenter dataCenter;
    @javafx.fxml.FXML
    private ComboBox<Airport> originCB;
    @javafx.fxml.FXML
    private ComboBox<Airport> destinyCB;

    @javafx.fxml.FXML
    public void initialize() throws ListException {
        dataCenter = new DataCenter();
        passenger = LoginController.getCurrentUser().getPassenger();
        hourCB.getItems().addAll(Utility.getDepartureHours());
        ticketManager = new TicketManager();
        DoublyLinkedList flights = dataCenter.getAirports();

        for (int i=1;i<=flights.size();i++){
            Airport airport= (Airport) flights.getNode(i).data;
            originCB.getItems().add(airport);
            destinyCB.getItems().add(airport);
        }
        alert=new Alert(Alert.AlertType.INFORMATION);
    }

    @javafx.fxml.FXML
    public void cancelOnAction(ActionEvent actionEvent) {
        tfCantidad.clear();
        dateDP.getEditor().clear();
        hourCB.getEditor().clear();
    }

    @javafx.fxml.FXML
    public void confirmOnAction(ActionEvent actionEvent) {
        Airport origin=originCB.getSelectionModel().getSelectedItem();
        Airport destiny=destinyCB.getSelectionModel().getSelectedItem();
        int cantidad = Integer.parseInt(tfCantidad.getText());
        LocalTime time = hourCB.getSelectionModel().getSelectedItem();
        LocalDateTime localDateTime = LocalDateTime.of(dateDP.getValue(), time);
        Flight flight;
        Ticket ticket;
        try {
            flight=returnFlightData(origin,destiny,localDateTime);
            if (validarVueloexistente(flight)) {
                if (camposDisponibles(flight)) {
                    alert.setTitle("Compra de ticketes");
                    showAlert(Alert.AlertType.INFORMATION, "Tiquete comprado con exito");
                    ticket=new Ticket(flight.getOrigin(),flight.getDestination(),passenger,localDateTime,cantidad,flight.getFlightID());
                    ticketManager.addTicket(ticket);
                    passenger.addFlight_ToHistory(flight);
                    loadTicket(flight);
                } else {
                    alert.setTitle("Compra de ticketes");
                    alert.setAlertType(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("El vuelo para la fecha " + localDateTime + " esta lleno \n" + proximoDispo(flight));
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        Flight newFlight = findNextFlight(dataCenter.getFlights(), flight);
                        alert.setTitle("Compra de ticketes");
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setContentText("Tickete comprado con exito");
                        ticket=new Ticket(flight.getOrigin(),flight.getDestination(),passenger,localDateTime,cantidad,newFlight.getFlightID());
                        ticketManager.addTicket(ticket);
                        passenger.addFlight_ToHistory(newFlight);
                        loadTicket(newFlight);
                    }
                }
            }
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTicket(Flight newFlight) throws ListException {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("ticket.fxml"));
            Parent root = loader.load();
            TicketController ticketController = loader.getController();
            ticketController.setData(passenger, newFlight);
            Stage stage = new Stage();
            stage.setTitle("Ticket");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            DataCenter.enQueueOperation("Tickete comprado");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String proximoDispo(Flight flight) throws ListException {
        Flight nextFlight=findNextFlight(dataCenter.getFlights(),flight);
        if(nextFlight==null){
            return "No se han programado mas vuelos de "+flight.getOrigin().getCountry()+" a "+flight.getDestination().getCountry();
        }
        if (camposDisponibles(nextFlight))
            return "El proximo vuelo disponible de "+flight.getOrigin().getCountry()+" a "+flight.getDestination().getCountry()+" sale "+flight.getDepartureTime()
                +"\n desea abordarlo? ";
        return proximoDispo(nextFlight);
    }

    private Flight findNextFlight(CircularDoublyLinkedList flights,Flight flight) throws ListException {
        for (int i=1;i<=flights.size();i++){
            Flight fl=(Flight) flights.getNode(i).data;
            if (fl.getOrigin().equals(flight.getOrigin())
                    && fl.getDestination().equals(flight.getDestination())
                    &&fl.getDepartureTime().getDayOfMonth()>=flight.getDepartureTime().getDayOfMonth()
                    &&fl.getDepartureTime().getHour()>flight.getDepartureTime().getHour()){
                return fl;
            }
        }
        return null;
    }

    private boolean camposDisponibles(Flight flight) {
        return flight.getOccupancy()<flight.getCapacity();
    }

    private boolean validarVueloexistente(Flight flight) throws ListException {
        CircularDoublyLinkedList flights=dataCenter.getFlights();
        for (int i=1;i<=flights.size();i++){
            Flight fl=(Flight) flights.getNode(i).data;
            if (fl.getOrigin().equals(flight.getOrigin())&&fl.getDestination().equals(flight.getDestination()));
            return true;
        }
        return false;
    }
    private Flight returnFlightData(Airport origin,Airport destiny,LocalDateTime ld) throws ListException {
        Flight flight=new Flight(origin,destiny,ld);
        CircularDoublyLinkedList flights=dataCenter.getFlights();
        for (int i=1;i<=flights.size();i++){
            Flight fl=(Flight) flights.getNode(i).data;
            if (fl.getOrigin().equals(flight.getOrigin())&&fl.getDestination().equals(flight.getDestination())
                    &&fl.getDepartureTime().isEqual(fl.getDepartureTime())){
                return fl;
            }
        }
        return flight;
    }
    private void showAlert(Alert.AlertType type, String msg){
        alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}