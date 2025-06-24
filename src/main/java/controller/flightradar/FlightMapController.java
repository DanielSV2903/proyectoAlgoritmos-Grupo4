package controller.flightradar;

import com.cretaairlines.HelloApplication;
import controller.MenuController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.*;
import model.datamanagment.*;
import model.tda.ListException;
import model.tda.QueueException;
import model.tda.graph.GraphException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlightMapController {

    @FXML
    private Pane mapPane;
    @FXML
    private ImageView imageViewMap;
    @FXML
    private Canvas canvasPlane;

    private final RouteManager routeManager = new RouteManager();
    private final AirportManager airportManager = new AirportManager();
    private final FlightManager flightManager = new FlightManager();
    private final PassengerManager passengerManager = new PassengerManager();
    private final AirportQueueManager airportQueueManager = new AirportQueueManager();
    private final BoardingAssignmentManager assignmentManager = new BoardingAssignmentManager(airportQueueManager, flightManager);
    private final BoardingSimulator boardingSimulator = new BoardingSimulator(airportQueueManager);
    @FXML
    private ComboBox<String> fromAirportComboBox;
    @FXML
    private ComboBox<String> toAirportComboBox;

    @FXML
    public void initialize() {
        imageViewMap.setImage(new Image(getClass().getResource("/images/mapamundi.jpg").toExternalForm()));
        try {
            drawMap();
            populateAirportComboBoxes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateAirportComboBoxes() throws ListException {
        List<Route> routes = routeManager.getRoutes().toTypedList();
        for (Route r : routes) {
            fromAirportComboBox.getItems().add(r.getOrigin_airport_id());
            toAirportComboBox.getItems().add(r.getDestination_airport_id());
        }
    }

    @FXML
    public void simulateFlightsOnAction(ActionEvent event) {
        try {
            mapPane.getChildren().removeIf(node -> node instanceof Circle || node instanceof Line);

            String fromCode = fromAirportComboBox.getValue();
            String toCode = toAirportComboBox.getValue();

            if (fromCode == null || toCode == null || fromCode.equals(toCode)) {
                System.out.println("Selección inválida");
                return;
            }

            Airport origin = findAirportByCode(fromCode);
            Airport destination = findAirportByCode(toCode);

            if (origin == null || destination == null) {
                System.out.println("No se encontraron los aeropuertos seleccionados");
                return;
            }

            List<Route> routeList = routeManager.getRoutes().toTypedList();
            List<Airport> airportList = airportManager.getAirports().toTypedList();

            for (Airport airport : airportList) {
                double x = airport.getMapX();
                double y = airport.getMapY();

                Circle node = new Circle(x + 20, y + 20, 4, Color.DODGERBLUE);
                Text label = new Text(x + 24, y + 24, airport.getCode());
                label.setStyle("-fx-font-size: 10px; -fx-fill: white;");

                mapPane.getChildren().addAll(node, label);
            }

            List<Point2D> path = new ArrayList<>();
            path.add(new Point2D(origin.getMapX(), origin.getMapY()));
            for (Route route : routeList) {
                if (route.getOrigin_airport_id().equalsIgnoreCase(fromCode) &&
                        route.getDestination_airport_id().equalsIgnoreCase(toCode)) {
                    Airport dest = findAirportByCode(toCode);
                    if (dest != null) {
                        path.add(new Point2D(dest.getMapX(), dest.getMapY()));
                        Line edge = new Line(
                                origin.getMapX() + 20, origin.getMapY() + 20,
                                dest.getMapX() + 20, dest.getMapY() + 20
                        );
                        edge.setStroke(Color.LIMEGREEN);
                        edge.setStrokeWidth(3);
                        mapPane.getChildren().add(edge);
                        break;
                    }
                }
            }

            animateFlight(new FlightRadar(fromCode + "->" + toCode, path));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Airport findAirportByCode(String code) throws ListException {
        List<Airport> airports = airportManager.getAirports().toTypedList();
        for (Airport a : airports) {
            if (a.getCode().equalsIgnoreCase(code)) {
                return a;
            }
        }
        return null;
    }

    @FXML
    public void flightRadarOnAction(ActionEvent actionEvent) {
        try {
            mapPane.getChildren().retainAll(imageViewMap);
            // 🔧 Asegúrate de que las colas estén creadas y asignadas
            assignmentManager.assignPassengersToQueues();
            drawMap();
            List<Flight> flights = flightManager.getFlights().toTypedList();
            for (Flight flight : flights) {
                simulateFlightRadar(flight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void simulateFlightRadar(Flight flight) throws ListException, QueueException, GraphException {
        boardingSimulator.boardPassengers(flight);

        // Obtener aeropuertos válidos por código
        Airport origin = findAirportByCode(flight.getOrigin().getCode());
        Airport destination = findAirportByCode(flight.getDestination().getCode());

        if (origin == null || destination == null) {
            System.out.println("Origen o destino no encontrado.");
            return;
        }
        RouteResult routeResult = routeManager.getShortestRouteBetweenAirports(origin, destination);

        if (routeResult == null) {
            System.out.println("No hay ruta de " + origin.getCode() + " a " + destination.getCode());
            return;
        }

        List<Object> escalaCodes = routeResult.getPath();
        List<Point2D> path = new ArrayList<>();

        for (Object code : escalaCodes) {
            Airport airport = findAirportByCode(code.toString());
            if (airport != null) {
                path.add(new Point2D(airport.getMapX(), airport.getMapY()));
            }
        }

        if (path.size() < 2) {
            System.out.println("Ruta demasiado corta para animar.");
            return;
        }

        for (Point2D p : path) {
            Circle dot = new Circle(p.getX() + 20, p.getY() + 20, 4, Color.RED);
            mapPane.getChildren().add(dot);
        }

        FlightRadar radar = new FlightRadar(origin.getCode() + "->" + destination.getCode(), path);
        animateFlight(radar);

        System.out.println("Vuelo " + flight.getFlightID() + " completado. Pasajeros a bordo: " +
                flight.getOccupancy() + "/" + flight.getCapacity());

    }

    @FXML
    public void backOnAction(ActionEvent event) {
        //loadPage();
    }

    private void animateFlight(FlightRadar flight) {
        List<Point2D> path = flight.getPath();
        if (path.size() < 2) return;

        ImageView plane = new ImageView(new Image(getClass().getResource("/images/avion.png").toExternalForm()));
        plane.setFitWidth(40);
        plane.setFitHeight(40);
        plane.setLayoutX(path.get(0).getX());
        plane.setLayoutY(path.get(0).getY());
        mapPane.getChildren().add(plane);

        Timeline timeline = new Timeline();
        for (int i = 1; i < path.size(); i++) {
            Point2D point = path.get(i);
            KeyFrame frame = new KeyFrame(Duration.seconds(i), e -> {
                plane.setLayoutX(point.getX());
                plane.setLayoutY(point.getY());
            });
            timeline.getKeyFrames().add(frame);
        }
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void drawMap() throws Exception {
        List<Route> routes = routeManager.getRoutes().toTypedList();
        List<Airport> airports = airportManager.getAirports().toTypedList();

        mapPane.getChildren().removeIf(n -> n instanceof Circle || n instanceof Line || n instanceof Text);
        if (!mapPane.getChildren().contains(imageViewMap)) {
            mapPane.getChildren().add(0, imageViewMap);
        }

        for (Airport airport : airports) {
            double x = airport.getMapX();
            double y = airport.getMapY();

            Circle node = new Circle(x + 20, y + 20, 4, Color.DODGERBLUE);
            Text label = new Text(x + 24, y + 24, airport.getCode());
            label.setStyle("-fx-font-size: 10px; -fx-fill: black;");

            mapPane.getChildren().addAll(node, label);
        }

        for (Route route : routes) {
            Airport origin = findAirportByCode(route.getOrigin_airport_id());
            Airport dest = findAirportByCode(route.getDestination_airport_id());

            if (origin != null && dest != null) {
                Line edge = new Line(
                        origin.getMapX() + 20, origin.getMapY() + 20,
                        dest.getMapX() + 20, dest.getMapY() + 20
                );
                edge.setStroke(Color.BLACK);
                edge.setStrokeWidth(1);
                mapPane.getChildren().add(edge);
            }
        }
    }

    private Airport getExistingAirportObject(String code) throws ListException {
        for (int i = 1; i <= airportManager.getAirports().size(); i++) {
            Airport a = (Airport) airportManager.getAirports().getNode(i).data;
            if (a.getCode().trim().equalsIgnoreCase(code.trim())) {
                return a;
            }
        }
        return null;
    }

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
