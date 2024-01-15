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
    public static Connection connection = null;

    public static void main(String[] args) {
        try {
            databaseConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void databaseConnection() {
        try {
            Properties properties = setProps();
            connection = DriverManager.getConnection(URL, properties);
            System.out.println("Database Connection Successful!");
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    private static void databaseClose(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database Connection Closed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public static void test() {
        System.out.println("Running test:");
        //Prepare SQl-statement (CRUD)
        PreparedStatement getData;
        //Get data from table in database with SQl-statement
        try {
            getData = connection.prepareCall("SELECT * FROM tblUser");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Display table data
        try {
            ResultSet tableData = getData.executeQuery();
            while (tableData.next()) {
                int ID = tableData.getInt("fldID");
                String name = tableData.getString("fldName");

                System.out.printf("ID: %d Name: %s%n", ID, name);
            }
        } catch (SQLException ignore) {}
    }


}
