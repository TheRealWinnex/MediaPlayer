package com.example.mediaplayer;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {
    public static final String USERNAME = "sa"; //username for SSMS login
    public static final String PASSWORD = "1234"; //password for SSMS login
    public static final String DATABASE_NAME = "dbMedia-player"; //database
    public static final String PORT = "1433"; //SSMS port here, 1433 is default
    public static final String URL = "jdbc:sqlserver://localhost:" + PORT + ";databaseName=" + DATABASE_NAME;
    public static final String ENCRYPT = "false";
    public static Connection connection = null;

    public void tryMe() {
        //Setting properties
        Properties properties = setProps();
        //creating JDBC connection
        connection = databaseConnection(properties, URL);
        //doing a test with SQL-statements
        test();
        //closing JDBC connection
        databaseClose(connection);
    }

    public static Properties setProps() {
        System.out.println("Setting up properties");
        Properties properties = new Properties();
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("encrypt", ENCRYPT);
        databaseConnection(properties, URL);
        return properties;
    }

    public static Connection databaseConnection(Properties properties, String URL) {
        try {
            connection = DriverManager.getConnection(URL, properties);
            System.out.println("DB Connection succesful");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void databaseClose(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Closing connection to JDBC");
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
