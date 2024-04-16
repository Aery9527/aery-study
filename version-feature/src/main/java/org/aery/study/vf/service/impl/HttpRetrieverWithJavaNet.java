package org.aery.study.vf.service.impl;

import org.aery.study.vf.ValueEL;
import org.aery.study.vf.service.api.HttpRetriever;
import org.aery.study.vf.tool.http.LineMerger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class HttpRetrieverWithJavaNet implements HttpRetriever {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value(ValueEL.HttpRetriever.PRINT_ALL_LINE)
    private boolean printAllLine;

    @Override
    public List<String> byLine(String fromURL, LineMerger lineMerger) throws IOException, URISyntaxException {
        URL url = new URI(fromURL).toURL();
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setConnectTimeout(3_000);
        conn.setReadTimeout(3_000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "");

        int responseCode = conn.getResponseCode();
        String responseMessage = conn.getResponseMessage();
        this.logger.info("{} => {} {}", fromURL, responseCode, responseMessage);

        List<String> resultOfLine = new ArrayList<>();
        try (InputStream inputStream = conn.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                if (this.printAllLine) {
                    this.logger.info(line);
                }

                lineMerger.merge(line).ifPresent(resultOfLine::add);
            }
        } finally {
            conn.disconnect();
        }

        return resultOfLine;
    }

}
