package util;

import java.sql.*;

public class DBConection {
    private static String Url = "jdbc:postgresql://localhost:5432/coursemanagement";
    private static String User = "postgres";
    private static String Password = "4105";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(Url, User, Password);
        } catch (SQLException e) {
            System.err.println("Kett noi that bai");
            throw new RuntimeException(e);
        }
        return conn;
    }

}
