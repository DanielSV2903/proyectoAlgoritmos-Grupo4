package ucr.lab.proyectoayed.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ucr.lab.proyectoayed.model.User;
import ucr.lab.proyectoayed.model.UserManager;

public class LoginController {

    @FXML
    private TextField userTextField; // Aquí va el email

    @FXML
    private TextField passwordTextField;

    @FXML
    private ComboBox<String> rolComboBox;

    private final UserManager userManager = new UserManager();

    @FXML
    public void initialize() {
        rolComboBox.getItems().addAll("admin", "user");
        userManager.loadUsers();
    }

    @FXML
    public void logInOnAction(ActionEvent actionEvent) {
        String email = userTextField.getText().trim();
        String password = passwordTextField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Campos vacíos", "Debe completar todos los campos.");
            return;
        }

        User user = userManager.validateLogin(email, password);

        if (user != null) {
            showAlert("Bienvenido", "Hola, " + user.getName() + " (" + user.getRole() + ")");
        } else {
            showAlert("Error", "Credenciales inválidas.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
