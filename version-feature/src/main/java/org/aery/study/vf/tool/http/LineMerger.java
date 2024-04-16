package org.aery.study.vf.tool.http;

import java.util.Optional;

public interface LineMerger {

    String getName();

    Optional<String> merge(String line);

}
