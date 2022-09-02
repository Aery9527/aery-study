package org.aery.study.lombok;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class Kerker {

    private String a;

    private String b;

    public void wtf() {
        log.info("???");
    }

}
