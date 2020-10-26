package ru.akirakozov.sd.refactoring;

import java.sql.SQLException;

public class Database {
    private final String databaseURL;
    Database(String databaseURL) {
        this.databaseURL = databaseURL;
    }

    public DatabaseConnection getConnection() throws SQLException {
        return new DatabaseConnection(databaseURL);
    }
}
