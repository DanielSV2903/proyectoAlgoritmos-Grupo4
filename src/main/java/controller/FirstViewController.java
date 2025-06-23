package controller;

import com.cretaairlines.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class FirstViewController {
    @javafx.fxml.FXML
    private BorderPane bp;

    @javafx.fxml.FXML
    public void registerOnAction(ActionEvent actionEvent) {
        loadPage("register.fxml");
    }

    @javafx.fxml.FXML
    public void logInOnAction(ActionEvent actionEvent) {
        loadPage("login.fxml");
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
