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
import java.util.Collections;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final Database database;

    public QueryServlet(Database database) {
        this.database = database;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        try {
            if ("max".equals(command)) {
                try (DatabaseConnection c = database.getConnection()) {
                    ArrayList<ArrayList<String>> res = c.executeSQLQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", Arrays.asList("name", "price"));
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("<h1>Product with max price: </h1>");

                    for (ArrayList<String> line : res) {
                        response.getWriter().println(line.get(0) + "\t" + line.get(1) + "</br>");
                    }
                    response.getWriter().println("</body></html>");
                }
            } else if ("min".equals(command)) {
                try (DatabaseConnection c = database.getConnection()) {
                    ArrayList<ArrayList<String>> res = c.executeSQLQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", Arrays.asList("name", "price"));
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("<h1>Product with min price: </h1>");

                    for (ArrayList<String> line : res) {
                        response.getWriter().println(line.get(0) + "\t" + line.get(1) + "</br>");
                    }
                    response.getWriter().println("</body></html>");
                }
            } else if ("sum".equals(command)) {
                try (DatabaseConnection c = database.getConnection()) {
                    ArrayList<ArrayList<String>> res = c.executeSQLQuery("SELECT SUM(price) FROM PRODUCT", Collections.singletonList(""), "int");
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("Summary price: ");

                    if (! res.isEmpty()) {
                        response.getWriter().println(res.get(0).get(0));
                    }
                    response.getWriter().println("</body></html>");
                }
            } else if ("count".equals(command)) {
                try (DatabaseConnection c = database.getConnection()) {
                    ArrayList<ArrayList<String>> res = c.executeSQLQuery("SELECT COUNT(*) FROM PRODUCT", Collections.singletonList(""), "int");
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("Number of products: ");

                    if (! res.isEmpty()) {
                        response.getWriter().println(res.get(0).get(0));
                    }
                    response.getWriter().println("</body></html>");
                }
            } else {
                response.getWriter().println("Unknown command: " + command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
