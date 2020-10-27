package ru.akirakozov.sd.refactoring;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OkHttpServletResponse {
    private final HttpServletResponse response;
    public OkHttpServletResponse(HttpServletResponse response) {
        this.response = response;
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    public void println(String line) throws IOException {
        response.getWriter().println(line);
    }
}
