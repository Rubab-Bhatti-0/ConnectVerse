package com.example.connectverseproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class myprofile {
    public void initialize() {
        myProfile();
    }
    @FXML
    private Label profileuser;
    @FXML
    private Label Lpost;
    @FXML
    public void backHandler(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/connectverseproject/home.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private ListView<Node> myprofile;
    @FXML
    private Label lfolowing;
    @FXML
    private Label tfollowers;
    @FXML
    private Label about;
@FXML
public void myProfile() {
    user currentUser = AppData.getCurrentUser();
    profileuser.setText(currentUser.getName());
    int totalfollowers=AppData.graph.get(currentUser.getId()).size();
    lfolowing.setText(""+totalfollowers);
    Lpost.setText(String.valueOf(currentUser.getPosts().size()));
    tfollowers.setText(""+AppData.totalfollowers(currentUser.getId(),currentUser.getName()));
    myprofile.getItems().clear(); // Clear existing posts first
    String abt=currentUser.getAbout();
    about.setText(abt);
    for (post p : currentUser.getPosts()) {
        VBox postBox = new VBox(5);
        postBox.setPadding(new Insets(10));
        postBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #fefefe; -fx-background-radius: 5;");
        postBox.setMaxWidth(300);

        // Post content (auto-wraps)
        Label contentLabel = new Label(p.getContent() != null ? p.getContent() : "");
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #555;");
        contentLabel.setMaxWidth(270); // Adjust width if needed

        // Post date/time
        Label dateTimeLabel = new Label(p.getDatetime() != null ? p.getDatetime().toString() : "");
        dateTimeLabel.setStyle("-fx-font-size: 11; -fx-text-fill: gray;");
        HBox timeBox = new HBox(dateTimeLabel);
        timeBox.setAlignment(Pos.CENTER_RIGHT);

        postBox.getChildren().addAll(contentLabel, timeBox);

        myprofile.getItems().add(postBox);
    }
}

}

