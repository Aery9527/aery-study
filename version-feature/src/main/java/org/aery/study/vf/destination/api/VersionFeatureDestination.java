package org.aery.study.vf.destination.api;

import org.aery.study.vf.vo.VersionFeaturePack;

public interface VersionFeatureDestination {

    void apply(VersionFeaturePack versionFeatures) throws Exception;

}
