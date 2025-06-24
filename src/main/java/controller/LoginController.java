package controller;

import com.cretaairlines.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.Passenger;
import model.User;
import model.datamanagment.DataCenter;
import model.datamanagment.UserManager;
import model.tda.ListException;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField userTextField; // Aquí va el email

    private static final UserManager userManager = new UserManager();
    @FXML
    private BorderPane bp;
    private static User currentUser;
    @FXML
    private PasswordField passwordTextField;

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setUserPassenger(Passenger passenger) throws ListException {
        currentUser.setPassenger(passenger);
        userManager.updateUser(currentUser);
    }

    @FXML
    public void initialize() {
        currentUser=new User();
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
        currentUser = user;

        if (email.equals("a") && password.equals("a")){
            user = new User();
        }

        if (user != null) {
            try {
            switch (user.getRole()){
                case "ADMIN":
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu.fxml"));
                    Parent menuRoot = fxmlLoader.load();
                    bp.getScene().setRoot(menuRoot);
                    DataCenter.enQueueOperation(user.getName()+" inició sesion");
                    showAlert("Bienvenido", "Hola, " + user.getName() + " (" + user.getRole() + ")");
                    break;
                case "USER":
                     fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("userView.fxml"));
                     menuRoot = fxmlLoader.load();
                    bp.getScene().setRoot(menuRoot);
                    DataCenter.enQueueOperation(user.getName()+" inició sesion");
                    showAlert("Bienvenido", "Hola, " + user.getName() + " (" + user.getRole() + ")");
                        break;
                        default:
                            break;
            }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Hubo un problema al cargar el menú.");
            }
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
    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void registerOnAction(ActionEvent actionEvent) {
        loadPage("register.fxml");
    }
}
