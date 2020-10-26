import org.junit.Assert;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AppTest {
    int port;
    String prefix;
    ArrayList<Pair<String, Integer>> products;
    int sum, min, max;

    @Test
    public void test_1() {
        try {
            String testDB = "test.db";
            Files.deleteIfExists(Paths.get(testDB));
            port = 8081;
            prefix = "http://localhost:" + port + "/";

            Application app = new Application(port, "jdbc:sqlite:" + testDB);
            app.start();

            products = new ArrayList<>();
            sum = 0;

            Assert.assertEquals(products, getProducts());
            Assert.assertEquals(0, query("count"));
            Assert.assertEquals(sum, query("sum"));

            testAddProduct("Product 1", 10);
            testAll();

            testAddProduct("Product 2", 20);
            testAll();

            testAddProduct("Product 3", 15);
            testAll();

            testAddProduct("Product 4", 25);
            testAll();

            testAddProduct("Product 5", 5);
            testAll();

            testAddProduct("Product 6", 0);
            testAll();

            testAddProduct("Product 7", 10);
            testAll();

            app.stop();
            app.join();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void test_2() {
        try {
            String testDB = "test.db";
            Files.deleteIfExists(Paths.get(testDB));
            port = 8081;
            prefix = "http://localhost:" + port + "/";

            Application app = new Application(port, "jdbc:sqlite:" + testDB);
            app.start();

            products = new ArrayList<>();
            sum = 0;

            Assert.assertEquals(products, getProducts());
            Assert.assertEquals(0, query("count"));
            Assert.assertEquals(sum, query("sum"));

            testAddProduct("Product 1", 3);
            testAll();

            testAddProduct("Product 2", 1);
            testAll();

            testAddProduct("Product 3", 4);
            testAll();

            testAddProduct("Product 4", 1);
            testAll();

            testAddProduct("Product 5", 5);
            testAll();

            testAddProduct("Product 6", 9);
            testAll();

            testAddProduct("Product 7", 2);
            testAll();

            app.stop();
            app.join();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void testAll() throws Exception {
        Assert.assertEquals(products, getProducts());
        Assert.assertEquals(products.size(), query("count"));
        Assert.assertEquals(sum, query("sum"));
        Assert.assertEquals(min, query("min"));
        Assert.assertEquals(max, query("max"));
    }

    private int query(String command) throws Exception {
        String response = readAsText(new URL(prefix + "query?command=" + command))
                .replaceAll("<[^>]*>", "").trim();
        int pos = response.length();
        while ((pos > 0) && (Character.isDigit(response.charAt(pos - 1)))) {
            pos--;
        }
        return Integer.parseInt(response.substring(pos));
    }

    private void testAddProduct(String productName, int price) throws Exception {
        if (products.size() == 0) {
            min = max = price;
        } else {
            min = Math.min(min, price);
            max = Math.max(max, price);
        }
        products.add(new Pair<>(productName, price));
        sum += price;
        String res = readAsText(new URL((prefix + "add-product?name=" + productName + "&price=" + price)
                                            .replaceAll(" ", "%20")))
                        .replaceAll("<[^>]*>", "").trim();
        Assert.assertEquals("OK", res);
    }

    private ArrayList<Pair<String, Integer>> getProducts() throws Exception {
        ArrayList<Pair<String, Integer>> products = new ArrayList<>();
        String result = readAsText(new URL(prefix + "get-products"))
                .replaceAll("<[^>]*>", "");

        String[] lines = result.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (! line.isEmpty()) {
                int pos = line.length();
                while ((pos > 0) && (('0' <= line.charAt(pos - 1)) && (line.charAt(pos - 1) <= '9'))) {
                    pos--;
                }
                String name = line.substring(0, pos).trim();
                int price = Integer.parseInt(line.substring(pos));
                products.add(new Pair<>(name, price));
            }
        }
        return products;
    }

    private String readAsText(URL url) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            return builder.toString();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
