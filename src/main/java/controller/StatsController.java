package controller;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import model.datamanagment.DataCenter;
import model.tda.*;
import model.tda.graph.DirectedSinglyLinkedListGraph;
import util.Utility;

import java.io.File;
import java.io.IOException;
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
        AVL passengersAVL= dataCenter.getPassengers();

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/java/data/routes.json");

        List<Flight> flightList = new ArrayList<>();
        List<Airport> airportList = new ArrayList<>();

        try {
            List<Passenger> passengerList =passengersAVL.toTypedList(Passenger.class);
            List<Route> routes = mapper.readValue(file, new TypeReference<List<Route>>() {});
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

            String topRoutes = "";

            List<Map.Entry<Route, Integer>> routeCounts = new ArrayList<>();

            for (Route route : routes) {
                for (Flight flight : flightList){
                    if (Utility.compare(route.getOrigin_airport_id(), flight.getOrigin().getCode())==0 && Utility.compare(route.getDestination_airport_id(), flight.getDestination().getCode())==0){
                        counter++;
                    }
                }
                routeCounts.add(new AbstractMap.SimpleEntry<>(route, counter));
                counter = 0;
            }

            routeCounts.sort((a, b) -> b.getValue() - a.getValue());

            topN = Math.min(3, routeCounts.size());
            for (int i = 0; i < topN; i++) {
                Map.Entry<Route, Integer> entry = routeCounts.get(i);
                topRoutes += entry.getKey().getOrigin_airport_id() +
                        " → " + entry.getKey().getDestination_airport_id() + ": " + entry.getValue() + "\n";
            }

            topRoutesTextArea.setText(topRoutes);

            String topPassengers = "";

            List<Map.Entry<Passenger, Integer>> passengersCounts = new ArrayList<>();

            for (Passenger passenger : passengerList) {
                int flightsCount;
                if (passenger.getFlight_history().isEmpty()) flightsCount = 0;
                else flightsCount = passenger.getFlight_history().size();
                passengersCounts.add(new AbstractMap.SimpleEntry<>(passenger, flightsCount));
            }

            passengersCounts.sort((a, b) -> b.getValue() - a.getValue());

            topN = Math.min(3, passengersCounts.size());
            for (int i = 0; i < topN; i++) {
                Map.Entry<Passenger, Integer> entry = passengersCounts.get(i);
                topPassengers += entry.getKey().getName() + ": " + entry.getValue() + " vuelos\n";
            }

            topPassengersTextArea.setText(topPassengers);

            float porcentaje = 0f;
            for (Flight flight : flightList) {
                porcentaje += (float) (flight.getOccupancy() / flight.getCapacity());
            }
            porcentaje = (porcentaje / flightList.size()) * 100f;

            occupancyPercentajeTf.setText(String.format("%.2f", porcentaje) + "%");

        } catch (ListException | IOException | TreeException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void generatePdfOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte Estadístico");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf")
        );
        fileChooser.setInitialFileName("reporte_estadistico.pdf");

        // Obtener la ventana actual para asociarla al diálogo
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            PdfReportGenerator.generateStatsReport(
                    topAirportsTextArea.getText(),
                    topRoutesTextArea.getText(),
                    topPassengersTextArea.getText(),
                    occupancyPercentajeTf.getText(),
                    selectedFile.getAbsolutePath()
            );
        }
    }
}
