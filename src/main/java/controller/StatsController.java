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
        // Inicializa texto de salida
        String topAirports = "";
        dataCenter = new DataCenter();

        // Carga estructuras desde el DataCenter
        DoublyLinkedList airports = dataCenter.getAirports();
        CircularDoublyLinkedList flights = dataCenter.getFlights();
        AVL passengersAVL = dataCenter.getPassengers();

        // Prepara mapeo JSON para rutas
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/java/data/routes.json");

        // Listas auxiliares para trabajar con datos
        List<Flight> flightList = new ArrayList<>();
        List<Airport> airportList = new ArrayList<>();

        try {
            // Convierte el AVL de pasajeros a lista ordenada
            List<Passenger> passengerList = passengersAVL.toTypedList(Passenger.class);

            // Carga las rutas desde el archivo JSON
            List<Route> routes = mapper.readValue(file, new TypeReference<List<Route>>() {});

            // Convierte la lista enlazada de vuelos a lista común
            for (int i = 1; i <= flights.size(); i++) {
                flightList.add((Flight) flights.getNode(i).data);
            }
            // Convierte la lista enlazada de aeropuertos a lista común
            for (int i = 1; i <= airports.size(); i++) {
                airportList.add((Airport) airports.getNode(i).data);
            }

            int counter = 0;
            List<Map.Entry<Airport, Integer>> airportCounts = new ArrayList<>();

            // Cuenta cuántos vuelos salen de cada aeropuerto
            for (Airport airport : airportList) {
                for (Flight flight : flightList) {
                    if (Utility.compare(airport, flight.getOrigin()) == 0) {
                        counter++;
                    }
                }
                airportCounts.add(new AbstractMap.SimpleEntry<>(airport, counter));
                counter = 0;
            }

            // Ordena aeropuertos por número de vuelos salientes
            airportCounts.sort((a, b) -> b.getValue() - a.getValue());

            // Muestra los 5 aeropuertos con más salidas
            int topN = Math.min(5, airportCounts.size());
            for (int i = 0; i < topN; i++) {
                Map.Entry<Airport, Integer> entry = airportCounts.get(i);
                if (entry.getValue() != 0)
                    topAirports += entry.getKey().getName() + " → " + entry.getValue() + " vuelos\n";
            }

            topAirportsTextArea.setText(topAirports);

            // Rutas más utilizadas
            String topRoutes = "";
            List<Map.Entry<Route, Integer>> routeCounts = new ArrayList<>();

            // Cuenta cuántos vuelos corresponden a cada ruta
            for (Route route : routes) {
                for (Flight flight : flightList) {
                    if (Utility.compare(route.getOrigin_airport_id(), flight.getOrigin().getCode()) == 0 &&
                            Utility.compare(route.getDestination_airport_id(), flight.getDestination().getCode()) == 0) {
                        counter++;
                    }
                }
                routeCounts.add(new AbstractMap.SimpleEntry<>(route, counter));
                counter = 0;
            }

            // Ordena rutas por número de coincidencias
            routeCounts.sort((a, b) -> b.getValue() - a.getValue());

            // Muestra las 3 rutas más usadas
            topN = Math.min(3, routeCounts.size());
            for (int i = 0; i < topN; i++) {
                Map.Entry<Route, Integer> entry = routeCounts.get(i);
                if (entry.getValue() != 0)
                    topRoutes += entry.getKey().getOrigin_airport_id() + " → "
                            + entry.getKey().getDestination_airport_id()
                            + ": " + entry.getValue() + "\n";
            }

            topRoutesTextArea.setText(topRoutes);

            // Pasajeros con más vuelos realizados
            String topPassengers = "";
            List<Map.Entry<Passenger, Integer>> passengersCounts = new ArrayList<>();

            // Cuenta el historial de vuelos por pasajero
            for (Passenger passenger : passengerList) {
                int flightsCount = passenger.getFlight_history().isEmpty() ? 0 : passenger.getFlight_history().size();
                passengersCounts.add(new AbstractMap.SimpleEntry<>(passenger, flightsCount));
            }

            // Ordena por número de vuelos realizados
            passengersCounts.sort((a, b) -> b.getValue() - a.getValue());

            // Muestra los 3 pasajeros con más vuelos
            topN = Math.min(3, passengersCounts.size());
            for (int i = 0; i < topN; i++) {
                Map.Entry<Passenger, Integer> entry = passengersCounts.get(i);
                if (entry.getValue() != 0)
                    topPassengers += entry.getKey().getName() + ": " + entry.getValue() + " vuelos\n";
            }

            topPassengersTextArea.setText(topPassengers);

            // Cálculo del porcentaje de ocupación promedio
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
        // Abre un diálogo para que el usuario elija la ruta del PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte Estadístico");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf")
        );
        fileChooser.setInitialFileName("reporte_estadistico.pdf");

        // Asocia el diálogo a la ventana actual
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);

        // Si el usuario eligió una ruta, genera el PDF
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
