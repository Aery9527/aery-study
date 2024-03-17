package org.aery.study.java.module.utils.tool;

import java.io.File;

public class PathUtil {

    public static String mixRootPath(String... folders) {
        String rootPath = System.getProperty("user.dir");
        return rootPath + File.separator + String.join(File.separator, folders);
    }

}
