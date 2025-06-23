package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.User;
import model.datamanagment.UserManager;
import model.tda.ListException;
import util.Utility;

public class RegisterController
{
    @javafx.fxml.FXML
    private TextField tfPassword;
    @javafx.fxml.FXML
    private TextField tfUserName;
    @javafx.fxml.FXML
    private TextField tfEmail;
    @javafx.fxml.FXML
    private BorderPane bp;
    private UserManager userManager;

    @javafx.fxml.FXML
    public void initialize() {
        userManager=new UserManager();
        userManager.loadUsers();
    }

    @javafx.fxml.FXML
    public void registerOnAction(ActionEvent actionEvent) {
        String pass = tfPassword.getText().trim();
        String userName = tfUserName.getText().trim();
        String email = tfEmail.getText().trim();

        if (pass.isEmpty() || userName.isEmpty() || email.isEmpty()){
            showAlert("Campos vacíos", "Debe completar todos los campos.");
            return;
        }

        String hashedPassword = Utility.hashPassword(pass);
        User user = new User(userName, hashedPassword, email);

        try {
            userManager.addUser(user);
            userManager.saveUsers();
            showAlert("Registro exitoso", "El usuario ha sido registrado correctamente.");
            clearFields();
        } catch (ListException e) {
            e.printStackTrace();
            showAlert("Error", "Hubo un problema al registrar el usuario.");
        }
    }

    private void clearFields() {
        tfEmail.clear();
        tfPassword.clear();
        tfUserName.clear();
    }

    @javafx.fxml.FXML
    public void cancelOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}