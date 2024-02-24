import org.aery.study.java.module.TextGraphicsServiceEntry;
import org.aery.study.java.module.api.TextGraphicsService;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

public class TextGraphicsServiceTest {

    /**
     * find service of {@link TextGraphicsService} implementation in the module path with {@link ServiceLoader}
     * XXX why can't find any service at here??
     */
    @Test
    void findService() {
        TextGraphicsServiceEntry.main(new String[0]);
    }

}
