package org.aery.study.java.module;

import org.aery.study.java.module.api.ExportedInAPI;
import org.aery.study.java.module.utils.tool.ExportedInUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;

public class RefractionStudy {

    private interface ExceptionWrapper {
        void run() throws Exception;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RefractionStudy.class);

    public static void main(String[] args) throws Exception {
        Runnable printSplitLine = () -> {
            System.out.println();
            System.out.println("--------------------------------------------------");
            System.out.println();
        };

        // api not open module, can't access by reflection
        printSplitLine.run();
        showRefraction_ExportedInAPI();
        printSplitLine.run();
        showRefraction_HiddenInAPI();

        // utils is open module, can access by reflection
        printSplitLine.run();
        showRefraction_ExportedInUtils();
        printSplitLine.run();
        showRefraction_HiddenInUtils();
    }

    public static void showRefraction_ExportedInAPI() throws Exception {
        Class<ExportedInAPI> exportedClass = ExportedInAPI.class;

        Field exportedPublicField = exportedClass.getDeclaredField("publicField");
        LOGGER.info((String) exportedPublicField.get(exportedClass.newInstance()));

        Field exportedPrivateField = exportedClass.getDeclaredField("privateField");
        wrapException(() -> exportedPrivateField.setAccessible(true), InaccessibleObjectException.class);

        Method exportedPublicMethod = exportedClass.getDeclaredMethod("publicMethod");
        LOGGER.info((String) exportedPublicMethod.invoke(exportedClass.newInstance()));

        Method exportedPrivateMethod = exportedClass.getDeclaredMethod("privateMethod");
        wrapException(() -> exportedPrivateMethod.setAccessible(true), InaccessibleObjectException.class);
    }

    public static void showRefraction_HiddenInAPI() throws Exception {
        Class<?> hiddenClass = Class.forName("org.aery.study.java.module.api.hidden.HiddenInAPI");

        Field hiddenPublicField = hiddenClass.getDeclaredField("publicField");
        wrapException(() -> hiddenPublicField.get(hiddenClass.newInstance()), IllegalAccessException.class);

        Field hiddenPrivateField = hiddenClass.getDeclaredField("privateField");
        wrapException(() -> hiddenPrivateField.setAccessible(true), InaccessibleObjectException.class);

        Method hiddenPublicMethod = hiddenClass.getDeclaredMethod("publicMethod");
        wrapException(() -> hiddenPublicMethod.invoke(hiddenClass.newInstance()), IllegalAccessException.class);

        Method hiddenPrivateMethod = hiddenClass.getDeclaredMethod("privateMethod");
        wrapException(() -> hiddenPrivateMethod.setAccessible(true), InaccessibleObjectException.class);
    }

    public static void showRefraction_ExportedInUtils() throws Exception {
        Class<ExportedInUtils> exportedClass = ExportedInUtils.class;

        Field exportedPublicField = exportedClass.getDeclaredField("publicField");
        LOGGER.info((String) exportedPublicField.get(exportedClass.newInstance()));

        Field exportedPrivateField = exportedClass.getDeclaredField("privateField");
        exportedPrivateField.setAccessible(true);
        LOGGER.info((String) exportedPrivateField.get(exportedClass.newInstance()));

        Method exportedPublicMethod = exportedClass.getDeclaredMethod("publicMethod");
        LOGGER.info((String) exportedPublicMethod.invoke(exportedClass.newInstance()));

        Method exportedPrivateMethod = exportedClass.getDeclaredMethod("privateMethod");
        exportedPrivateMethod.setAccessible(true);
        LOGGER.info((String) exportedPrivateMethod.invoke(exportedClass.newInstance()));
    }

    public static void showRefraction_HiddenInUtils() throws Exception {
        Class<?> hiddenClass = Class.forName("org.aery.study.java.module.utils.hidden.HiddenInUtils");

        Field hiddenPublicField = hiddenClass.getDeclaredField("publicField");
        LOGGER.info((String) hiddenPublicField.get(hiddenClass.newInstance()));

        Field hiddenPrivateField = hiddenClass.getDeclaredField("privateField");
        hiddenPrivateField.setAccessible(true);
        LOGGER.info((String) hiddenPrivateField.get(hiddenClass.newInstance()));

        Method hiddenPublicMethod = hiddenClass.getDeclaredMethod("publicMethod");
        LOGGER.info((String) hiddenPublicMethod.invoke(hiddenClass.newInstance()));

        Method hiddenPrivateMethod = hiddenClass.getDeclaredMethod("privateMethod");
        hiddenPrivateMethod.setAccessible(true);
        LOGGER.info((String) hiddenPrivateMethod.invoke(hiddenClass.newInstance()));
    }

    private static void wrapException(ExceptionWrapper action, Class<? extends Exception> exceptionException) throws Exception {
        try {
            action.run();
            throw new RuntimeException("expect exception not thrown");
        } catch (Exception e) {
            if (exceptionException.isInstance(e)) {
                LOGGER.error("expect exception thrown", e);
            } else {
                throw e;
            }
        }
    }

}
