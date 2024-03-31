package org.aery.study.java.module.test;

import org.aery.study.java.module.RefractionStudy;
import org.junit.jupiter.api.Test;

class RefractionStudyTest {

    @Test
    void showRefraction_ExportedInAPI() throws Exception {
        RefractionStudy.showRefraction_ExportedInAPI();
    }

    @Test
    void showRefraction_HiddenInAPI() throws Exception {
        RefractionStudy.showRefraction_HiddenInAPI();
    }

    @Test
    void showRefraction_ExportedInUtils() throws Exception {
        RefractionStudy.showRefraction_ExportedInUtils();
    }

    @Test
    void showRefraction_HiddenInUtils() throws Exception {
        RefractionStudy.showRefraction_HiddenInUtils();
    }

}
