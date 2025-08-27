
package com.example.connectverseproject;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppData {
    public static Map<Integer, user> allUsers = new HashMap<>();
    public static ArrayList<ArrayList<Edge>> graph=new ArrayList<>();
    public static user currentUser;
    public static int totalUsers;
    public static int totalFollowing;
    public static int tFollowers =0;
    public static int totalPosts;
    public static void setCurrentUser(user u) {
        currentUser = u;
    }
    public static user getCurrentUser() {
        return currentUser;
    }
    public static void initGraph() {
        graph.clear();
        for (int i = 0; i <= totalUsers+1; i++) {
            graph.add(new ArrayList<>());
        }
    }
    public static void creategraph(int userID,int friendID) {
        user u = allUsers.get(userID);
        user f = allUsers.get(friendID);
        if (u != null && f != null) {
            graph.get(userID).add(new Edge(u, f));
        }
    }
   public static int totalfollowers(int id,String username){
        int temp=0;
       for (int i = 0; i < graph.size(); i++) {
           ArrayList<Edge> edges = graph.get(i);
           if (edges.isEmpty()) {
               continue;
           }
           for (Edge edge : edges) {
              if(edge.dest.getName().equals(username)) {
                  tFollowers++;
                  temp++;
              }

           }
       }
       return temp;

   }
   public static void endfriendship(int id,String friendname,String username){
        ArrayList<Edge> edges= graph.get(id);
       for(int i=0;i<edges.size();i++){
           Edge e=edges.get(i);
           if(e.src.getName().equals(username)&&e.dest.getName().equals(friendname)){
               edges.remove(i);
           }
       }
   }
    public static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

