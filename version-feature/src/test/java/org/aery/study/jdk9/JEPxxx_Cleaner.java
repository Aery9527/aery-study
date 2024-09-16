package org.aery.study.jdk9;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Cleaner;

public class JEPxxx_Cleaner {

    private static final Logger LOGGER = LoggerFactory.getLogger(JEPxxx_Cleaner.class);

    static class Resource implements AutoCloseable {

        private static final Cleaner CLEANER = Cleaner.create();

        private final Cleaner.Cleanable cleanable;

        public Resource() {
            cleanable = CLEANER.register(this, new CleanupTask());
        }

        @Override
        public void close() {
            cleanable.clean();
        }
    }

    public static class CleanupTask implements Runnable {
        @Override
        public void run() {
            LOGGER.info("Resource cleaned up!");
        }
    }

    public static void main(String[] args) {
        try (Resource resource = new Resource()) {
            LOGGER.info("Using resource");
        }
    }
}
