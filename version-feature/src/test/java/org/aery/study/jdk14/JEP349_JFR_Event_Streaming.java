package org.aery.study.jdk14;

import jdk.jfr.consumer.EventStream;
import org.aery.study.jdk11.JEP328_Flight_Recorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JEP349_JFR_Event_Streaming {

    private static final Logger LOGGER = LoggerFactory.getLogger(JEP349_JFR_Event_Streaming.class);

    /**
     * 可以先執行 {@link JEP328_Flight_Recorder} 來產生 myrecording.jfr, 再來執行這邊的 sample code
     */
    public static void main(String[] args) throws IOException {
        Path jfrFile = Paths.get("myrecording.jfr");
        try (EventStream eventStream = EventStream.openFile(jfrFile)) {
            eventStream.onEvent(event -> LOGGER.info("Event : {}", event.getEventType().getName()));
            eventStream.onEvent("jdk.CPULoad", event -> {
                double jvmUser = event.getDouble("jvmUser");
                double machineTotal = event.getDouble("machineTotal");
                LOGGER.info("JVM User CPU Load      : " + jvmUser);
                LOGGER.info("Machine Total CPU Load : " + machineTotal);
            });
            eventStream.start();
        }
    }

}
