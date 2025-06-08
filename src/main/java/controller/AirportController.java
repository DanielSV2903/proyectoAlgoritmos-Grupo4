package ucr.lab.proyectoayed.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ucr.lab.proyectoayed.model.tda.SinglyLinkedList;

public class AirportController
{
    @javafx.fxml.FXML
    private TextField countryTextField;
    @javafx.fxml.FXML
    private TableColumn codeColumn;
    @javafx.fxml.FXML
    private TableView airportTableView;
    @javafx.fxml.FXML
    private TableColumn nameColumn;
    @javafx.fxml.FXML
    private TextField codeTextField;
    @javafx.fxml.FXML
    private TextField cityTextField;
    @javafx.fxml.FXML
    private TableColumn cityColumn;
    @javafx.fxml.FXML
    private TableColumn countryColumn;
    @javafx.fxml.FXML
    private TextField nameTextField;
    private SinglyLinkedList singlyLinkedList;

    @javafx.fxml.FXML
    public void initialize() {
        singlyLinkedList = new SinglyLinkedList();
    }

    @javafx.fxml.FXML
    public void addAirportOnAction(ActionEvent actionEvent) {
    }
}