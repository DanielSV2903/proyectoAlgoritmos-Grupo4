package controller;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import model.Flight;
import model.Passenger;
import model.tda.ListException;
import model.tda.SinglyLinkedList;

public class FlightDetailsController
{
    @javafx.fxml.FXML
    private TableColumn<Passenger,String> destinyCol;
    @javafx.fxml.FXML
    private TableColumn<Passenger,Integer> idCol;
    @javafx.fxml.FXML
    private TableColumn<Passenger,String> originCol;
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
            label.setText(label.getText()+flight.getFlightID()+" "+flight.getOrigin().getCountry()+"-"+flight.getDestination().getCountry());
            passengers=flight.getPassengers();
            try {
                tview.getItems().clear();
                for (int i=1;i<=passengers.size();i++){
                    Object raw=passengers.getNode(i).data;
                    if (raw instanceof Passenger)
                        tview.getItems().add((Passenger) raw);
                }
            } catch (ListException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @javafx.fxml.FXML
    public void initialize() {
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