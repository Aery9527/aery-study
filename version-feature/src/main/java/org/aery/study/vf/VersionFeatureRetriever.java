package org.aery.study.vf;

import org.aery.study.vf.destination.api.VersionFeatureDestination;
import org.aery.study.vf.source.api.VersionFeatureSource;
import org.aery.study.vf.vo.VersionFeaturePack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class VersionFeatureRetriever {

    public static void main(String[] args) throws Exception {
        try (ConfigurableApplicationContext application = SpringApplication.run(VersionFeatureRetriever.class, args)) {
            application.getBean(VersionFeatureRetriever.class).retrieve();
        }
    }

    @Autowired
    private VersionFeatureSource versionFeatureSource;

    @Autowired
    private VersionFeatureDestination versionFeatureDestination;

    public void retrieve() throws Exception {
        VersionFeaturePack versionFeaturePack = this.versionFeatureSource.get();
        this.versionFeatureDestination.apply(versionFeaturePack);
    }

}
