package org.aery.study.vf.service.api;

import org.aery.study.vf.tool.http.LineMerger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface HttpRetriever {

    List<String> byLine(String fromURL, LineMerger lineMerger) throws IOException, URISyntaxException;

}
