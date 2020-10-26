package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.Database;
import ru.akirakozov.sd.refactoring.DatabaseConnection;

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
        try {
            try (DatabaseConnection c = database.getConnection()) {
                ArrayList<ArrayList<String>> res = c.executeSQLQuery("SELECT * FROM PRODUCT", Arrays.asList("name", "price"));
                response.getWriter().println("<html><body>");

                for (ArrayList<String> row : res) {
                    response.getWriter().println(row.get(0) + "\t" + row.get(1) + "</br>");
                }
                response.getWriter().println("</body></html>");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
