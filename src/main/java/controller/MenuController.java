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
import model.*;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;

public class MenuController
{

    private Node root;
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane mainMenu;
    private PassengersData passengersData;
    private RouteData routeData;
    private int counter = 0;

    @javafx.fxml.FXML
    public void initialize() {
        Platform.runLater(() -> {
            root = mainMenu;
        });
        passengersData = new PassengersData();
        routeData = new RouteData();
        counter = 50;
    }

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public void menuCreatePassenger(ActionEvent actionEvent) {
        loadPage("createPassenger.fxml");
    }

    @FXML
    public void menuViewPassengers(ActionEvent actionEvent) throws IOException {
        loadPage("passengerManager.fxml");
        for (int i = 0; i < counter; i++) {
            List<Passenger> passengerList = new ArrayList<>(PassengersData.getRandomNames(50));
            PassengersData.writePassengersToJSON("src/main/java/data/passengers.json", passengerList);


            //TODO cambiar en ver rutas
            List<Airport> airports = RouteData.readAirportsFromJSON("src/main/java/data/airports.json");
            List<Route> routeList = RouteData.generateRandomRoutes(20, airports);
            RouteData.writeRoutesToJSON("src/main/java/data/routes.json", routeList);
        }
    }

    @Deprecated
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

    @FXML
    public void flightRadar(ActionEvent actionEvent) {loadPage("flight_map.fxml");
    }
}