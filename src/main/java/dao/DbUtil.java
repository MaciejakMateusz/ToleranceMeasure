package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private static final String USER = "root";
    private static final String PASSWORD = "coderslab";
    private static final String URL = "jdbc:mysql://localhost:3306/";

    public static Connection getConnection (String database) throws SQLException {
        String url = URL + database + "?useSSL=false&characterEncoding=utf8";
        return DriverManager.getConnection(url, USER, PASSWORD);
    }

}