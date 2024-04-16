package org.aery.study.vf.destination.impl;

import jakarta.annotation.PostConstruct;
import org.aery.study.vf.ValueEL;
import org.aery.study.vf.destination.api.VersionFeatureDestination;
import org.aery.study.vf.vo.VersionFeature;
import org.aery.study.vf.vo.VersionFeaturePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class VersionFeatureToMarkdown implements VersionFeatureDestination {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value(ValueEL.TARGET)
    private String target;

    @Value(ValueEL.Destination.MDF_NAME)
    private String fileName;

    private File targetFile;

    @PostConstruct
    public void initial() throws URISyntaxException {
        URL url = ClassLoader.getSystemResource("");
        Path path = Paths.get(url.toURI());
        Path projectPath = path.getParent().getParent().getParent();
        projectPath = projectPath.resolve(this.fileName);
        this.targetFile = projectPath.toFile();

        this.logger.info("projectPath : {}", this.targetFile);
    }

    @Override
    public void apply(VersionFeaturePack versionFeaturePack) throws Exception {
        String source = versionFeaturePack.source();
        List<VersionFeature> versionFeatures = versionFeaturePack.versionFeatures();

        this.logger.info("receive version feature count : {}", versionFeatures.size());

        this.targetFile.delete();
        this.targetFile.createNewFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            writer.write("## [form OpenJDK - " + this.target + "](" + source + ")");
            writer.newLine();
            writer.newLine();

            for (VersionFeature versionFeature : versionFeatures) {
                int jep = versionFeature.jep();
                String feature = versionFeature.feature();
                String link = versionFeature.link();

                writer.write("- [" + jep + " : " + feature + "](" + link + ")");
                writer.newLine();
            }

            writer.flush();
        }
    }

}
