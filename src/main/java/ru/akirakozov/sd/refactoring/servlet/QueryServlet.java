package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.Database;
import ru.akirakozov.sd.refactoring.DatabaseConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final Database database;
    private final Map<String, Consumer<HttpServletResponse>> queries;

    public QueryServlet(Database database) {
        this.database = database;

        queries = new TreeMap<>();
        queries.put("max", (HttpServletResponse r) -> getTopProduct("max", r));
        queries.put("min", (HttpServletResponse r) -> getTopProduct("min", r));
        queries.put("sum", (HttpServletResponse r) -> calcIntFunction("SUM(price)", "Summary price: ", r));
        queries.put("count", (HttpServletResponse r) -> calcIntFunction("COUNT(*)", "Number of products: ", r));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if (! queries.containsKey(command)) {
            response.getWriter().println("Unknown command: " + command);
        } else {
            response.getWriter().println("<html><body>");
            queries.get(command).accept(response);
            response.getWriter().println("</body></html>");
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void calcIntFunction(String sqlFunction, String message, HttpServletResponse response) {
        try (DatabaseConnection c = database.getConnection()) {
            ArrayList<ArrayList<String>> res = c.executeSQLQuery("SELECT " + sqlFunction + " FROM PRODUCT", Collections.singletonList(""), "int");
            response.getWriter().println(message);

            if (! res.isEmpty()) {
                response.getWriter().println(res.get(0).get(0));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void getTopProduct(String type, HttpServletResponse response) {
        try (DatabaseConnection c = database.getConnection()) {
            ArrayList<ArrayList<String>> res = c.executeSQLQuery("SELECT * FROM PRODUCT ORDER BY PRICE " + (type.equals("max") ? "DESC " : "") + "LIMIT 1", Arrays.asList("name", "price"));
            response.getWriter().println("<h1>Product with " + type + " price: </h1>");

            for (ArrayList<String> line : res) {
                response.getWriter().println(line.get(0) + "\t" + line.get(1) + "</br>");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
