package org.aery.study.jdk11;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JEP321_Http2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(JEP321_Http2.class);

    private static final String HTTP_SERVER_IP = "localhost";

    private static final int HTTP_SERVER_PORT = 9527;

    private static HttpServer SERVER;

    @BeforeAll
    static void start_http_server() throws IOException {
        SERVER = HttpServer.create();
        SERVER.bind(new InetSocketAddress(HTTP_SERVER_IP, HTTP_SERVER_PORT), 0);
        SERVER.createContext("/", httpExchange -> {
            String method = httpExchange.getRequestMethod();
            URI uri = httpExchange.getRequestURI();
            String body = new String(httpExchange.getRequestBody().readAllBytes());
            LOGGER.info("receive request :　{}　{} {}", method, uri, body);
            httpExchange.sendResponseHeaders(200, body.length());
            httpExchange.getResponseBody().write(body.getBytes());
        });
        SERVER.start();
    }

    @AfterAll
    static void afterAll() {
        SERVER.stop(0);
    }

    @Test
    void HttpClient_send() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://" + HTTP_SERVER_IP + ":" + HTTP_SERVER_PORT + "/"))
                .header("User-Agent", "jdk 11 http client")
//                .GET()
                .POST(HttpRequest.BodyPublishers.ofString("kerker"))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        int statusCode = httpResponse.statusCode();
        String body = httpResponse.body();
        LOGGER.info("receive response :　{}　{}", statusCode, body);
    }

}
