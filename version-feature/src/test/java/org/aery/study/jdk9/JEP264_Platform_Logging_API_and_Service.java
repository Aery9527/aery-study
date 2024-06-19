package org.aery.study.jdk9;

import java.util.ResourceBundle;

public class JEP264_Platform_Logging_API_and_Service {

    public static void main(String[] args) {
        System.Logger logger = System.getLogger("kerker");
        logger.log(System.Logger.Level.INFO, "Hello, World!");
    }

    public static class QuizLoggerFinder extends System.LoggerFinder {
        @Override
        public System.Logger getLogger(String name, Module module) {
            return new QuizLogger(name);
        }
    }

    public static class QuizLogger implements System.Logger {
        private final String name;

        QuizLogger(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isLoggable(Level level) {
            return true;
        }

        @Override
        public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
            org.slf4j.LoggerFactory.getLogger(name).info(msg, thrown);
        }

        @Override
        public void log(Level level, ResourceBundle bundle, String format, Object... params) {
            org.slf4j.LoggerFactory.getLogger(name).info(String.format(format, params));
        }
    }

}
