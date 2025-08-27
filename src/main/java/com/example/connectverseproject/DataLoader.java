

package com.example.connectverseproject;


import eu.hansolo.toolbox.time.DateTimes;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class DataLoader {
    public static void loadAllData() {
        Map<Integer, user> userMap = new HashMap<>();

        try (Connection conn = dbconnection.getConnection()) {
            // Load users
            ResultSet rs = conn.prepareStatement("SELECT * FROM signup").executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idsignup");
                String name = rs.getString("username");
                String email = rs.getString("email");
                String ps=rs.getString("password");
                String about=rs.getString("about");
                userMap.put(id, new user(id, name, email,about,ps));
            }

            // Load posts
            rs = conn.prepareStatement("SELECT * FROM posts").executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("userid");
                String content = rs.getString("content");
                String category = rs.getString("interest");
               Timestamp date= rs.getTimestamp("timestamp");
               LocalDateTime local=date.toLocalDateTime();

                post p = new post(content, category, local);
                if (userMap.containsKey(userId)) {
                    userMap.get(userId).addPost(p);
                    AppData.totalPosts++;
                }
            }
            AppData.allUsers = userMap;
            int maxUserId = Collections.max(userMap.keySet());
            AppData.totalUsers = maxUserId;
            AppData.initGraph();
            // Load friendships
            rs = conn.prepareStatement("SELECT * FROM friends").executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("userid");
                int friendId = rs.getInt("friendid");
                if (userMap.containsKey(userId) && userMap.containsKey(friendId)) {
                   AppData.creategraph(userId,friendId);
                }
            }

            // Store into AppData
            AppData.allUsers = userMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static boolean hasUserLiked(int postId, int userId) {
        String sql = "SELECT 1 FROM post_likes WHERE post_id=? AND user_id=?";
        Connection conn = dbconnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);   // must match posts.idposts
            stmt.setInt(2, userId);   // must match signup.idsignup
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean likePost(int postId, int userId) {
        String sql = "INSERT INTO post_likes (post_id, user_id) VALUES (?, ?)";
        Connection conn = dbconnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);   // FK → posts.idposts
            stmt.setInt(2, userId);   // FK → signup.idsignup
            stmt.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            // This happens if FK fails or duplicate like
            System.out.println("User " + userId + " already liked or invalid post_id/user_id");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean unlikePost(int postId, int userId) {
        String sql = "DELETE FROM post_likes WHERE post_id=? AND user_id=?";
        Connection conn = dbconnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void toggleLike(int postId, int userId) {
        if (hasUserLiked(postId, userId)) {
            unlikePost(postId, userId);
            System.out.println("Unliked post " + postId);
        } else {
            likePost(postId, userId);
            System.out.println("Liked post " + postId);
        }
    }

    public static int getLikeCount(int postId) {
        String sql = "SELECT COUNT(*) FROM post_likes WHERE post_id=?";
        Connection conn = dbconnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String postTime(LocalDateTime pt){
        LocalDateTime currentDate=LocalDateTime.now();
        Duration duration =Duration.between(pt,currentDate);

        long seconds=duration.getSeconds();
        long minutes=seconds/ 60;
        long hours=minutes/60;
        long days=duration.toDays();
        long months=days/12;

        if(seconds<60){
            return "a few seconds ago";

        }else if(minutes<60){
            return minutes+" minute ago";
        }
        else if(hours<24){
            return hours+" ago";
        } else  return  months<12?  pt.format(DateTimeFormatter.ofPattern("dd MMMM")): "at "+pt.format(DateTimeFormatter.ofPattern("dd MMMM YYYY"));


    }


}
