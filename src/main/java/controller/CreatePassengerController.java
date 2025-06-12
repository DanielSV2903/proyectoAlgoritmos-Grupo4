package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.Passenger;
import model.tda.TreeException;

import java.util.Optional;

public class CreatePassengerController
{
    private PassengerManagerController passengerManagerController;
    @javafx.fxml.FXML
    private TextField nameTF;
    @javafx.fxml.FXML
    private TextField tfID;
    @javafx.fxml.FXML
    private BorderPane bp;
    @javafx.fxml.FXML
    private TextField nationalityTF;

    public void setPassengerManagerController(PassengerManagerController passengerManagerController) {
        this.passengerManagerController = passengerManagerController;
    }

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void cancelOnAction(ActionEvent actionEvent) {
        this.nameTF.clear();
        this.tfID.clear();
        this.nationalityTF.clear();
    }

    @javafx.fxml.FXML
    public void registerPassengerOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(bp.getScene().getWindow());
        alert.setTitle("Registrar Pasajero");
        alert.setHeaderText(null);
        if (validarEntradas()){
        String nationality=nationalityTF.getText();
        int passengerID= Integer.parseInt(tfID.getText());
        String passengerName=nameTF.getText();
        Passenger passenger=new Passenger(passengerID,passengerName,nationality);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                try {
                    passengerManagerController.registerPassenger(passenger);
                    passengerManagerController.updateTV();
                } catch (TreeException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Llene todos los datos");
            alert.showAndWait();
        }
    }
    private boolean validarEntradas(){
        return !this.nameTF.getText().isEmpty() || !this.tfID.getText().isEmpty() || !this.nationalityTF.getText().isEmpty();
    }
}