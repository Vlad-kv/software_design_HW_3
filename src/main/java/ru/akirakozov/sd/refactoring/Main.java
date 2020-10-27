package ru.akirakozov.sd.refactoring;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Application app = new Application(8081, "jdbc:sqlite:test.db");
        app.start();
        app.join();
    }
}
