package controller;

import com.cretaairlines.HelloApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MenuController
{

    private Node root;
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane mainMenu;

    @javafx.fxml.FXML
    public void initialize() {
        Platform.runLater(() -> {
            root = mainMenu;
        });
    }

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void menuCreatePassenger(ActionEvent actionEvent) {
        loadPage("createPassenger.fxml");
    }

    @FXML
    public void menuViewPassengers(ActionEvent actionEvent) {loadPage("passengerManager.fxml");
    }

    @FXML
    public void menuCreateFlight(ActionEvent actionEvent) {
        CreateFlightController controller = new CreateFlightController();
        controller.setFlightManagerController(new FlightManagerController());
        loadPage("createFlight.fxml");
    }

    @FXML
    public void menuAirport(ActionEvent actionEvent) {loadPage("airport.fxml");
    }

    @FXML
    public void menuViewFlights(ActionEvent actionEvent) {loadPage("flightManager.fxml");
    }

    @FXML
    public void mainmenuOnAction(Event event) {
        bp.setCenter(mainMenu);
    }

    @FXML
    public void menuLogout(ActionEvent actionEvent) {
        loadPage("login.fxml");
    }
}