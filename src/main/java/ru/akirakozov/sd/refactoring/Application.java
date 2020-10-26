package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

public class Application {
    private final Server server;
    private final Database database;

    public Application(int port, String dbURL) throws Exception {
        database = new Database(dbURL);
        try (DatabaseConnection c = database.getConnection()) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            c.executeSQLUpdate(sql);
        }

        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(database)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(database)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(database)),"/query");
    }

    public void start() throws Exception {
        server.start();
    }

    public void join() throws Exception {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }
}
