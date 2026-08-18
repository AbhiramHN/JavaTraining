package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBConnection
{
    private static final ResourceBundle bundle = ResourceBundle.getBundle("database");

    static
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException exception)
        {
            throw new RuntimeException(exception);
        }
    }

    public static Connection getConnection()
    {
        try
        {
            return DriverManager.getConnection(
                    bundle.getString("db.url"),
                    bundle.getString("db.username"),
                    bundle.getString("db.password"));
        }
        catch (SQLException exception)
        {
            throw new RuntimeException(exception);
        }
    }
}