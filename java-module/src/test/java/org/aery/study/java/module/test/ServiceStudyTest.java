package org.aery.study.java.module.test;

import org.aery.study.java.module.ServiceStudy;
import org.aery.study.java.module.api.TextGraphicsService;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

/**
 * find service of {@link TextGraphicsService} implementation in the module path with {@link ServiceLoader}
 * XXX need explicit designation requires module x and z,
 * XXX otherwise can't find any service at here,
 * XXX because junit is automatic module, it can't find service in the module path
 */
class ServiceStudyTest {

    @Test
    void main() {
        ServiceStudy.main(new String[0]);
    }

}
