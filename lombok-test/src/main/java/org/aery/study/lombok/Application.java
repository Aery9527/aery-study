package org.aery.study.lombok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext application = SpringApplication.run(Application.class, args)) {
            Kerker kerker = application.getBean(Kerker.class);
            kerker.wtf();
        }
    }

}
