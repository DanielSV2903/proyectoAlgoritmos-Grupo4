package controller.flightradar;

import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import model.Airport;
import model.Route;

import model.datamanagment.RouteManager;

import java.util.ArrayList;
import java.util.List;

public class FlightMapController {

    @FXML
    private Pane mapPane;
    @FXML
    private ImageView imageViewMap;
    @FXML
    private Canvas canvasPlane;

    private RouteManager routeManager = new RouteManager();

    @FXML
    public void initialize() {
        imageViewMap.setImage(new Image(getClass().getResource("/images/mapamundi.jpg").toExternalForm()));
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

        Line trail = new Line();
        trail.setStartX(path.get(0).getX() + 20);
        trail.setStartY(path.get(0).getY() + 20);

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

    @FXML
    public void simulateFlightsOnAction(ActionEvent actionEvent) {
        try {
            List<Route> routeList = routeManager.getRoutes().toTypedList();
            List<Airport> airportList = routeManager.getAirportManager().getAirports().toTypedList();

            for (Route route : routeList) {
                Airport originAirport = findAirportByCode(airportList, route.getOrigin_airport_id());
                Airport destAirport = findAirportByCode(airportList, route.getDestination_airport_id());

                if (originAirport != null && destAirport != null) {
                    Point2D start = new Point2D(originAirport.getMapX(), originAirport.getMapY());
                    Point2D end = new Point2D(destAirport.getMapX(), destAirport.getMapY());

                    List<Point2D> path = new ArrayList<>();
                    path.add(start);
                    path.add(end);

                    animateFlight(new FlightRadar(originAirport.getCode() + "->" + destAirport.getCode(), path));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Airport findAirportByCode(List<Airport> airports, String code) {
        for (Airport airport : airports) {
            if (airport.getCode().equals(code)) {
                return airport;
            }
        }
        return null;
    }

    @FXML
    public void backOnAction(ActionEvent actionEvent) {
    }

}
