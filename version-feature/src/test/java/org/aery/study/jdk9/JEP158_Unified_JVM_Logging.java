package org.aery.study.jdk9;

import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <pre>
 * -Xlog:help
 * -Xlog:all=warning:stderr:uptime,level,tags (default)
 * -Xlog:all=info:stdout:time,level,tags,tid,pid
 * -Xlog:gc=trace:stdout:time,level,tid,pid
 * </pre>
 */
public class JEP158_Unified_JVM_Logging {

    private static final Logger LOGGER = Logger.getLogger(JEP158_Unified_JVM_Logging.class.getName());

    public static void main(String[] args) {
        printAllLevel();
    }

    public static void printAllLevel() {
        LOGGER.severe("severe");
        LOGGER.warning("warning");
        LOGGER.info("info");
        LOGGER.config("config");
        LOGGER.fine("fine");
        LOGGER.finer("finer");
        LOGGER.finest("finest");
    }

    /**
     * all level at here : {@link java.util.logging.Level#standardLevels}
     */
    @Test
    void level() {
        Level[] standardLevels = {
                Level.OFF, // close all log
                Level.SEVERE,
                Level.WARNING,
                Level.INFO,
                Level.CONFIG,
                Level.FINE,
                Level.FINER,
                Level.FINEST,
                Level.ALL
        };

        // reverse
//        List<Object> list = Arrays.asList(standardLevels);
//        Collections.reverse(list);
//        standardLevels = list.toArray(standardLevels);

        for (Level standardLevel : standardLevels) {
            System.out.println("current Level : " + standardLevel.getName());

            LOGGER.setLevel(standardLevel);
            printAllLevel();

            System.out.println("--------------------------------------------------------");
        }
    }

}
