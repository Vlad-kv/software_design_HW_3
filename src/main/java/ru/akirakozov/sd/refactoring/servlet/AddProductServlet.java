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
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {
    private final Database database;

    public AddProductServlet(Database database) {
        this.database = database;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        OkHttpServletResponse r = new OkHttpServletResponse(response);

        try {
            try (DatabaseConnection c = database.getConnection()) {
                String sql = "INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
                c.executeSQLUpdate(sql);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        r.println("OK");
    }
}
