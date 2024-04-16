package org.aery.study.vf.source.api;

import org.aery.study.vf.vo.VersionFeaturePack;

public interface VersionFeatureSource {

    VersionFeaturePack get() throws Exception;

}
