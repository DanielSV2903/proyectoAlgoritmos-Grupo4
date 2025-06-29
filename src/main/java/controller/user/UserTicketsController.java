package controller.user;

import com.cretaairlines.HelloApplication;
import com.sun.source.tree.ParenthesizedTree;
import controller.FlightHistoryController;
import controller.LoginController;
import controller.TicketController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Flight;
import model.Passenger;
import model.PassengersData;
import model.Ticket;
import model.datamanagment.DataCenter;
import model.datamanagment.FlightManager;
import model.datamanagment.TicketManager;
import model.tda.ListException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserTicketsController
{
    @javafx.fxml.FXML
    private VBox ticketVBox;
    private TicketManager ticketManager;
    private List<Ticket> tickets;
    private Passenger passenger;
    @FXML
    private BorderPane bp;

    @FXML
    public void initialize() {
        ticketVBox.getChildren().clear();

        FlightManager flightManager = new FlightManager();
        passenger = LoginController.getCurrentUser().getPassenger();
        ticketManager = new TicketManager();
        tickets = ticketManager.getTickets();

        List<Ticket> filteredTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getPassenger().getId() == passenger.getId()) {
                filteredTickets.add(ticket);
            }
        }

        try {
            for (Ticket ticket : filteredTickets) {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ticket.fxml"));
                Parent root = fxmlLoader.load();

                TicketController ticketController = fxmlLoader.getController();
                Flight flight = flightManager.getFlight(ticket.getFlID());

                ticketController.setData(ticket.getPassenger(), flight);
                root.setOnMouseClicked(event -> {
                    try {
                        DataCenter.enQueueOperation("Revisando estadisticas de " + ticket.getPassenger().getName());
                        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("history.fxml"));
                        Parent view = loader.load();
                        HistoryController flightHistoryController = loader.getController();
                        flightHistoryController.setPassenger(ticket.getPassenger());
                        Stage stage = new Stage();
                        stage.setTitle("Historial de vuelos");
                        stage.setScene(new Scene(view));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait(); // Espera que se cierre para continuar
                    } catch (IOException | ListException e) {
                        e.printStackTrace();
                    }
                });
                ticketVBox.getChildren().add(root);
            }
        } catch (IOException | ListException e) {
            e.printStackTrace(); // 💡 Importante para detectar errores
        }
    }
    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}