package controller;

import com.cretaairlines.HelloApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Passenger;
import model.datamanagment.PassengerManager;
import model.tda.AVL;
import model.tda.ListException;
import model.tda.SinglyLinkedList;
import model.tda.TreeException;

import java.io.IOException;
import java.util.List;

public class PassengerManagerController
{
    @javafx.fxml.FXML
    private TableColumn<Passenger,String> vuelosCol;
    @javafx.fxml.FXML
    private TableColumn<Passenger,Integer> codeColumn;
    @javafx.fxml.FXML
    private TableView<Passenger> passengerTableView;
    @javafx.fxml.FXML
    private TableColumn<Passenger,String> nameCol;
    @javafx.fxml.FXML
    private TableColumn<Passenger,String> nationalidadCol;
    private PassengerManager passengerManager;
    private AVL passengersAVL;
    @javafx.fxml.FXML
    private TextField tfFilter;
    private FilteredList<Passenger> filteredPassengers;


    @javafx.fxml.FXML
    public void initialize() {
        passengerManager = new PassengerManager();
        passengersAVL = new AVL();

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nationalidadCol.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        try {
        vuelosCol.setCellValueFactory(data-> {
                    try {
                        SinglyLinkedList list = data.getValue().getFlight_history();
                        if (!list.isEmpty())
                            return new SimpleStringProperty(data.getValue().getFlight_history().size()+"");
                        return new SimpleStringProperty("0");
                    } catch (ListException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        passengersAVL=passengerManager.getPassengers();
            List<Passenger> passengerList =passengersAVL.toTypedList(Passenger.class);
            passengerTableView.getItems().clear();
            filteredPassengers = new FilteredList<>(
                    FXCollections.observableArrayList(passengerList),
                    p -> true
            );
            passengerTableView.setItems(filteredPassengers);
            tfFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredPassengers.setPredicate(passenger -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    return String.valueOf(passenger.getId()).contains(newValue);
                });
            });

        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void statsOnAction(ActionEvent actionEvent) {
        Passenger selectedPassenger = passengerTableView.getSelectionModel().getSelectedItem();
        if (selectedPassenger != null) {
            //TODO
        }
    }

    @javafx.fxml.FXML
    public void removePassengerOnAction(ActionEvent actionEvent) {
        Passenger selectedPassenger = passengerTableView.getSelectionModel().getSelectedItem();
        if (selectedPassenger != null) {
            try {
                passengerManager.removePassenger(selectedPassenger);
                updateTV();
            } catch (TreeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @javafx.fxml.FXML
    public void createPassengerOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("createPassenger.fxml"));
            Parent root = loader.load();
            CreatePassengerController createPassengerController = loader.getController();
            createPassengerController.setPassengerManagerController(this);
            Stage stage = new Stage();
            stage.setTitle("Crear nuevo pasajero");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTV() throws TreeException {
        List<Passenger> passengerList = passengersAVL.toTypedList(Passenger.class);
        filteredPassengers = new FilteredList<>(
                FXCollections.observableArrayList(passengerList),
                p -> true
        );
        passengerTableView.setItems(filteredPassengers);

        tfFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPassengers.setPredicate(passenger -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return String.valueOf(passenger.getId()).contains(newValue);
            });
        });
    }

    public void registerPassenger(Passenger passenger) {
        passengerManager.addPassenger(passenger);
    }
}