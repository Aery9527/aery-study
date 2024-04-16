package org.aery.study.vf.service.impl;

import jakarta.annotation.PostConstruct;
import org.aery.study.vf.service.api.VersionFeatureConverter;
import org.aery.study.vf.vo.VersionFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OfficialWebsiteVersionFeatureConverter implements VersionFeatureConverter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String pattern_jep = "(?<jep>\\d{3})";

    private final String pattern_link = "href=\"(?<link>\\S+)\"";

    private final String pattern_feature = ">(?<feature>.*)</a>";

    private final Pattern pattern = Pattern.compile(pattern_jep + ".*:.*" + pattern_link + pattern_feature);

    @PostConstruct
    public void initial() {
        this.logger.info("pattern : {}", this.pattern.pattern());
    }

    @Override
    public VersionFeature convert(String content) {
        Matcher matcher = this.pattern.matcher(content);
        String linkPrefix = "https://openjdk.org"; // from jdk19 need prefix
        if (matcher.find()) {
            String jep = matcher.group("jep").trim();
            String link = linkPrefix + matcher.group("link").trim();
            String feature = matcher.group("feature").trim();
            return new VersionFeature(Integer.parseInt(jep), feature, link);
        } else {
            this.logger.warn("content not match pattern => {}", content);
            return null;
        }
    }

}
