package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Airport;
import model.datamanagment.AirportManager;
import model.tda.ListException;
import model.tda.SinglyLinkedList;

public class AirportController
{
    @javafx.fxml.FXML
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
    private SinglyLinkedList singlyLinkedList;
    private AirportManager airportManager;

    @javafx.fxml.FXML
    public void initialize() {
        airportManager = new AirportManager();
        singlyLinkedList =airportManager.getAirports();
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        try {
        this.airportTableView.getItems().clear();
        if (!singlyLinkedList.isEmpty()&&singlyLinkedList!=null) {
            for (int i=1;i<=singlyLinkedList.size();i++){
                Airport a= (Airport) singlyLinkedList.getNode(i).data;
                this.airportTableView.getItems().add(a);
            }
        }
        }catch (ListException e){
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void addAirportOnAction(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Airport");
        alert.setHeaderText(null);
        alert.setContentText(null);
        if (validarEntradas()){
        String code = codeTextField.getText();
        String city = cityTextField.getText();
        String country = countryTextField.getText();
        String name = nameTextField.getText();
        Airport airport = new Airport(code,name,city,country);
        airportManager.addAirports(airport);
        singlyLinkedList.add(airport);
        alert.setContentText("Airport added correctly");
        alert.showAndWait();
            try {
                updateTV();
            } catch (ListException e) {
                throw new RuntimeException(e);
            }
        }else{
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Llene todas las casillas");
            alert.showAndWait();
        }
    }

    private void updateTV() throws ListException {
        this.airportTableView.getItems().clear();
        for (int i=1;i<singlyLinkedList.size();i++){
            Airport a= (Airport) singlyLinkedList.getNode(i).data;
            this.airportTableView.getItems().add(a);
        }
    }

    private boolean validarEntradas(){
        return !countryTextField.getText().isEmpty()
                &&!cityTextField.getText().isEmpty()&&!nameTextField.getText().isEmpty()&&!codeTextField.getText().isEmpty();
    }
}