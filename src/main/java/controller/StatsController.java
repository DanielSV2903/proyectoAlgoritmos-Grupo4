package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Airport;
import model.Flight;
import model.datamanagment.DataCenter;
import model.tda.CircularDoublyLinkedList;
import model.tda.DoublyLinkedList;
import model.tda.ListException;
import util.Utility;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatsController {

    @javafx.fxml.FXML
    private TextArea topAirportsTextArea;
    @javafx.fxml.FXML
    private TextArea topRoutesTextArea;
    @javafx.fxml.FXML
    private TextField occupancyPercentajeTf;
    @javafx.fxml.FXML
    private TextArea topPassengersTextArea;

    private DataCenter dataCenter;

    @FXML
    public void initialize() {
        String topAirports = "";
        dataCenter = new DataCenter();

        DoublyLinkedList airports = dataCenter.getAirports();
        CircularDoublyLinkedList flights = dataCenter.getFlights();

        List<Flight> flightList = new ArrayList<>();
        List<Airport> airportList = new ArrayList<>();

        try {
            for (int i = 1; i <= flights.size(); i++) {
                flightList.add((Flight) flights.getNode(i).data);
            }
            for (int i = 1; i <= airports.size(); i++) {
                airportList.add((Airport) airports.getNode(i).data);
            }

            int counter = 0;

            List<Map.Entry<Airport, Integer>> airportCounts = new ArrayList<>();

            for (Airport airport : airportList) {
                for (Flight flight : flightList) {
                    if (Utility.compare(airport, flight.getOrigin())==0){
                        counter++;
                    }
                }
                airportCounts.add(new AbstractMap.SimpleEntry<>(airport, counter));
                counter = 0;
            }

            airportCounts.sort((a, b) -> b.getValue() - a.getValue());

            int topN = Math.min(5, airportCounts.size());
            for (int i = 0; i < topN; i++) {
                Map.Entry<Airport, Integer> entry = airportCounts.get(i);
                topAirports += entry.getKey().getName() +
                        " → " + entry.getValue() + " vuelos\n";
            }

            topAirportsTextArea.setText(topAirports);

        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void generatePdfOnAction(ActionEvent actionEvent) {
    }
}
