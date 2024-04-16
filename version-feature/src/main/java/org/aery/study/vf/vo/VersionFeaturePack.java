package org.aery.study.vf.vo;

import java.util.List;

public record VersionFeaturePack(
        String source,
        List<VersionFeature> versionFeatures
) {
}
