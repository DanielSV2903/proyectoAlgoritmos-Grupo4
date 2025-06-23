package controller.flightradar;

import controller.flightradar.FlightRadar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
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
    private final BoardingAssignmentManager assignmentManager =
            new BoardingAssignmentManager(airportQueueManager, flightManager);
    private final BoardingSimulator boardingSimulator =
            new BoardingSimulator(airportQueueManager);

    @FXML
    public void initialize() {
        imageViewMap.setImage(new Image(getClass().getResource("/images/mapamundi.jpg").toExternalForm()));
        try {
            assignmentManager.assignPassengersToQueues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void simulateFlightsOnAction(ActionEvent event) {
        try {
            // Limpia todo menos el fondo del mapa
            mapPane.getChildren().retainAll(imageViewMap);

            // 1. Dibujar el grafo completo
            drawGraph();

            // 2. Simular todos los vuelos
            List<Flight> flights = flightManager.getFlights().toTypedList();
            for (Flight flight : flights) {
                simulateFlightProcess(flight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void simulateFlightProcess(Flight flight)
            throws ListException, QueueException, GraphException {

        boardingSimulator.boardPassengers(flight);

        Airport origin = flight.getOrigin();
        Airport destination = flight.getDestination();
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

        if (path.size() < 2) return;

        for (Point2D p : path) {
            Circle dot = new Circle(p.getX() + 20, p.getY() + 20, 4, Color.RED);
            mapPane.getChildren().add(dot);
        }

        FlightRadar radar = new FlightRadar(origin.getCode() + "->" + destination.getCode(), path);
        animateFlight(radar);

        System.out.println("Vuelo " + flight.getFlightID() + " completado. Pasajeros a bordo: " +
                flight.getOccupancy() + "/" + flight.getCapacity());
    }

    private void drawGraph() throws Exception {
        List<Route> routeList = routeManager.getRoutes().toTypedList();
        List<Airport> airportList = airportManager.getAirports().toTypedList();

        mapPane.getChildren().removeIf(n -> n instanceof Circle || n instanceof Line);

// Vuelve a agregar la imagen si se ha eliminado accidentalmente
        if (!mapPane.getChildren().contains(imageViewMap)) {
            mapPane.getChildren().add(0, imageViewMap);
        }

        // Nodos
        for (Airport airport : airportList) {
            double x = airport.getMapX();
            double y = airport.getMapY();

            Circle node = new Circle(x + 20, y + 20, 4, Color.DODGERBLUE);
            Text label = new Text(x + 24, y + 24, airport.getCode());
            label.setStyle("-fx-font-size: 10px; -fx-fill: white;");

            mapPane.getChildren().addAll(node, label);
        }

        // Aristas
        for (Route route : routeList) {
            Airport origin = findAirportByCode(route.getOrigin_airport_id());
            Airport dest = findAirportByCode(route.getDestination_airport_id());

            if (origin != null && dest != null) {
                double startX = origin.getMapX() + 20;
                double startY = origin.getMapY() + 20;
                double endX = dest.getMapX() + 20;
                double endY = dest.getMapY() + 20;

                Line edge = new Line(startX, startY, endX, endY);
                edge.setStroke(Color.BLACK);
                edge.setStrokeWidth(1);
                mapPane.getChildren().add(edge);
            }
        }
    }

    public void animateFlight(FlightRadar flight) {
        List<Point2D> path = flight.getPath();
        if (path.size() < 2) return;

        ImageView plane = new ImageView(new Image(getClass().getResource("/images/avion.png").toExternalForm()));
        plane.setFitWidth(40);
        plane.setFitHeight(40);
        plane.setLayoutX(path.get(0).getX());
        plane.setLayoutY(path.get(0).getY());
        mapPane.getChildren().add(plane);

        Line trail = new Line(path.get(0).getX() + 20, path.get(0).getY() + 20,
                path.get(0).getX() + 20, path.get(0).getY() + 20);
        trail.setStroke(Color.RED);
        mapPane.getChildren().add(trail);

        Timeline timeline = new Timeline();
        for (int i = 1; i < path.size(); i++) {
            Point2D point = path.get(i);
            KeyFrame frame = new KeyFrame(Duration.seconds(i * 0.5), e -> {
                plane.setLayoutX(point.getX());
                plane.setLayoutY(point.getY());
                trail.setEndX(point.getX() + 20);
                trail.setEndY(point.getY() + 20);
            });
            timeline.getKeyFrames().add(frame);
        }
        timeline.play();
    }

    private Airport findAirportByCode(String code) throws ListException {
        List<Airport> airports = airportManager.getAirports().toTypedList();
        for (Airport a : airports) {
            if (a.getCode().equals(code)) return a;
        }
        return null;
    }

    @FXML
    public void backOnAction(ActionEvent event) {
        // volver al menú o vista anterior
    }
}
