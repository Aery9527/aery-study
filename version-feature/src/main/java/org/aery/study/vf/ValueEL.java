package org.aery.study.vf;

public class ValueEL {

    public static final String TARGET = "${" + ValueKeys.TARGET + "}";

    public static class Source {
        public static final String OW_URL = "${" + ValueKeys.Source.OW_URL + "}";
    }

    public static class Destination {
        public static final String MDF_NAME = "${" + ValueKeys.Destination.MDF_NAME + "}";
    }

    public static class HttpRetriever {
        public static final String PRINT_ALL_LINE = "${" + ValueKeys.HttpRetriever.PRINT_ALL_LINE + ":false}";
    }

}
