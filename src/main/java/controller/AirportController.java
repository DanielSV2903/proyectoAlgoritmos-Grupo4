package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.Airport;
import model.datamanagment.AirportManager;
import model.tda.DoublyLinkedList;
import model.tda.ListException;

import java.util.Optional;

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
    private DoublyLinkedList airportList;
    private AirportManager airportManager;
    @javafx.fxml.FXML
    private ComboBox<String> statusCB;
    @javafx.fxml.FXML
    private TableColumn<Airport,String> statusCol;

    @javafx.fxml.FXML
    public void initialize() {
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
            boolean status=false;
            if (statusCB.getSelectionModel().getSelectedItem().equals("Active"))
                status=true;
            String name = nameTextField.getText();
            Airport airport = new Airport(code,name,city,country,status);
            try {
                airportManager.addAirports(airport);
                alert.setContentText("Airport added correctly");
                alert.showAndWait();
                updateTV();
                clear();
            } catch (ListException e) {
                throw new RuntimeException(e);
            }
        }else{
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Llene todas las casillas");
            alert.showAndWait();
        }
    }

    private void clear() {
        countryTextField.clear();
        cityTextField.clear();
        nameTextField.clear();
        codeTextField.clear();
        statusCB.getSelectionModel().clearSelection();
    }

    public void updateTV() throws ListException {
        this.airportTableView.getItems().clear();
        airportList =airportManager.getAirports();
        for (int i = 1; i<= airportList.size(); i++){
            Airport a= (Airport) airportList.getNode(i).data;
            this.airportTableView.getItems().add(a);
        }
    }

    private boolean validarEntradas(){
        return !countryTextField.getText().isEmpty()
                &&!cityTextField.getText().isEmpty()&&!nameTextField.getText().isEmpty()&&!codeTextField.getText().isEmpty();
    }

    @javafx.fxml.FXML
    public void deleteOnAction(ActionEvent actionEvent) {
        Airport a = airportTableView.getSelectionModel().getSelectedItem();
        if (a==null) {
            mostrarAlerta("Debe seleccionar un aeropuerto antes de eliminarlo");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Airport");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this airport?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    airportManager.removeAirport(a);
                    updateTV();
                } catch (ListException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error removing airport: " + e.getMessage()).showAndWait();
                }
            } else if (result.get() == ButtonType.CANCEL) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Confirmado");
                alert2.setHeaderText("El aeropuerto " + a.getName() + " no fue eliminado");
                alert.setContentText(null);
                alert2.showAndWait();
            }

        }
    }

    @javafx.fxml.FXML
    public void editoOnAction(ActionEvent actionEvent) {
        Airport a = (Airport) airportTableView.getSelectionModel().getSelectedItem();
        if (a==null) {
            mostrarAlerta("Debe seleccionar un aeropuerto antes de editarlo");
        }else {
            try {
                if (a != null) {
                    airportManager.removeAirport(a);
                    //Editar el aeropuerto
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Edit Airport");
                    alert.setHeaderText(null);
                    alert.setContentText(null);
                    GridPane gp = new GridPane();
                    TextField tfCode = new TextField(a.getCode());
                    TextField tfName = new TextField(a.getName());
                    TextField tfCity = new TextField(a.getCity());
                    TextField tfCountry = new TextField(a.getCountry());
                    tfCode.setText(a.getCode());
                    tfName.setText(a.getName());
                    tfCity.setText(a.getCity());
                    tfCountry.setText(a.getCountry());
                    ChoiceBox<String> cbStatus = new ChoiceBox();
                    cbStatus.getItems().add("Active");
                    cbStatus.getItems().add("Inactive");
                    cbStatus.getSelectionModel().select("Inactive");
                    if (a.getStatus())
                        cbStatus.getSelectionModel().select("Active");
                    gp.add(new Label("Codigo"), 0, 1);
                    gp.add(tfCode, 1, 1);
                    gp.add(new Label("Nombre"), 0, 2);
                    gp.add(tfName, 1, 2);
                    gp.add(new Label("City"), 0, 3);
                    gp.add(tfCity, 1, 3);
                    gp.add(new Label("Country"), 0, 4);
                    gp.add(tfCountry, 1, 4);
                    gp.add(new Label("Status"), 0, 5);
                    gp.add(cbStatus, 1, 5);
                    alert.getDialogPane().setContent(gp);
                    alert.showAndWait();
                    a.setCode(tfCode.getText());
                    a.setName(tfName.getText());
                    a.setCity(tfCity.getText());
                    a.setCountry(tfCountry.getText());
                    switch (cbStatus.getSelectionModel().getSelectedItem()) {
                        case "Active":
                            a.setStatus(true);
                            break;
                        case "Inactive":
                            a.setStatus(false);
                            break;
                    }
                    airportManager.addAirports(a);
                }
                updateTV();
            } catch (ListException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Seleccione una opción para proceder");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}