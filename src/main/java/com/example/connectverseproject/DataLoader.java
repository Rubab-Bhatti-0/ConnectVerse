

package com.example.connectverseproject;


import eu.hansolo.toolbox.time.DateTimes;

import java.sql.*;
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
//                System.out.println(date);
//                LocalDateTime lc=LocalDateTime.now();
//               // LocalDateTime dt=lc.minus(lc);
//             int t= date.compareTo(Timestamp.valueOf(lc));
//                if(t==0){
//                    System.out.println("equal");
//                } else if (t>=0) {
//                    System.out.println(date+" is befoe to "+ lc);
//
//                }
//                else{
//                    System.out.println(lc+" is befoe to "+ date.toString());
//
//                }
//              Date datetime = rs.getDate("timestamp");
//                System.out.println(datetime);
//              //  Date d =datetime.formatted((DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                System.out.println();
                post p = new post(content, category, date);
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



}
