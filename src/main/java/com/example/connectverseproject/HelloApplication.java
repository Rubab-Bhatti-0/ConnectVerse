package com.example.connectverseproject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DataLoader.loadAllData();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("logo.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 356, 532);
        stage.setTitle("ConnectVerse");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}