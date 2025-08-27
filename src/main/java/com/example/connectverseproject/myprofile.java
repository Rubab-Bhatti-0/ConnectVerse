package com.example.connectverseproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        String posting_dt=DataLoader.postTime(p.getDatetime());
        Label dateTimeLabel = new Label(posting_dt != null ?  posting_dt : "");
        dateTimeLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #5e35b1;");
        HBox timeBox = new HBox(dateTimeLabel);
        timeBox.setAlignment(Pos.CENTER_RIGHT);

//        ImageView photo=new ImageView();
//        Image logo = new Image(getClass().getResource("/photos/del.png").toExternalForm());
//        photo.setImage(logo);
//        Button deletepost=new Button();
//        deletepost.
//        deletepost.setText();
//        deletepost.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #E53935; -fx-text-fill: bold white;");
//        HBox delBox = new HBox(deletepost);
//        delBox.setAlignment(Pos.BOTTOM_RIGHT);
//
        ImageView photo = new ImageView(new Image(getClass().getResource("/photos/del.png").toExternalForm()));

        photo.setFitHeight(20);
        photo.setFitWidth(20);
        photo.setPreserveRatio(true);

        Button deletePost = new Button();
        deletePost.setGraphic(photo);  // set image as button graphic

        deletePost.setStyle("-fx-background-color: white; " + "-fx-background-radius: 8; " +  "-fx-padding: 6; " +   "-fx-cursor: hand;");

        deletePost.setOnAction(e -> {
            System.out.println("Delete button clicked!");
        });
        HBox delBox = new HBox(deletePost);
        delBox.setAlignment(Pos.TOP_RIGHT);



        int likeCount = DataLoader.getLikeCount(AppData.getCurrentUser().getId());
        String l=likeCount==1?" like":" likes";// from DB
        Label likeCountLabel = new Label(String.valueOf(likeCount)+l);
        likeCountLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #5e35b1;");
        likeCountLabel.setAlignment(Pos.CENTER_LEFT);

        postBox.getChildren().addAll(contentLabel, timeBox,likeCountLabel,delBox);

        myprofile.getItems().add(postBox);
    }
}

}

