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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Flight;
import model.Passenger;
import model.datamanagment.FlightManager;
import model.datamanagment.PassengerManager;
import model.tda.*;
import util.Utility;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FlightManagerController
{
    @javafx.fxml.FXML
    private TableColumn<Flight,Integer> codeColumn;
    @javafx.fxml.FXML
    private TableColumn<Flight,String> departureTimeCol;
    @javafx.fxml.FXML
    private TableColumn<Flight,String> destinyCol;
    @javafx.fxml.FXML
    private TableColumn<Flight,Integer> passengersCol;
    @javafx.fxml.FXML
    private TableColumn<Flight,String> originCol;
    @javafx.fxml.FXML
    private TableColumn<Flight,Integer> capacityCol;
    private CircularDoublyLinkedList flightList;
    private FlightManager flightManager;
    @javafx.fxml.FXML
    private TableView<Flight> flightTableView;

    @javafx.fxml.FXML
    public void initialize() {
        flightManager=new FlightManager();
        flightList=new CircularDoublyLinkedList();
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("flightID"));
        originCol.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getOrigin().getCity()));
        destinyCol.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getDestination().getCity()));
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        passengersCol.setCellValueFactory(new PropertyValueFactory<>("occupancy"));
        departureTimeCol.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getDepartureTime().toString()));
        flightList=flightManager.getFlights();
        try {
            //Cargar los vuelos a la tv
        if (!flightList.isEmpty()){
            flightTableView.getItems().clear();
            for (int i=1;i<=flightList.size();i++){
                Flight flight=(Flight)flightList.getNode(i).data;
                flightTableView.getItems().add(flight);
            }
        }
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public void statsOnAction(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void removeFlightOnAction(ActionEvent actionEvent) {
        Flight flight= flightTableView.getSelectionModel().getSelectedItem();
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Flight");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to remove this flight?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                flightManager.removeFlight(flight);
                updateTV();
            }else if (result.get() == ButtonType.CANCEL){
                alert.close();
            }
        }catch (ListException e){
            e.printStackTrace();
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
        flightList=flightManager.getFlights();
        for (int i=1;i<=flightList.size();i++){
            Flight flight=(Flight)flightList.getNode(i).data;
            flightTableView.getItems().add(flight);
        }
    }

    public CircularDoublyLinkedList getFlightList() {
        return flightList;
    }

    public void addFlight(Flight flight) {
        this.flightManager.addFlight(flight);
    }

    @javafx.fxml.FXML
    public void addPassengers(ActionEvent actionEvent) {
        try {
        Flight flight= flightTableView.getSelectionModel().getSelectedItem();
        flightManager.removeFlight(flight);
        PassengerManager passengerManager=new PassengerManager();
        AVL passengersAVL=passengerManager.getPassengers();
        int cap=flight.getCapacity();
        String pasajeros="";
            List<Passenger> passengerList =passengersAVL.toTypedList(Passenger.class);
            for (int i=0;i<cap;i++){
                int rand=Utility.random(passengerList.size());
                Passenger passenger=passengerList.get(rand);
                pasajeros+=passenger.getName()+"\n";
                flight.getPassengers().add(passenger);
            }
            flight.setOccupancy(flight.getPassengers().size());
            flightManager.addFlight(flight);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Added Passengers");
            alert.setHeaderText(null);
            alert.setContentText("Pasajeros añadidos:\n"+pasajeros);
            alert.showAndWait();
            updateTV();
        } catch (TreeException | ListException e) {
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void detailsOnAction(ActionEvent actionEvent) {
        try {
            Flight flight = flightTableView.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("flightDetails.fxml"));
                Parent root = loader.load();
                FlightDetailsController flightDetailsController = loader.getController();
                flightDetailsController.setController(this);
                flightDetailsController.setFlight(flight);
                Stage stage = new Stage();
                stage.setTitle("Detalles del vuelo");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait(); // Espera que se cierre para continuar
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}