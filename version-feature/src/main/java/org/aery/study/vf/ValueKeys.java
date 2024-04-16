package org.aery.study.vf;

public class ValueKeys {

    public static final String PREFIX = "aery.study.vf";

    public static final String TARGET = PREFIX + ".target";

    public static class Source {
        public static final String PREFIX = ValueKeys.PREFIX + ".source";

        public static final String OFFICIAL_WEBSITE = PREFIX + ".official-website";
        public static final String OW_URL = OFFICIAL_WEBSITE + ".url";
    }

    public static class Destination {
        public static final String PREFIX = ValueKeys.PREFIX + ".destination";

        public static final String MARKDOWN_FILE = PREFIX + ".md-file";
        public static final String MDF_NAME = MARKDOWN_FILE + ".name";
    }

    public static class HttpRetriever {
        public static final String PREFIX = ValueKeys.PREFIX + ".http-retriever";

        public static final String PRINT_ALL_LINE = PREFIX + ".print-all-line";
    }

}
