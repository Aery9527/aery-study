package org.aery.study.jdk18;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JEP408_Simple_Web_Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(JEP408_Simple_Web_Server.class);

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        int port = 8000;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", exchange -> {
            String response = "kerker!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });
        server.start();

        HttpClient httpClient = HttpClient.newHttpClient();
        try (httpClient) {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + port + "/"))
                    .header("User-Agent", "jdk 1ㄚ http client")
                    .GET()
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            int statusCode = httpResponse.statusCode();
            String body = httpResponse.body();

            LOGGER.info("receive response({}) : {}", statusCode, body);
        }

        server.stop(0);
    }

}
