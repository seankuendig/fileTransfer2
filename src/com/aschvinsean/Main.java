package com.aschvinsean;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        double time = System.currentTimeMillis();
        Connection con = null;
        StringBuilder insertSqlDir = new StringBuilder("INSERT INTO directory(`permission`, `number`, `owner`, `ownergroup`, `size`, `date`, `name`) VALUES");
        StringBuilder insertSqlFile = new StringBuilder("INSERT INTO file(`permission`, `number`, `owner`, `ownergroup`, `size`, `date`, `name`, `path`) VALUES");

        ArrayList<Object> parametersDir = new ArrayList<>();
        ArrayList<Object> parametersFile = new ArrayList<>();

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

            String path = "/";

            for (int i = 0; i < 2; i++) {
                scanner.nextLine();
            }

            permission = scanner.next();
            number = scanner.nextInt();
            owner = scanner.next();
            ownergroup = scanner.next();
            size = scanner.nextBigDecimal();
            date = (scanner.next() + " " + scanner.next() + " " + scanner.next()).trim();
            name = scanner.next();

            parametersDir.add(permission);
            parametersDir.add(number);
            parametersDir.add(owner);
            parametersDir.add(ownergroup);
            parametersDir.add(size);
            parametersDir.add(date);
            parametersDir.add(path);
            insertSqlDir.append(" (?,?,?,?,?,?,?),");

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

                            if (permission.startsWith("d")) {
                                parametersDir.add(permission.replace(".", ""));
                                parametersDir.add(number);
                                parametersDir.add(owner);
                                parametersDir.add(ownergroup);
                                parametersDir.add(size);
                                parametersDir.add(date);
                                if (path.equals("/")) {
                                    parametersDir.add(path + name);
                                } else {
                                    parametersDir.add(path + "/" + name);
                                }
                                insertSqlDir.append(" (?,?,?,?,?,?,?),");
                            }

                            if (permission.startsWith("-")) {
                                parametersFile.add(permission.replace(".", ""));
                                parametersFile.add(number);
                                parametersFile.add(owner);
                                parametersFile.add(ownergroup);
                                parametersFile.add(size);
                                parametersFile.add(date);
                                parametersFile.add(name);
                                parametersFile.add(path);
                                insertSqlFile.append(" (?,?,?,?,?,?,?,?),");
                            }


                            permission = scanner.next();
                            continue;

                        } else {
                            scanner.nextLine();
                            permission = scanner.next();
                            continue;
                        }
                    } else {
                        path = permission.substring(1);
                        path += scanner.nextLine();

                        path = path.substring(0, path.length() - 1);


                        for (int i = 0; i < 3; i++) {
                            scanner.nextLine();
                        }


                        permission = scanner.next();
                    }
                } catch (NoSuchElementException e) {
                    System.out.println((System.currentTimeMillis() - time) / 1000);

                    insertSqlDir.setLength(insertSqlDir.length() - 1);
                    PreparedStatement pstmt = con.prepareStatement(insertSqlDir.toString());
                    for (int i = 1; i <= parametersDir.size(); i++) {
                        pstmt.setObject(i, parametersDir.get(i - 1));
                    }
                    pstmt.execute();

                    insertSqlFile.setLength(insertSqlFile.length() - 1);
                    pstmt = con.prepareStatement(insertSqlFile.toString());
                    for (int i = 1; i <= parametersFile.size(); i++) {
                        pstmt.setObject(i, parametersFile.get(i - 1));
                    }
                    pstmt.execute();
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        System.out.println((System.currentTimeMillis() - time) / 1000);
    }


}
