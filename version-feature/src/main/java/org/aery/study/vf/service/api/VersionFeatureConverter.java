package org.aery.study.vf.service.api;

import org.aery.study.vf.vo.VersionFeature;

public interface VersionFeatureConverter {

    VersionFeature convert(String line);

}
