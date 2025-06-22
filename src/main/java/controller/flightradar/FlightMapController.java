package controller.flightradar;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import controller.flightradar.FlightRadar;


import java.util.List;

public class FlightMapController {

    @FXML
    private Pane mapPane;

    public void animateFlight(FlightRadar flight) {
        List<Point2D> path = flight.getPath();
        if (path.size() < 2) return;

        ImageView plane = new ImageView(new Image("/resources/images/plane.png"));
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
            int index = i;
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
}