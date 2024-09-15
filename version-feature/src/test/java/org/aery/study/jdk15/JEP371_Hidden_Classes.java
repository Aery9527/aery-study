package org.aery.study.jdk15;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class JEP371_Hidden_Classes {

    public static class HiddenClass {
        public String convertToUpperCase(String s) {
            return s.toUpperCase();
        }
    }

    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        // 取得隱藏類的 bytecode
        byte[] classBytes = JEP371_Hidden_Classes.HiddenClass.class
                .getResourceAsStream("JEP371_Hidden_Classes$HiddenClass.class")
                .readAllBytes();

        // 定義隱藏類
        Class<?> hiddenClass = lookup
                .defineHiddenClass(classBytes, true)
                .lookupClass();

        // 創建隱藏類實例
        Object hiddenInstance = hiddenClass.getDeclaredConstructor().newInstance();

        // 調用隱藏類的方法
        Method method = hiddenClass.getDeclaredMethod("convertToUpperCase", String.class);
        System.out.println(method.invoke(hiddenInstance, "Hello"));

        // 驗證是否為隱藏類
        System.out.println("Is hidden: " + hiddenClass.isHidden());
    }

}
