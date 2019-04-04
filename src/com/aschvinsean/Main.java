package com.aschvinsean;

import java.sql.*;

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
                    "permissions VARCHAR(15) NOT NULL," +
                    "number INT NOT NULL," +
                    "owner VARCHAR(50) NOT NULL," +
                    "group VARCHAR(50) NOT NULL," +
                    "size INT NOT NULL," +
                    "date VARCHAR(15) NOT NULL," +
                    "name varchar(255) NOT NULL," +
                    "PRIMARY KEY (directory_ID)" +
                    ")";
            stmt.executeQuery(sqlQuery);

            // process the result set
            /*
            while (rs.next()) {
                String ln = rs.getString("last_name");
                String fn = rs.getString("first_name");
                System.out.println(fn + " " + ln);
            }

       */
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
    }
}
