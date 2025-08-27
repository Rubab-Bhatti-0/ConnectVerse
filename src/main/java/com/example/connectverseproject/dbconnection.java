package com.example.connectverseproject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconnection {

    private static final String URL = "jdbc:mysql://localhost:3306/connect";
    private static final String USER = "root";
    private static final String PASSWORD = "lOCKER@1";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Failed to connect to DB.");
            e.printStackTrace();
        }
        return null;
    }
}

