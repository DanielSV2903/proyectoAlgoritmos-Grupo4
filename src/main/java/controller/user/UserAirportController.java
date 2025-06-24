package controller.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Airport;
import model.datamanagment.AirportManager;
import model.datamanagment.DataCenter;
import model.tda.DoublyLinkedList;
import model.tda.ListException;

public class UserAirportController
{ @javafx.fxml.FXML
private TextField countryTextField;
    @javafx.fxml.FXML
    private TableColumn<Airport,String> codeColumn;
    @javafx.fxml.FXML
    private TableView<Airport> airportTableView;
    @javafx.fxml.FXML
    private TableColumn<Airport,String> nameColumn;
    @javafx.fxml.FXML
    private TextField codeTextField;
    @javafx.fxml.FXML
    private TextField cityTextField;
    @javafx.fxml.FXML
    private TableColumn<Airport,String> cityColumn;
    @javafx.fxml.FXML
    private TableColumn<Airport,String> countryColumn;
    @javafx.fxml.FXML
    private TextField nameTextField;
    private DoublyLinkedList airportList;
    private AirportManager airportManager;
    @javafx.fxml.FXML
    private ComboBox<String> statusCB;
    @javafx.fxml.FXML
    private TableColumn<Airport,String> statusCol;

    @javafx.fxml.FXML
    public void initialize() {
        DataCenter.enQueueOperation("Gestion de aeropuertos");
        airportManager = new AirportManager();
        airportList =airportManager.getAirports();
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        statusCol.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getStatus()?"Active":"Inactive"));
        try {
            this.airportTableView.getItems().clear();
            if (!airportList.isEmpty()&& airportList !=null) {
                for (int i = 1; i<= airportList.size(); i++){
                    Airport a= (Airport) airportList.getNode(i).data;
                    this.airportTableView.getItems().add(a);
                }
            }
        }catch (ListException e){
            throw new RuntimeException(e);
        }
        this.statusCB.getItems().add("Active");
        this.statusCB.getItems().add("Inactive");
    }
}