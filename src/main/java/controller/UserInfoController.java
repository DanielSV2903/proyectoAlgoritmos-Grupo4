package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.Passenger;
import model.User;
import model.tda.ListException;

public class UserInfoController
{
    @javafx.fxml.FXML
    private Label label;
    @javafx.fxml.FXML
    private BorderPane bp;
    @javafx.fxml.FXML
    private TextField nameTF;
    @javafx.fxml.FXML
    private TextField tfID;
    @javafx.fxml.FXML
    private TextField nationalityTF;
    private Passenger passenger;

    @javafx.fxml.FXML
    public void initialize() {
        passenger = LoginController.getCurrentUser().getPassenger();
        this.nameTF.setText(passenger.getName());
        this.tfID.setText(String.valueOf(passenger.getId()));
        this.nationalityTF.setText(passenger.getNationality());
    }

    @javafx.fxml.FXML
    public void saveOnAction(ActionEvent actionEvent) {
        String name = nameTF.getText();
        int id = Integer.parseInt(tfID.getText());
        String nationality = nationalityTF.getText();
        passenger.setName(name);
        passenger.setId(id);
        passenger.setNationality(nationality);
        passenger.setPriority(3);
        try {
            LoginController.setUserPassenger(passenger);
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }
}