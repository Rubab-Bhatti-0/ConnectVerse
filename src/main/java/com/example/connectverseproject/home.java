
package com.example.connectverseproject;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class home {

    @FXML
    private ImageView homeimage;
    @FXML
    private ImageView HomeIcon;
    @FXML
    private ImageView Connections;
    @FXML
    private ImageView Add;
    @FXML
    private ImageView MyProfile;
    @FXML
    private ImageView Setting;
    @FXML
    private VBox homeVbox;
    @FXML
    private VBox feedbox;
    @FXML
    private ChoiceBox<String> feedchoicebox;

    @FXML
    private Button homeButton;

    @FXML
    private Button connectionButton;

    @FXML
    private Button addPostButton;

    @FXML
    private Button myProfileButton;

    @FXML
    private Button settingsButton;

    public void initialize() {
        showFeed();
        // Load the image from resources
        Image logo = new Image(getClass().getResource("/photos/248918.png").toExternalForm());
        homeimage.setImage(logo);


        Image home = new Image(getClass().getResource("/photos/home1.png").toExternalForm());
        HomeIcon.setImage(home);


        Image connection = new Image(getClass().getResource("/photos/people.png").toExternalForm());
        Connections.setImage(connection);


        Image addpost = new Image(getClass().getResource("/photos/addp.png").toExternalForm());
        Add.setImage(addpost);


        Image profile = new Image(getClass().getResource("/photos/images.jpeg").toExternalForm());
        MyProfile.setImage(profile);


        Image setting = new Image(getClass().getResource("/photos/settings.png").toExternalForm());
        Setting.setImage(setting);

        // Apply tooltips to buttons
        addTooltip(homeButton,        "Home");
        addTooltip(connectionButton,  "Connections");
        addTooltip(addPostButton,     "Add Post");
        addTooltip(myProfileButton,   "My Profile");
        addTooltip(settingsButton,    "Settings");
        feedchoicebox.setItems(FXCollections.observableArrayList("All","Education", "Entertainment", "Sports","Others"));
        feedchoicebox.setValue("All");
        personalizedFeed("All");
        feedchoicebox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {

            String selectedInterest = feedchoicebox.getValue().trim();
            personalizedFeed(selectedInterest);
        });


    }

    private void addTooltip(Node node, String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setStyle(
                "-fx-font-size: 14px; " + "-fx-text-fill: white; " + "-fx-background-color: #1976D2; " + "-fx-padding: 5px;");
        tooltip.setShowDelay(Duration.millis(200));
        Tooltip.install(node, tooltip);
    }
    // Handle new post creation
    @FXML
    private void new_posting() {
        showFeed();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Post");
        dialog.setHeaderText("Enter post content and select category");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().clear();

        ButtonType postButton = new ButtonType("Post", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(postButton, cancelButton);

        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Enter your post here...");
        contentArea.setWrapText(true);

        ChoiceBox<String> categoryBox = new ChoiceBox<>(FXCollections.observableArrayList("Education", "Entertainment", "Sports","Others"));
        categoryBox.setValue("Education");

        VBox vbox = new VBox(10, new Label("Post Content:"), contentArea, new Label("Select Category:"), categoryBox);
        vbox.setPadding(new Insets(10));
        dialogPane.setContent(vbox);

        dialog.setResultConverter(button -> {
            if (button == postButton) {
                String content = contentArea.getText().trim();
                String category = categoryBox.getValue();

                if (content.isEmpty()) {
                    AppData.showAlert(Alert.AlertType.ERROR, "Validation Error", "Post content cannot be empty.");
                    return null;
                }
                LocalDateTime currentdate=LocalDateTime.now();
                String timestamp = currentdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                try (Connection conn = dbconnection.getConnection()) {
                    String sql = "INSERT INTO posts (userid, content,interest,timestamp ,likes) VALUES (?, ?, ?, ?,?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, AppData.getCurrentUser().getId());
                    stmt.setString(2, content);
                    stmt.setString(3, category);
                    stmt.setString(4, timestamp);
                    stmt.setInt(5, 0);

                    stmt.executeUpdate();

                     //Update current user's post list in memory
                    post newPost = new post(content, category, currentdate);
                    AppData.getCurrentUser().addPost(newPost);

                    AppData.showAlert(Alert.AlertType.INFORMATION, "Success", "✅ Post submitted successfully.");

                } catch (Exception e) {
                    e.printStackTrace();
                    AppData.showAlert(Alert.AlertType.ERROR, "Database Error", "❌ Failed to save post.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }



    @FXML
    public void switchToConnections(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/connectverseproject/connection.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    public void switchToMyProfile(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/connectverseproject/myprofile.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    public void settings(){
        if(homeVbox.isVisible()){
            homeVbox.setVisible(false);
            showFeed();
        }else
            homeVbox.setVisible(true);
    }

    @FXML
    public void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/connectverseproject/hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void showfeedbox(){
        if(feedbox.isVisible()){
            feedbox.setVisible(false);
        }else
            feedbox.setVisible(true);
    }


    @FXML
    private ListView<Node> listviewhome;




   private void feed(priorityQueue queue){
       user currentUser = AppData.getCurrentUser();
       if (currentUser == null) return;

       ArrayList<Edge> edges = AppData.graph.get(currentUser.getId());
       // PriorityQueue<post> pq = new PriorityQueue<>();
       if (edges == null || edges.isEmpty()) return;

       listviewhome.getItems().clear();

       for (Edge edge : edges) {
           user friend = edge.dest;
           if (friend == null) continue;
           // here changes
           List<post> posts = friend.getPosts();
           for (post p : posts) {
               if (p != null){
                   //   pq.add(p);
                   p.setOwner(friend.getId());
                   queue.feed_by_Priority(p);

               }

           }
       }
   }
    @FXML
    private void showFeed() {
        priorityQueue queue=new priorityQueue();
        feed(queue);
           post p;
            while (!queue.isEmpty()) {
                p=queue.poll();
                if (p == null) continue;

                VBox postBox = new VBox(5);
                postBox.setPadding(new Insets(10));
                postBox.setStyle("-fx-border-color: #ccc; " + "-fx-border-radius: 5; " + "-fx-background-color: #fefefe; " + "-fx-background-radius: 5;");
                postBox.setMaxWidth(300);
                 String name=AppData.allUsers.get(p.getowner()).getName();
                Label friendLabel = new Label(name != null ? name : "Unknown");
                friendLabel.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-font-size: 14px; -fx-text-fill: #1976D2;");

                Label contentLabel = new Label(p.getContent() != null ? p.getContent() : "No content");
                contentLabel.setWrapText(true);
                contentLabel.setMaxWidth(240);
                contentLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

                String posting_dt=DataLoader.postTime(p.getDatetime());
                Label dateTimeLabel = new Label(posting_dt != null ?  posting_dt : "No Time");
                dateTimeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #5e35b1;");
                HBox timeBox = new HBox(dateTimeLabel);
                timeBox.setAlignment(Pos.CENTER_RIGHT);



                ImageView photo = new ImageView(new Image(getClass().getResource("/photos/purpleHeart.png").toExternalForm()));
                photo.setFitHeight(20);
                photo.setFitWidth(20);
                photo.setPreserveRatio(true);
                ImageView whiteHeart = new ImageView(new Image(getClass().getResource("/photos/whiteHeart.jpg").toExternalForm()));
                whiteHeart .setFitHeight(14);
                whiteHeart .setFitWidth(14);
                whiteHeart .setPreserveRatio(true);
                Button likeButton = new Button();
                if (DataLoader.hasUserLiked(p.getowner(), AppData.currentUser.getId())) {
                    likeButton.setGraphic(photo);//
                } else {
                    //likeButton.setText("❤");
                    likeButton.setGraphic(whiteHeart);
                }

                likeButton.setStyle("-fx-background-color: white; " + "-fx-background-radius: 8; " +  "-fx-padding: 6; " +   "-fx-cursor: hand;");


                int likeCount = DataLoader.getLikeCount(p.getowner()); // from DB

                String l=likeCount==1?" like":" likes";
                Label likeCountLabel = new Label(String.valueOf(likeCount)+l);
                likeCountLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #5e35b1;");

                post finalP = p;
                likeButton.setOnAction(e -> {
                    DataLoader.toggleLike(finalP.getowner(), AppData.currentUser.getId());
                    int updatedCount = DataLoader.getLikeCount(finalP.getowner());
                   String k=updatedCount==1?" like":" likes";
                    likeCountLabel.setText(String.valueOf(updatedCount)+k);

                    // Change heart color
                    if (DataLoader.hasUserLiked(finalP.getowner(), AppData.currentUser.getId())) {
                        likeButton.setGraphic(photo);//
                    } else {
                        //likeButton.setText("❤");
                        likeButton.setGraphic(whiteHeart);//
                    }
                });
                // Bottom row → likes left, date right
                HBox bottomRow = new HBox(10, likeButton, likeCountLabel);
                bottomRow.setAlignment(Pos.CENTER_LEFT);
                bottomRow.setSpacing(10);


                HBox likeRow = new HBox(5, likeButton, bottomRow);
                likeRow.setAlignment(Pos.CENTER_LEFT);


                postBox.getChildren().addAll(friendLabel, contentLabel, likeRow,timeBox);
                listviewhome.getItems().add(postBox);

            }
        }


    @FXML
    private void personalizedFeed(String s) {

        priorityQueue queue=new priorityQueue();
        feed(queue);


        if (s.equalsIgnoreCase("All")) {
            showFeed();
            return;
        }


            post p;
        while (!queue.isEmpty()) {
            p=queue.poll();
                if (p == null || p.getInterest() == null) continue;
                if (!p.getInterest().equalsIgnoreCase(s)) continue;

                VBox postBox = new VBox(5);
                postBox.setPadding(new Insets(10));
                postBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #fefefe; -fx-background-radius: 5;");
                postBox.setMaxWidth(300);
            String name=AppData.allUsers.get(p.getowner()).getName();
                Label friendLabel = new Label(name != null ? name: "Unknown");
                friendLabel.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-font-size: 14px; -fx-text-fill: #1976D2;");

                Label contentLabel = new Label(p.getContent() != null ? p.getContent() : "");
                contentLabel.setWrapText(true);
                contentLabel.setMaxWidth(250);
                contentLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

                String posting_dt=DataLoader.postTime(p.getDatetime());
                Label dateTimeLabel = new Label(posting_dt != null ?  posting_dt : "");
                dateTimeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #5e35b1;");
                HBox timeBox = new HBox(dateTimeLabel);
                timeBox.setAlignment(Pos.CENTER_RIGHT);



            ImageView photo = new ImageView(new Image(getClass().getResource("/photos/purpleHeart.png").toExternalForm()));
            photo.setFitHeight(20);
            photo.setFitWidth(20);
            photo.setPreserveRatio(true);
            ImageView whiteHeart = new ImageView(new Image(getClass().getResource("/photos/whiteHeart.jpg").toExternalForm()));
            whiteHeart .setFitHeight(14);
            whiteHeart .setFitWidth(14);
            whiteHeart .setPreserveRatio(true);
            Button likeButton = new Button();
            if (DataLoader.hasUserLiked(p.getowner(), AppData.currentUser.getId())) {
                likeButton.setGraphic(photo);//
            } else {
                likeButton.setGraphic(whiteHeart);
            }
            likeButton.setStyle("-fx-background-color: white; " + "-fx-background-radius: 8; " +  "-fx-padding: 6; " +   "-fx-cursor: hand;");
            int likeCount = DataLoader.getLikeCount(p.getowner()); // from DB
            String l=likeCount==1?" like":" likes";
            Label likeCountLabel = new Label(String.valueOf(likeCount)+l);
            likeCountLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #5e35b1;");
            post finalP = p;
            likeButton.setOnAction(e -> {
                DataLoader.toggleLike(finalP.getowner(), AppData.currentUser.getId());
                // Update count
                int updatedCount = DataLoader.getLikeCount(finalP.getowner());
                String k=updatedCount==1?" like":" likes";
                likeCountLabel.setText(String.valueOf(updatedCount)+k);
                if (DataLoader.hasUserLiked(finalP.getowner(), AppData.currentUser.getId())) {
                    likeButton.setGraphic(photo);//
                } else {
                    likeButton.setGraphic(whiteHeart);
                }
            });
            HBox bottomRow = new HBox(10, likeButton, likeCountLabel);
            bottomRow.setAlignment(Pos.CENTER_LEFT);
            bottomRow.setSpacing(10);


            HBox likeRow = new HBox(5, likeButton, bottomRow);
            likeRow.setAlignment(Pos.CENTER_LEFT);

                postBox.getChildren().addAll(friendLabel, contentLabel, timeBox,likeRow);
                listviewhome.getItems().add(postBox);
            }
        }




    @FXML
    private void handleEditProfile() {
        user currentUser = AppData.getCurrentUser();
        if (currentUser == null) return;

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton editAbout = new RadioButton("Edit About");
        RadioButton editUsername = new RadioButton("Edit Username");
        RadioButton editPassword = new RadioButton("Edit Password");

        editAbout.setToggleGroup(toggleGroup);
        editUsername.setToggleGroup(toggleGroup);
        editPassword.setToggleGroup(toggleGroup);

        VBox radioBox = new VBox(10, editAbout, editUsername, editPassword);

        TextField inputField = new TextField();
        inputField.setVisible(false);

        ButtonType okButtonType = ButtonType.OK;
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                inputField.setVisible(true);
                okButton.setDisable(false);

                if (editPassword.isSelected()) {
                    inputField.setPromptText("Enter current password");
                } else if (editUsername.isSelected()) {
                    inputField.setPromptText("Enter new username");
                } else if (editAbout.isSelected()) {
                    inputField.setPromptText("Enter new about");
                }
            }
        });

        VBox content = new VBox(15, radioBox, inputField);
        content.setPadding(new Insets(25));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                String value = inputField.getText().trim();
                if (value.isEmpty()) return null;

                if (editAbout.isSelected()) {
                    if (updateSingleField("about", value, currentUser.getId())) {
                        currentUser.setAbout(value);
                       AppData.showAlert(Alert.AlertType.INFORMATION, "Updated", "About updated successfully.");
                    }
                }
                else if (editUsername.isSelected()) {
                    if (value.equalsIgnoreCase(currentUser.getName())) {
                        AppData.showAlert(Alert.AlertType.ERROR, "Same Username", "New username must be different.");
                        return null;
                    }
                    if (usernameExists(value)) {
                        AppData.showAlert(Alert.AlertType.ERROR, "Username Exists", "This username is already taken.");
                        handleEditProfile();
                        return null;
                    }
                    if (updateSingleField("username", value, currentUser.getId())) {
                        currentUser.setusername(value);
                        AppData.showAlert(Alert.AlertType.INFORMATION, "Updated", "Username updated successfully.");
                    }
                }
                else if (editPassword.isSelected()) {
                    if (!authenticate(value, currentUser)) {
                        AppData.showAlert(Alert.AlertType.ERROR, "Incorrect Password", "Enter correct current password.");
                        handleEditProfile();
                        return null;
                    }

                    // Ask for new password
                    Dialog<Void> newPassDialog = new Dialog<>();
                    newPassDialog.setTitle("Change Password");

                    TextField newPass = new TextField();
                    newPass.setPromptText("Enter new password");

                    VBox passBox = new VBox(15, newPass);
                    passBox.setPadding(new Insets(25));

                    newPassDialog.getDialogPane().setContent(passBox);
                    newPassDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    newPassDialog.setResultConverter(passBtn -> {
                        if (passBtn == ButtonType.OK) {
                            String newPassword = newPass.getText().trim();
                            if (newPassword.isEmpty()) {
                                AppData.showAlert(Alert.AlertType.ERROR, "Empty Field", "New password cannot be empty.");
                                return null;
                            }
                            if (newPassword.equals(currentUser.getPassword())) {
                                AppData.showAlert(Alert.AlertType.ERROR, "Same Password", "New password must be different.");
                                return null;
                            }

                            if (!isValidPassword(newPassword)) {
                                AppData.showAlert(Alert.AlertType.ERROR, "Invalid Password", "⚠️ Password must be at least 8 characters, include a number, and a special character.");
                                return null;
                            }
                            if (passwordExists(newPassword)) {
                                AppData. showAlert(Alert.AlertType.WARNING, "Password Exists", "This password is already used by another account. Choose a unique one.");
                                handleEditProfile();
                                return null;
                            }

                            if (updateSingleField("password", newPassword, currentUser.getId())) {
                                currentUser.setPassword(newPassword);
                                AppData.showAlert(Alert.AlertType.INFORMATION, "Password Updated", "Password changed successfully.");
                            }
                        }
                        return null;
                    });

                    newPassDialog.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private boolean isValidPassword(String password) {
        boolean hasLength = password.length() >= 8;
        boolean hasSpecial = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        return hasLength && hasSpecial && hasDigit;
    }
    private boolean passwordExists(String password) {
        String sql = "SELECT COUNT(*) FROM signup WHERE password = ?";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


// Helper Methods

    private boolean authenticate(String input, user u) {
        return input.equals(u.getPassword());
    }

    private boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM signup WHERE username = ?";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updateSingleField(String column, String value, int userId) {
        String sql = "UPDATE signup SET " + column + " = ? WHERE idsignup = ?";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, value);
            stmt.setInt(2, userId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            AppData.showAlert(Alert.AlertType.ERROR, "Database Error", "Could not update " + column);
            return false;
        }
    }




}



