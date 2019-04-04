package com.aschvinsean;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Connection con = null;
        try {
            // get a connection to database
            con = DriverManager.getConnection(
                    "jdbc:mariadb://192.168.40.129/", "root", "1234");
            // create a statement
            Statement stmt = con.createStatement();
            // execute SQL statement
            String sqlQuery = "CREATE DATABASE IF NOT EXISTS filetransfer";
            stmt.executeQuery(sqlQuery);
            sqlQuery = "USE filetransfer";
            stmt.executeQuery(sqlQuery);
            sqlQuery = "CREATE TABLE IF NOT EXISTS directory(" +
                    "directory_ID INT NOT NULL UNIQUE AUTO_INCREMENT," +
                    "permission VARCHAR(15) NOT NULL," +
                    "number INT NOT NULL," +
                    "owner VARCHAR(50) NOT NULL," +
                    "ownergroup VARCHAR(50) NOT NULL," +
                    "size INT NOT NULL," +
                    "date VARCHAR(15) NOT NULL," +
                    "name varchar(255) NOT NULL," +
                    "PRIMARY KEY (directory_ID)" +
                    ")";
            stmt.executeQuery(sqlQuery);

            sqlQuery = "CREATE TABLE IF NOT EXISTS file(" +
                    "file_ID INT NOT NULL UNIQUE AUTO_INCREMENT," +
                    "permission VARCHAR(15) NOT NULL," +
                    "number INT NOT NULL," +
                    "owner VARCHAR(50) NOT NULL," +
                    "ownergroup VARCHAR(50) NOT NULL," +
                    "size INT NOT NULL," +
                    "date VARCHAR(15) NOT NULL," +
                    "name varchar(255) NOT NULL," +
                    "ID_directory INT," +
                    "PRIMARY KEY (file_ID)," +
                    "FOREIGN KEY (ID_directory) REFERENCES directory(directory_ID) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ")";
            stmt.executeQuery(sqlQuery);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }

        try {
            Scanner scanner = new Scanner(new File("tree.txt"));

            for (int i = 0; i < 4; i++) {
                scanner.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
