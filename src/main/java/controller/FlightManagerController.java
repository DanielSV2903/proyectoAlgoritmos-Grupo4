package controller;

import com.cretaairlines.HelloApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Airport;
import model.Flight;
import model.Passenger;
import model.datamanagment.AirportManager;
import model.datamanagment.DataCenter;
import model.datamanagment.FlightManager;
import model.datamanagment.PassengerManager;
import model.tda.*;
import util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FlightManagerController {
    @javafx.fxml.FXML
    private TableColumn<Flight, Integer> codeColumn;
    @javafx.fxml.FXML
    private TableColumn<Flight, String> departureTimeCol;
    @javafx.fxml.FXML
    private TableColumn<Flight, String> destinyCol;
    @javafx.fxml.FXML
    private TableColumn<Flight, Integer> passengersCol;
    @javafx.fxml.FXML
    private TableColumn<Flight, String> originCol;
    @javafx.fxml.FXML
    private TableColumn<Flight, Integer> capacityCol;
    private CircularDoublyLinkedList flightList;
    private FlightManager flightManager;
    @javafx.fxml.FXML
    private TableView<Flight> flightTableView;
    private AirportManager airportManager;

    @javafx.fxml.FXML
    public void initialize() {
        airportManager = new AirportManager();
        flightManager = new FlightManager();
        flightList = new CircularDoublyLinkedList();
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("flightID"));
        originCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrigin().getCity()));
        destinyCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDestination().getCity()));
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        passengersCol.setCellValueFactory(new PropertyValueFactory<>("occupancy"));
        departureTimeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDepartureTime().toString()));
        flightList = flightManager.getFlights();
        try {
            //Cargar los vuelos a la tv
            if (!flightList.isEmpty()) {
                flightTableView.getItems().clear();
                for (int i = 1; i <= flightList.size(); i++) {
                    Flight flight = (Flight) flightList.getNode(i).data;
                    flightTableView.getItems().add(flight);
                }
            }
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public void statsOnAction(ActionEvent actionEvent) {
        showAlert("Esta función no se encuentra implementada en este momento");
    }

    @javafx.fxml.FXML
    public void removeFlightOnAction(ActionEvent actionEvent) {
        Flight flight = flightTableView.getSelectionModel().getSelectedItem();
        if (flight == null) {
            showAlert("Debe seleccionar un vial para eliminarlo");
        }else {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Remove Flight");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to remove this flight?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    flightManager.removeFlight(flight);
                    updateTV();
                } else if (result.get() == ButtonType.CANCEL) {
                    alert.close();
                }
            } catch (ListException e) {
                e.printStackTrace();
            }
        }
    }

    @javafx.fxml.FXML
    public void createFlightOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("createFlight.fxml"));
            Parent root = loader.load();
            CreateFlightController createFlightController = loader.getController();
            createFlightController.setFlightManagerController(this);
            Stage stage = new Stage();
            stage.setTitle("Crear nuevo vuelo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait(); // Espera que se cierre para continuar
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTV() throws ListException {
        flightTableView.getItems().clear();
        flightList = flightManager.getFlights();
        for (int i = 1; i <= flightList.size(); i++) {
            Flight flight = (Flight) flightList.getNode(i).data;
            flightTableView.getItems().add(flight);
        }
    }

    public CircularDoublyLinkedList getFlightList() {
        return flightList;
    }

    public void addFlight(Flight flight) throws ListException {
        this.flightManager.addFlight(flight);
    }

    @javafx.fxml.FXML
    public void addPassengers(ActionEvent actionEvent) {
        try {
            Flight flight = flightTableView.getSelectionModel().getSelectedItem();
            if (flight == null) {
                showAlert("No hay vuelo seleccionado.");
                return;
            }
            PassengerManager passengerManager = new PassengerManager();
            List<Passenger> passengerList = passengerManager.getPassengers().toTypedList(Passenger.class);

            if (passengerList.size() < flight.getCapacity()) {
                showAlert("No hay suficientes pasajeros disponibles para llenar el vuelo.");
                return;
            }

            Flight toHistory = new Flight();
            toHistory.setFlightID(flight.getFlightID());
            toHistory.setOrigin(flight.getOrigin());
            toHistory.setDestination(flight.getDestination());
            toHistory.setCapacity(flight.getCapacity());
            toHistory.setDepartureTime(flight.getDepartureTime());

            List<Passenger> assignedPassengers = new ArrayList<>();
            StringBuilder pasajeros = new StringBuilder();

            Collections.shuffle(passengerList);
            int cantidadPasajeros = util.Utility.random(flight.getCapacity()+1);

            for (int i = 0; i < cantidadPasajeros; i++) {
                Passenger passenger = passengerList.get(i);
                passenger.addFlight_ToHistory(toHistory);
                assignedPassengers.add(passenger);
                pasajeros.append(passenger.getName()).append("\n");
            }

            for (Passenger passenger : assignedPassengers) {
                passengerManager.updatePassenger(passenger);
                flight.addPassenger(passenger);
            }

            flight.setOccupancy(flight.getPassengers().size());
            flightManager.updateFlight(flight);

            showAlert("Pasajeros añadidos:\n" + pasajeros);
            DataCenter.enQueueOperation("Pasajeros añadidos al vuelo:"+flight.getFlightID());
            updateTV();

        } catch (TreeException | ListException e) {
            e.printStackTrace();
            showAlert("Error al añadir pasajeros: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

//    public void addPassengers(ActionEvent actionEvent) {//AIDAN
//        try {
//            Flight flight= flightTableView.getSelectionModel().getSelectedItem();
//            flightManager.removeFlight(flight);
//            PassengerManager passengerManager = new PassengerManager();
//            AVL passengersAVL = passengerManager.getPassengers();
//            int cap = flight.getCapacity();
//            Flight toHistory = new Flight();
//            toHistory.setFlightID(flight.getFlightID());
//            toHistory.setOrigin(flight.getOrigin());
//            toHistory.setDestination(flight.getDestination());
//            toHistory.setDepartureTime(flight.getDepartureTime());
//            String pasajeros="";
//            List<Passenger> passengerList =passengersAVL.toTypedList(Passenger.class);
//
//            for (int i=0;i<cap;i++){
//                int rand=Utility.random(passengerList.size());
//                Passenger passenger=passengerList.get(rand);
//                pasajeros+=passenger.getName()+"\n";
//                if (passenger.getFlight_history().isEmpty()){
//                    passengerManager.removePassenger(passenger);
//                    passenger.getFlight_history().add(toHistory);
//                    passengerManager.addPassenger(passenger);
//                }
//                if (!passenger.getFlight_history().contains(toHistory)&&!passenger.getFlight_history().isEmpty()){
//                    passengerManager.removePassenger(passenger);
//                    passenger.getFlight_history().add(toHistory);
//                    passengerManager.addPassenger(passenger);
//                }
//                if (flight.getPassengers().isEmpty())
//                    flight.getPassengers().add(passenger);
//                if (!flight.getPassengers().contains(passenger)&&!flight.getPassengers().isEmpty()){
//                    flight.getPassengers().add(passenger);
//                }
//            }
//            flight.setOccupancy(flight.getPassengers().size());
//
//            flightManager.addFlight(flight);
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Added Passengers");
//            alert.setHeaderText(null);
//            alert.setContentText("Pasajeros añadidos:\n"+pasajeros);
//            alert.showAndWait();
//            updateTV();
//        } catch (TreeException | ListException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @javafx.fxml.FXML
    public void detailsOnAction(ActionEvent actionEvent) {

        try {
            Flight flight = flightTableView.getSelectionModel().getSelectedItem();
            if (flight == null) {
                showAlert("Debe seleccionar un vuelo para ver sus detalles");
            } else {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("flightDetails.fxml"));
                Parent root = loader.load();
                FlightDetailsController flightDetailsController = loader.getController();
                flightDetailsController.setController(this);
                flightDetailsController.setFlight(flight);
                if (!flight.getPassengers().isEmpty()) {
                    Stage stage = new Stage();
                    stage.setTitle("Detalles del vuelo");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait(); // Espera que se cierre para continuar
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public void routesOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cretaairlines/routesManager.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage(); // Nueva ventana
            stage.setTitle("Visualización de rutas");
            stage.setScene(new Scene(root));
            stage.show();

            // Opcional: cerrar la ventana actual
            // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerDeparture(Flight flight) throws ListException {
        Airport airport=flight.getOrigin();
        String departureInfo="";
        departureInfo+= "Flight: "+flight.getFlightID()+" destiny: "
                +flight.getDestination().getCode()+" departure time "
                + Utility.formatedDate(flight.getDepartureTime());
        airport.addDepartureToBoard(departureInfo);
        airportManager.updateAirport(airport);
    }
}