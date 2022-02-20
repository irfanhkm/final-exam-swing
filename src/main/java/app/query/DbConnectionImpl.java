package app.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnectionImpl implements DbConnection {
    private static DbConnection INSTANCE;
    private Statement st;

    private DbConnectionImpl() {
        try {
            String username = "root";
            String password = "Rahasia123!";
            String database = "thecake";
            String server = "localhost";
            String connection = String.format(
                "jdbc:mysql://%s:3306/%s?user=%s&password=%s&serverTimezone=UTC",
                server, database, username, password);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(connection);
            st = con.createStatement(1004, 1008);
            System.out.println("Connection Successful");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection Error");
        }
    }

    // Singleton
    public static DbConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DbConnectionImpl();
        }

        return INSTANCE;
    }

    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection Error");
        }
        return rs;
    }

    public void executeUpdate(String query) {
        try {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection Error");
        }
    }
}
