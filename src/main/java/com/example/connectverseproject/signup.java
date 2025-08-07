
package com.example.connectverseproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.regex.Pattern;

public class signup {

    @FXML
    private ImageView signupimage;

    @FXML
    private ImageView pass_icon_sign;

    @FXML
    private TextField pas_feild;

    @FXML
    private Button ps_show_signup;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailfeild;

    @FXML
    private PasswordField passwordField;

    private boolean isPasswordVisible = false;

    @FXML
    public void initialize() {
        try {
            Image logo = new Image(getClass().getResource("/photos/bk.png").toExternalForm());
            signupimage.setImage(logo);
        } catch (Exception e) {
            System.out.println("Logo not found: " + e.getMessage());
        }

        try {
            Image pass_show = new Image(getClass().getResource("/photos/visual.png").toExternalForm());
            pass_icon_sign.setImage(pass_show);
        } catch (Exception e) {
            System.out.println("Pass icon not found: " + e.getMessage());
        }
    }

    @FXML
    public void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordField.setText(pas_feild.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            pas_feild.setVisible(false);
            pas_feild.setManaged(false);
        } else {
            // Show password
            pas_feild.setText(passwordField.getText());
            pas_feild.setVisible(true);
            pas_feild.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
        }
        isPasswordVisible = !isPasswordVisible;
    }

    @FXML
    public void signupAndReturn(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String email = emailfeild.getText().trim();
        String password = isPasswordVisible ? pas_feild.getText().trim() : passwordField.getText().trim();
        String about = "";

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("⚠️ Please fill in all fields.");
            return;
        }

        for (user u : AppData.allUsers.values()) {
            if (u.getName().equalsIgnoreCase(username)) {
                showAlert("⚠️ Username already exists. Try another.");
                return;
            }
            if (u.getPassword().equals(password)) {
                showAlert("❌ Password already in use. Choose a different one.");
                return;
            }
        }

        if (!isValidPassword(password)) {
            showAlert("⚠️ Password must be at least 8 characters, include a number, and a special character.");
            return;
        }

        try (Connection conn = dbconnection.getConnection()) {
            String query = "INSERT INTO signup (username, email, password, About) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, about);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                showAlert("✅ Signup successful!");
                goToLogin(event);
            } else {
                showAlert("❌ Signup failed.");
            }

        } catch (Exception e) {
            showAlert("❌ Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isValidPassword(String password) {
        boolean hasLength = password.length() >= 8;
        boolean hasSpecial = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        return hasLength && hasSpecial && hasDigit;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Signup Status");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void goToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/connectverseproject/hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
