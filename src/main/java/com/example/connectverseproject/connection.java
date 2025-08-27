


package com.example.connectverseproject;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.text.Font;
import static com.example.connectverseproject.AppData.graph;
public class connection {
    @FXML
    private ImageView Search;
    @FXML
    private ListView<HBox> suggestionList;

    @FXML
    public void initialize() {
        // Load the image from resources

        Image setting = new Image(getClass().getResource("/photos/search.jpg").toExternalForm());
        Search.setImage(setting);
        suggestion();
    }

    @FXML
    public void addfriend() {
        user currentUser = AppData.getCurrentUser(); // Logged-in user
        String currentUsername = currentUser.getName();
        ArrayList<Edge> edges = graph.get(currentUser.getId());

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add Friend");
        dialog.setHeaderText("Search and Add a Friend");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefWidth(400);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField friendField = new TextField();
        friendField.setPromptText("Enter friend's name");

        VBox contentBox = new VBox(10, new Label("Friend's Name:"), friendField);
        contentBox.setPadding(new Insets(15));
        dialogPane.setContent(contentBox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String inputName = friendField.getText().trim();

                if (inputName.isEmpty()) {
                    AppData.showAlert(Alert.AlertType.ERROR, "Validation Error", "Friend name cannot be empty.");
                    return null;
                }

                if (inputName.equalsIgnoreCase(currentUsername)) {
                    AppData.showAlert(Alert.AlertType.ERROR, "Validation Error", "You cannot add yourself as a friend.");
                    return null;
                }

                // Check if already a friend
                for (Edge edge : edges) {
                    if (edge.dest.getName().equalsIgnoreCase(inputName)) {
                        AppData.showAlert(Alert.AlertType.ERROR, "Already a Friend", inputName + " is already your friend.");
                        return null;
                    }
                }

                // Search user to add
                user friendUser = null;
                for (user u : AppData.allUsers.values()) {
                    if (u.getName().equalsIgnoreCase(inputName)) {
                        friendUser = u;
                        break;
                    }
                }

                if (friendUser == null) {
                    AppData.showAlert(Alert.AlertType.ERROR, "User Not Found", "No user found with name: " + inputName);
                    return null;
                }

                // Add to graph and database
                AppData.creategraph(currentUser.getId(), friendUser.getId()); // Add edge
                addFriendToDatabase(currentUser.getId(), friendUser.getId()); // Insert into DB
                following();
                AppData.showAlert(Alert.AlertType.INFORMATION, "Success", inputName + " added to your friends list.");
            }
            return null;
        });

        dialog.showAndWait();
    }


    private void addFriendToDatabase(int userId, int friendId) {

        try (Connection conn = dbconnection.getConnection()) {
            String insert = "INSERT INTO friends (userid, friendid) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, friendId);
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            AppData.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add friend to database.");
        }
    }


    @FXML
    public void backHandler(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/connectverseproject/home.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    private Button followingb;
    @FXML
    private Button followersb;
    @FXML
    private Button suggestionB;

    @FXML
    public void following() {
        followingb.setTextFill(Color.WHITE);
        followingb.setStyle("-fx-background-color: #1976D2;");
        followersb.setStyle("-fx-background-color: #white;");
        followersb.setStyle("-fx-text-fill: #1976D2;");
        suggestionB.setStyle("-fx-background-color: #white;");
        suggestionB.setStyle("-fx-text-fill: #1976D2;");
        displayFollowing();
    }


    @FXML
    public void displayFollowing() {
        suggestionList.getItems().clear();
        user currentUser = AppData.getCurrentUser();
        ArrayList<Edge> edges = graph.get(currentUser.getId());

        if (edges == null || edges.isEmpty()) {
            HBox emptyRow = new HBox(new Label("No following available."));
            emptyRow.setPadding(new Insets(10));
            suggestionList.getItems().add(emptyRow);
            return;
        }

        for (Edge edge : edges) {
            user friend = edge.dest;

            // Main HBox for each row
            HBox row = new HBox(10);
            row.setPadding(new Insets(8));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #e0e0e0;");

            // Name label
            Label nameLabel = new Label(friend.getName());
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            nameLabel.setStyle(" -fx-text-fill: #5e35b1;");
            // Spacer
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Unfollow button
            Button removeButton = new Button("Unfollow");
            removeButton.setStyle("-fx-background-color: #E53935; -fx-text-fill: white;");
            removeButton.setOnAction(e -> {
                AppData.endfriendship(currentUser.getId(), friend.getName(), currentUser.getName());
                removeFriendFromDatabase(currentUser.getId(), friend.getId());
                AppData.showAlert(Alert.AlertType.INFORMATION, "Friend Removed", friend.getName() + " has been unfollowed.");
                displayFollowing(); // Refresh
            });

            // Combine
            row.getChildren().addAll(nameLabel, spacer, removeButton);
            suggestionList.getItems().add(row);
        }
    }

    private void removeFriendFromDatabase(int userId, int friendId) {
        String deleteQuery = "DELETE FROM friends WHERE userid = ? AND friendid = ?";

        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            AppData.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to remove friend from database.");
        }
    }




    @FXML
    public void Followers() {
        // Style buttons to indicate active tab
        followersb.setTextFill(Color.WHITE);
        followersb.setStyle("-fx-background-color: #1976D2;");
        followingb.setStyle("-fx-background-color: white; -fx-text-fill: #1976D2;");
        suggestionB.setStyle("-fx-background-color: white; -fx-text-fill: #1976D2;");

        suggestionList.getItems().clear();
        user currentUser = AppData.getCurrentUser();
        boolean hasFollowers = false;

        for (int i = 0; i < AppData.graph.size(); i++) {
            ArrayList<Edge> edges = AppData.graph.get(i);
            if (edges == null || edges.isEmpty()) continue;

            for (Edge edge : edges) {
                if (edge.dest.getId() == currentUser.getId()) {
                    hasFollowers = true;
                    user follower = edge.src;

                    Label nameLabel = new Label(follower.getName());
                    nameLabel.setPrefWidth(200);
                    nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    nameLabel.setStyle(" -fx-text-fill: #5e35b1;");
                    HBox row = new HBox(10);
                    row.setPadding(new Insets(8));

                    row.getChildren().add(nameLabel);
                    row.setStyle("-fx-border-color:#1976D2; -fx-background-color: white;");
                    // Check if current user already follows this follower
                    boolean alreadyFollowing = false;
                    ArrayList<Edge> myEdges = AppData.graph.get(currentUser.getId());
                    if (myEdges != null) {
                        for (Edge myEdge : myEdges) {
                            if (myEdge.dest.getId() == follower.getId()) {
                                alreadyFollowing = true;
                                break;
                            }
                        }
                    }

                    if (!alreadyFollowing) {
                        Button followBackBtn = new Button("Follow Back");
                        followBackBtn.setStyle("-fx-background-color: #6F42C1; -fx-text-fill: #FFFFFF;");
                        followBackBtn.setOnAction(e -> {
                            AppData.creategraph(currentUser.getId(), follower.getId());
                            addFriendToDatabase(currentUser.getId(), follower.getId());
                            AppData.showAlert(Alert.AlertType.INFORMATION, "Friend Added", follower.getName() + " has been added to your following.");
                            followBackBtn.setDisable(true);
                            followBackBtn.setText("Followed");
                            followBackBtn.setStyle("-fx-background-color: #28A745; -fx-text-fill: #FFFFFF;");

                        });
                        row.getChildren().add(followBackBtn);
                    } else {
                        Label followedLabel = new Label("Followed");
                        followedLabel.setStyle("-fx-background-color: #28A745; -fx-text-fill: #FFFFFF;");
                        row.getChildren().add(followedLabel);
                    }

                    suggestionList.getItems().add(row);
                }
            }
        }

        if (!hasFollowers) {
            HBox emptyRow = new HBox(new Label("No followers available."));
            emptyRow.setPadding(new Insets(10));
            suggestionList.getItems().add(emptyRow);
        }
    }


    @FXML
    public void suggestion() {
        suggestionB.setTextFill(Color.WHITE);
        suggestionB.setStyle("-fx-background-color: #1976D2;");
        followersb.setStyle("-fx-background-color: white; -fx-text-fill: #1976D2;");
        followingb.setStyle("-fx-background-color: white; -fx-text-fill: #1976D2;");

        user currentUser = AppData.getCurrentUser();
        suggestionList.getItems().clear();

        Set<Integer> alreadyFriends = new HashSet<>();
        alreadyFriends.add(currentUser.getId());

        ArrayList<Edge> currentEdges = AppData.graph.get(currentUser.getId());

        if (currentEdges != null) {
            for (Edge edge : currentEdges) {
                alreadyFriends.add(edge.dest.getId());
            }
        }

        Set<Integer> suggestedIds = new HashSet<>();

        // If user is new (no edges in graph)
        if (currentEdges == null || currentEdges.isEmpty()) {
            for (user u : AppData.allUsers.values()) {
                if (u.getId() != currentUser.getId()) {
                    HBox row = buildSuggestionRow(currentUser, u);
                    suggestionList.getItems().add(row);
                }
            }


        }

        //  Regular suggestion logic for users with friends
        for (Edge edge : currentEdges) {
            user friend = edge.dest;
            ArrayList<Edge> friendEdges = AppData.graph.get(friend.getId());

            if (friendEdges != null) {
                for (Edge friendEdge : friendEdges) {
                    user suggestedUser = friendEdge.dest;

                    if (!alreadyFriends.contains(suggestedUser.getId()) &&
                            !suggestedIds.contains(suggestedUser.getId())) {

                        suggestedIds.add(suggestedUser.getId());

                        HBox row = buildSuggestionRow(currentUser, suggestedUser);
                        suggestionList.getItems().add(row);
                    }
                }
            }
        }

        if (suggestionList.getItems().isEmpty()) {
            HBox emptyRow = new HBox(new Label("No suggestions available."));
            emptyRow.setPadding(new Insets(10));
            suggestionList.getItems().add(emptyRow);
        }
    }
    private HBox buildSuggestionRow(user currentUser, user suggestedUser) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(5));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color:#1976D2; -fx-background-color: white;");
        VBox infoBox = new VBox(3);
        Label nameLabel = new Label(suggestedUser.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setStyle(" -fx-text-fill: #5e35b1;");

        ArrayList<String> followedByNames = getFollowedByNames(currentUser, suggestedUser);
        Label followedByLabel = new Label();

        if (!followedByNames.isEmpty()) {
            StringBuilder sb = new StringBuilder("Followed by ");
            int showLimit = Math.min(2, followedByNames.size());
            for (int i = 0; i < showLimit; i++) {
                sb.append(followedByNames.get(i));
                if (i < showLimit - 1) sb.append(", ");
            }
            if (followedByNames.size() > 2) {
                sb.append(" +").append(followedByNames.size() - 2).append(" more");
            }

            followedByLabel.setText(sb.toString());
            followedByLabel.setFont(Font.font("Arial", 11));
            followedByLabel.setTextFill(Color.GRAY);
            followedByLabel.setCursor(Cursor.HAND);

            followedByLabel.setOnMouseClicked(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Followed by");
                alert.setHeaderText("People you follow who also follow " + suggestedUser.getName());
                alert.setContentText(String.join("\n", followedByNames));
                alert.showAndWait();
            });
        }

        // New label under each suggestion
        Label suggestedLabel = new Label("Suggested for you");
        suggestedLabel.setFont(Font.font("Arial", 10));
        suggestedLabel.setTextFill(Color.LIGHTGRAY);

        // Add all labels to info box
        infoBox.getChildren().addAll(nameLabel, followedByLabel, suggestedLabel);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            AppData.creategraph(currentUser.getId(), suggestedUser.getId());
            addFriendToDatabase(currentUser.getId(), suggestedUser.getId());
            AppData.showAlert(Alert.AlertType.INFORMATION, "Friend Added", suggestedUser.getName() + " is now your friend.");
            suggestion(); // Refresh the list
        });

        addButton.setStyle("-fx-background-color: #1DA1F2; -fx-text-fill: #FFFFFF;");
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> suggestionList.getItems().remove(row));
        cancelButton.setStyle("-fx-background-color: #6C757D; -fx-text-fill: #FFFFFF;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonBox = new HBox(5, addButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        row.getChildren().addAll(infoBox, spacer, buttonBox);
        return row;
    }




    private ArrayList<String> getFollowedByNames(user currentUser, user suggestedUser) {
        ArrayList<String> names = new ArrayList<>();

        ArrayList<Edge> currentUserEdges = AppData.graph.get(currentUser.getId());
        if (currentUserEdges == null) return names;

        Set<Integer> following = new HashSet<>();
        for (Edge edge : currentUserEdges) {
            following.add(edge.dest.getId());
        }

        for (Integer id : following) {
            ArrayList<Edge> edges = AppData.graph.get(id);
            if (edges != null) {
                for (Edge e : edges) {
                    if (e.dest.getId() == suggestedUser.getId()) {
                        user follower = AppData.allUsers.get(id);
                        if (follower != null) names.add(follower.getName());
                    }
                }
            }
        }

        return names;
    }

}







