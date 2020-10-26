package ru.akirakozov.sd.refactoring;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection implements AutoCloseable {
    private final Connection c;
    DatabaseConnection(String databaseUrl) throws SQLException {
        c = DriverManager.getConnection(databaseUrl);
    }

    public ArrayList<ArrayList<String>> executeSQLQuery(String sqlQuery) throws SQLException{
        return executeSQLQuery(sqlQuery, new ArrayList<>());
    }

    public ArrayList<ArrayList<String>> executeSQLQuery(String sqlQuery, List<String> resultColumnNames) throws SQLException{
        return executeSQLQuery(sqlQuery, resultColumnNames, "String");
    }

    public void executeSQLUpdate(String sqlUpdate) throws SQLException {
        Statement stmt = c.createStatement();
        stmt.executeUpdate(sqlUpdate);
        stmt.close();
    }

    public ArrayList<ArrayList<String>> executeSQLQuery(String sqlQuery, List<String> resultColumnNames, String dataType) throws SQLException{
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sqlQuery);

        ArrayList<ArrayList<String>> res = new ArrayList<>();

        while (rs.next()) {
            ArrayList<String> row = new ArrayList<>();
            int colNo = 1;
            for (String colName : resultColumnNames) {
                if (colName.isEmpty()) {
                    if (dataType.equals("int")) {
                        row.add(rs.getInt(colNo) + "");
                    } else {
                        row.add(rs.getString(colNo));
                    }
                } else {
                    if (dataType.equals("int")) {
                        row.add(rs.getInt(colName) + "");
                    } else {
                        row.add(rs.getString(colName));
                    }
                }
                colNo++;
            }
            res.add(row);
        }

        rs.close();
        stmt.close();
        return res;
    }

    @Override
    public void close() throws SQLException {
        c.close();
    }
}
