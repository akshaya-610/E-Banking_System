package com.example.demo;
import java.sql.*;
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    // The constructor is marked private to prevent instantiation from outside the class.
    private DatabaseConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_management_system", "root", "");
    }
    
    // The static method that returns the single instance of the class.
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        
        return instance;
    }
    
    // Public method to access the Connection object
    public Connection getConnection() {
        return connection;
    }
}

