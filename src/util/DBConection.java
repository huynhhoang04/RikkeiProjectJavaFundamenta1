package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConection {

    public static Connection getConnection() {
        Connection conn = null;
        try (InputStream in = DBConection.class.getClassLoader().getResourceAsStream("dbconfig.properties");
             ) {
            Properties properties = new Properties();
            if(in != null) {
                properties.load(in);
            }
            String Url = properties.getProperty("url");
            String User = properties.getProperty("user");
            String Password = properties.getProperty("password");
            conn = DriverManager.getConnection(Url, User, Password);
        } catch (SQLException | IOException e) {
            System.err.println("Kett noi that bai");
            throw new RuntimeException(e);
        }
        return conn;
    }

}
