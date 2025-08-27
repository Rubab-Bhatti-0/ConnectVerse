package com.example.connectverseproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.io.IOException;


public class HelloController {
    @FXML
    private ImageView imageview;

    @FXML
    private ImageView pass_icon;


    @FXML
    public void initialize() {
        // Load the image from resources
        Image logo = new Image(getClass().getResource("/photos/bk.png").toExternalForm());
        imageview.setImage(logo);

        Image pass_show = new Image(getClass().getResource("/photos/visual.png").toExternalForm());
        pass_icon.setImage(pass_show);

    }
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordField;



    private boolean isPasswordVisible = false;

    @FXML
    public void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordField.setText(visiblePasswordField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
        } else {
            // Show password
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
        }
        isPasswordVisible = !isPasswordVisible;
    }

    @FXML
    private TextField usernameField;
    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = isPasswordVisible ? visiblePasswordField.getText() : passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            AppData.showAlert(Alert.AlertType.ERROR,"Login Failed", "⚠️ Please enter username and password.");
            return;
        }

        // Load data only once (if not already loaded)

            DataLoader.loadAllData();


        user matchedUser = null;

        for (user u : AppData.allUsers.values()) {
            if (u.getName().equals(username)) {
                matchedUser = u;
                break;
            }
        }

        if (matchedUser == null) {
            AppData.showAlert(Alert.AlertType.ERROR,"Login Failed", "❌ Username not found.");
        } else if (!matchedUser.getPassword().equals(password)) {
            AppData.showAlert(Alert.AlertType.ERROR,"Login Failed", "❌ Incorrect password.");
        } else {
            AppData.setCurrentUser(matchedUser);
            AppData.showAlert(Alert.AlertType.INFORMATION,"Login Success", "✅ Login successful.");
            switchToHome(event);
        }
    }


    public void switchToSignup(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/connectverseproject/signup.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void switchToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/connectverseproject/home.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


}
