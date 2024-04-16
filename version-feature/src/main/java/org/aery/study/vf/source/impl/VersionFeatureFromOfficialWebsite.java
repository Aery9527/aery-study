package org.aery.study.vf.source.impl;

import jakarta.annotation.PostConstruct;
import org.aery.study.vf.ValueEL;
import org.aery.study.vf.service.api.HttpRetriever;
import org.aery.study.vf.service.api.VersionFeatureConverter;
import org.aery.study.vf.source.api.VersionFeatureSource;
import org.aery.study.vf.source.tool.AnchorMerger;
import org.aery.study.vf.tool.http.ParagraphMerger;
import org.aery.study.vf.tool.http.ParagraphMergerBuilder;
import org.aery.study.vf.vo.VersionFeature;
import org.aery.study.vf.vo.VersionFeaturePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class VersionFeatureFromOfficialWebsite implements VersionFeatureSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value(ValueEL.Source.OW_URL)
    private String fromURL;

    @Autowired
    private HttpRetriever httpRetriever;

    @Autowired
    private VersionFeatureConverter versionFeatureConverter;

    @PostConstruct
    public void initial() {
        this.logger.info("fromURL : {}", this.fromURL);
    }

    @Override
    public VersionFeaturePack get() throws Exception {
        ParagraphMerger merger = new ParagraphMergerBuilder()
                .name("Features-Schedule")
                .startAnchor("<h2 id=\"Features\">Features</h2>")
                .endAnchor("<h2 id=\"Schedule\">Schedule</h2>")
                .mergeAction(new AnchorMerger("</a>"))
                .build();
        List<String> lines = this.httpRetriever.byLine(this.fromURL, merger);

        lines.forEach(this.logger::info);

        List<VersionFeature> versionFeatures = lines.stream().map(this.versionFeatureConverter::convert).filter(Objects::nonNull).toList();
        return new VersionFeaturePack(this.fromURL, versionFeatures);
    }

}
