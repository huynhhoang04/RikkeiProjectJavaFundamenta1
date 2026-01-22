package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnection {
    //init kết nối db
    public static Connection getConnection() {
        Connection conn = null;
        //khởi tạo luông input lấy dữ liệu từ file properties load driver jdbc của postgre , tên và mk
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("dbconfig.properties");
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
            System.err.println("Ket noi that bai");
            throw new RuntimeException(e);
        }
        return conn;
    }

}
