package com.example.mediaplayer;

import java.sql.*;
import java.util.Properties;

public class Database {
    public static final String USERNAME = "sa"; //username for SSMS login
    public static final String PASSWORD = "Kokopops02"; //password for SSMS login
    public static final String DATABASE_NAME = "dbMedia-player"; //database
    public static final String PORT = "1433"; //SSMS port here, 1433 is default
    public static final String URL = "jdbc:sqlserver://localhost:" + PORT + ";databaseName=" + DATABASE_NAME;
    public static final String ENCRYPT = "false";
    public static Properties properties = setProps();
    public static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(URL, properties);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        try {
            databaseConnection();
            selectAndDisplay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void databaseConnection() {
        try {
            System.out.println("Database Connection Successful!");
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    public static Properties setProps() {
        System.out.println("Setting up properties");
        Properties properties = new Properties();
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("encrypt", ENCRYPT);
        return properties;
    }

    public static void selectAndDisplay() {
        System.out.println("Running test:");

        PreparedStatement getData; //Prepare SQl-statement (CRUD)

        //Get data from table in database with SQl-statement
        try {
            getData = connection.prepareCall("SELECT * FROM Media");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Display table data
        try {
            ResultSet tableData = getData.executeQuery();
            while (tableData.next()) {
                int ID = tableData.getInt("ID");
                String name = tableData.getString("Name");

                System.out.printf("ID: %d Name: %s", ID, name);
            }
        } catch (SQLException ignore) {}
    }


}
