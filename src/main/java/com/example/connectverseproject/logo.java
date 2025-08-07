
package com.example.connectverseproject;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class logo {
    @FXML
    private ImageView photo;


    public void initialize() {
        // Load images
        Image logo = new Image(getClass().getResource("/photos/bk.png").toExternalForm());
        photo.setImage(logo);

        // Delay and switch to login screen
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/connectverseproject/hello-view.fxml"));
                Stage stage = (Stage) photo.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        delay.play();
    }
}
