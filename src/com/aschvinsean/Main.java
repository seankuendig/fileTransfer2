package com.aschvinsean;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        Connection con = null;
        try {
            // get a connection to database
            con = DriverManager.getConnection(
                    "jdbc:mariadb://192.168.40.129/hr", "root", "1234");
            // create a statement
            Statement stmt = con.createStatement();
            // execute SQL statement
            String sqlQuery = "SELECT * FROM employee";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            // process the result set
            while (rs.next()) {
                String ln = rs.getString("last_name");
                String fn = rs.getString("first_name");
                System.out.println(fn + " " + ln);
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
