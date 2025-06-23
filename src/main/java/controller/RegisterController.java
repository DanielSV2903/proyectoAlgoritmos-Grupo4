package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.User;
import model.datamanagment.UserManager;
import model.tda.ListException;

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
    }

    @javafx.fxml.FXML
    public void registerOnAction(ActionEvent actionEvent) {
        String pass = tfPassword.getText().trim();
        String userName = tfUserName.getText().trim();
        String email = tfEmail.getText().trim();
        User user=new User(userName,pass,email);
        try {
            userManager.addUser(user);
        } catch (ListException e) {
            throw new RuntimeException(e);
        }

    }

    @javafx.fxml.FXML
    public void cancelOnAction(ActionEvent actionEvent) {
        tfEmail.clear();
        tfPassword.clear();
        tfUserName.clear();
    }
}