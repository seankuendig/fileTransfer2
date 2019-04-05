package com.aschvinsean;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
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
            String sqlQuery = "DROP DATABASE IF EXISTS filetransfer";
            stmt.executeQuery(sqlQuery);
            sqlQuery = "CREATE DATABASE IF NOT EXISTS filetransfer";
            stmt.executeQuery(sqlQuery);
            sqlQuery = "USE filetransfer";
            stmt.executeQuery(sqlQuery);
            sqlQuery = "CREATE TABLE IF NOT EXISTS directory(" +
                    "permission VARCHAR(15) NOT NULL," +
                    "number INT NOT NULL," +
                    "owner VARCHAR(50) NOT NULL," +
                    "ownergroup VARCHAR(50) NOT NULL," +
                    "size BIGINT NOT NULL," +
                    "date VARCHAR(15) NOT NULL," +
                    "name VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ," +
                    "PRIMARY KEY (name)" +
                    ")";
            stmt.executeQuery(sqlQuery);

            sqlQuery = "CREATE TABLE IF NOT EXISTS file(" +
                    "file_ID INT NOT NULL UNIQUE AUTO_INCREMENT," +
                    "permission VARCHAR(15) NOT NULL," +
                    "number INT NOT NULL," +
                    "owner VARCHAR(50) NOT NULL," +
                    "ownergroup VARCHAR(50) NOT NULL," +
                    "size BIGINT NOT NULL," +
                    "date VARCHAR(15) NOT NULL," +
                    "name VARCHAR(255) NOT NULL," +
                    "path VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL," +
                    "PRIMARY KEY (file_ID)," +
                    "FOREIGN KEY (path) REFERENCES directory(name) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ")";
            stmt.executeQuery(sqlQuery);


            Scanner scanner = new Scanner(new File("tree.txt"));
            String permission;
            int number;
            String owner;
            String ownergroup;
            BigDecimal size;
            String date;
            String name;
            PreparedStatement preparedStmt = null;

            String path = "/";

            for (int i = 0; i < 2; i++) {
                scanner.nextLine();
            }

            permission = scanner.next();
            number = scanner.nextInt();
            owner = (scanner.next()).trim();
            ownergroup = (scanner.next()).trim();
            size = scanner.nextBigDecimal();
            date = (scanner.next() + " " + scanner.next() + " " + scanner.next()).trim();
            name = (scanner.next()).trim();

            sqlQuery = "INSERT INTO directory(`permission`, `number`, `owner`, `ownergroup`, `size`, `date`, `name`) VALUES (?,?,?,?,?,?,?) ";
            preparedStmt = con.prepareStatement(sqlQuery);
            preparedStmt.setString(1, permission);
            preparedStmt.setInt(2, number);
            preparedStmt.setString(3, owner);
            preparedStmt.setString(4, ownergroup);
            preparedStmt.setBigDecimal(5, size);
            preparedStmt.setString(6, date);
            preparedStmt.setString(7, path);
            System.out.println(preparedStmt.getParameterMetaData().);
            preparedStmt.execute();

            for (int i = 0; i < 2; i++) {
                scanner.nextLine();
            }

            permission = scanner.next();
            while (scanner.hasNext()) {

                try {

                    if (!(permission.startsWith("."))) {

                        if (permission.startsWith("l")) {
                            scanner.nextLine();
                            permission = scanner.next();
                            continue;
                        }

                        if ((permission.startsWith("d")) || (permission.startsWith("-"))) {
                            number = scanner.nextInt();
                            owner = scanner.next();
                            ownergroup = scanner.next();
                            size = scanner.nextBigDecimal();
                            date = (scanner.next() + " " + scanner.next() + " " + scanner.next()).trim();
                            name = (scanner.nextLine()).trim();

                            String table;
                            if (permission.startsWith("d")) {
                                table = "directory";

                                sqlQuery = "INSERT INTO " + table + "(`permission`, `number`, `owner`, `ownergroup`, `size`, `date`, `name`) VALUES (?,?,?,?,?,?,?) ";
                                preparedStmt = con.prepareStatement(sqlQuery);
                                if (path.equals("/")) {
                                    preparedStmt.setString(7, path + name);
                                } else {
                                    preparedStmt.setString(7, path + "/" + name);

                                }

                            }
                            if (permission.startsWith("-")) {
                                table = "file";
                                sqlQuery = "INSERT INTO " + table + "(`permission`, `number`, `owner`, `ownergroup`, `size`, `date`, `name`, `path`) VALUES (?,?,?,?,?,?,?,?) ";
                                preparedStmt = con.prepareStatement(sqlQuery);
                                preparedStmt.setString(8, path);
                                preparedStmt.setString(7, name);
                            }

                            preparedStmt.setString(1, permission);
                            preparedStmt.setInt(2, number);
                            preparedStmt.setString(3, owner);
                            preparedStmt.setString(4, ownergroup);
                            preparedStmt.setBigDecimal(5, size);
                            preparedStmt.setString(6, date);
                            preparedStmt.execute();

                            permission = scanner.next();
                            continue;

                        } else {
                            scanner.nextLine();
                            permission = scanner.next();
                            continue;
                        }
                    } else {
                        path = permission.substring(1);
                        path+= scanner.nextLine();

                        path = path.substring(0, path.length()-1);

                        System.out.println(path);

                        for (int i = 0; i < 3; i++) {
                            scanner.nextLine();
                        }


                        permission = scanner.next();
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Done");
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
