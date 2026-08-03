package database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection
{
    private static final Properties properties = new Properties();

    static
    {
        try
        {
            InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("database.properties");

            if (inputStream == null)
            {
                throw new RuntimeException("database.properties file not found.");
            }

            properties.load(inputStream);

            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (IOException | ClassNotFoundException exception)
        {
            throw new RuntimeException(exception);
        }
    }

    public static Connection getConnection()
    {
        try
        {
            return DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.username"),
                    properties.getProperty("db.password"));
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
            return null;
        }
    }
}