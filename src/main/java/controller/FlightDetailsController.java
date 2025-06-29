package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import model.Flight;
import model.Passenger;
import model.datamanagment.DataCenter;
import model.tda.ListException;
import model.tda.SinglyLinkedList;

public class FlightDetailsController
{
    @javafx.fxml.FXML
    private TableColumn<Passenger,Integer> idCol;
    @javafx.fxml.FXML
    private TableColumn<Passenger,String> nameCol;
    @javafx.fxml.FXML
    private TableColumn<Passenger,String> nacionalityCol;
    @javafx.fxml.FXML
    private Label label;
    @javafx.fxml.FXML
    private BorderPane bp;
    @javafx.fxml.FXML
    private TableView<Passenger> tview;
    private FlightManagerController controller;
    private Flight flight;
    private SinglyLinkedList passengers;

    public void setController(FlightManagerController controller) {
        this.controller = controller;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
        updateFlightDetails();
    }
    private void updateFlightDetails() {
        if (flight != null) {
            label.setText(label.getText()+flight.getFlightID()+" "+flight.getOrigin().getCity()+"-"+flight.getDestination().getCity());
            passengers=flight.getPassengers();
            try {
                tview.getItems().clear();
                if (!passengers.isEmpty()) {
                    for (int i=1;i<=passengers.size();i++){
                        Object raw=passengers.getNode(i).data;
                        if (raw instanceof Passenger)
                            tview.getItems().add((Passenger) raw);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("No hay pasajeros registrados en este vuelo");
                    alert.showAndWait();
                }
            } catch (ListException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @javafx.fxml.FXML
    public void initialize() {
        DataCenter.enQueueOperation("Gestion de vuelos");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nacionalityCol.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    }
    private void updateTV() throws ListException {
        tview.getItems().clear();
        passengers=flight.getPassengers();
        for (int i=1;i<=passengers.size();i++){
            tview.getItems().add((Passenger) passengers.getNode(i).data);
        }
    }
}