package app.query;

import java.sql.ResultSet;

public interface DbConnection {
    ResultSet executeQuery(String query);
    void executeUpdate(String query);
}
