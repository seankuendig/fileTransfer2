package com.aschvinsean;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
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
                    "number BIGINT NOT NULL," +
                    "owner VARCHAR(50) NOT NULL," +
                    "ownergroup VARCHAR(50) NOT NULL," +
                    "size BIGINT NOT NULL," +
                    "date VARCHAR(15) NOT NULL," +
                    "name varchar(255) NOT NULL," +
                    "PRIMARY KEY (directory_ID)" +
                    ")";
            stmt.executeQuery(sqlQuery);

            sqlQuery = "CREATE TABLE IF NOT EXISTS file(" +
                    "file_ID INT NOT NULL UNIQUE AUTO_INCREMENT," +
                    "permission VARCHAR(15) NOT NULL," +
                    "number BIGINT NOT NULL," +
                    "owner VARCHAR(50) NOT NULL," +
                    "ownergroup VARCHAR(50) NOT NULL," +
                    "size BIGINT NOT NULL," +
                    "date VARCHAR(15) NOT NULL," +
                    "name varchar(255) NOT NULL," +
                    "ID_directory INT," +
                    "PRIMARY KEY (file_ID)" +
                    //     "FOREIGN KEY (ID_directory) REFERENCES directory(directory_ID) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ")";
            stmt.executeQuery(sqlQuery);

            Scanner scanner = new Scanner(new File("tree.txt"));

            while (scanner.hasNext()) {

                for (int i = 0; i < 4; i++) {
                    scanner.nextLine();
                }

                String permission = scanner.next();
                while (!(permission.startsWith("."))) {
                    BigDecimal number = new BigDecimal("0");
                    String owner = null;
                    String ownergroup = null;
                    BigDecimal size = new BigDecimal("0");
                    String date = null;
                    String name = null;

                    if (permission.startsWith("l")) {
                        scanner.nextLine();
                        permission = scanner.next();
                        continue;
                    }

                    if ((permission.startsWith("d")) || (permission.startsWith("-"))) {
                        number = scanner.nextBigDecimal();
                        owner = scanner.next();
                        ownergroup = scanner.next();
                        size = scanner.nextBigDecimal();
                        date = scanner.next() + " " + scanner.next() + " " + scanner.next();
                        name = scanner.next();

                        String table = null;
                        if (permission.startsWith("d")) {
                            table = "directory";
                        }
                        if (permission.startsWith("-")) {
                            table = "file";
                        }

                        sqlQuery = "INSERT INTO " + table + "(`permission`, `number`, `owner`, `ownergroup`, `size`, `date`, `name`) VALUES (?,?,?,?,?,?,?) ";

                        PreparedStatement preparedStmt = con.prepareStatement(sqlQuery);
                        preparedStmt.setString(1, permission);
                        preparedStmt.setBigDecimal(2, number);
                        preparedStmt.setString(3, owner);
                        preparedStmt.setString(4, ownergroup);
                        preparedStmt.setBigDecimal(5, size);
                        preparedStmt.setString(6, date);
                        preparedStmt.setString(7, name);
                        preparedStmt.execute();

                        scanner.nextLine();
                        permission = scanner.next();
                        continue;

                    } else {
                        scanner.nextLine();
                        permission = scanner.next();
                        continue;
                    }
                }
            }
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
