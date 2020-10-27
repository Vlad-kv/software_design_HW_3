package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.Database;
import ru.akirakozov.sd.refactoring.DatabaseConnection;
import ru.akirakozov.sd.refactoring.OkHttpServletResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final Database database;

    public GetProductsServlet(Database database) {
        this.database = database;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OkHttpServletResponse r = new OkHttpServletResponse(response);
        try {
            try (DatabaseConnection c = database.getConnection()) {
                ArrayList<ArrayList<String>> res = c.executeSQLQuery("SELECT * FROM PRODUCT", Arrays.asList("name", "price"));
                r.println("<html><body>");

                for (ArrayList<String> row : res) {
                    r.println(row.get(0) + "\t" + row.get(1) + "</br>");
                }
                r.println("</body></html>");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
