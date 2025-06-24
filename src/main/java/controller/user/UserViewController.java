package controller.user;

import com.cretaairlines.HelloApplication;
import controller.CreateFlightController;
import controller.FlightManagerController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.*;

import java.io.IOException;

public class UserViewController
{
    @javafx.fxml.FXML
    private AnchorPane mainMenu;
    @javafx.fxml.FXML
    private BorderPane bp;
    private int counter = 0;
    private PassengersData passengersData;
    private Node root;
    private RouteData routeData;

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


    @Deprecated
    public void menuCreateFlight(ActionEvent actionEvent) {
        CreateFlightController controller = new CreateFlightController();
        controller.setFlightManagerController(new FlightManagerController());
        loadPage("createFlight.fxml");
    }

    @FXML
    public void menuAirport(ActionEvent actionEvent) {loadPage("airportForUsers.fxml");
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

    @FXML
    public void buyTicketOnAction(ActionEvent actionEvent) {
        loadPage("buyTicket.fxml");
    }

    @FXML
    public void userInfoOnAction(ActionEvent actionEvent) {
        loadPage("userInfo.fxml");
    }
    @FXML
    public void myTicketsOnAction(ActionEvent actionEvent) {
        loadPage("userTickets.fxml");
    }

    @FXML
    public void historyOnAction(ActionEvent actionEvent) {
        loadPage("history.fxml");
    }
}