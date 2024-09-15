package org.aery.study.jdk17;

import java.io.*;

public class JEP415_ContextSpecific_Deserialization_Filters {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ObjectInputFilter.Config.setSerialFilterFactory((f, c) -> { // 設定全域過濾器工廠
            return info -> {
                Class<?> clazz = info.serialClass();
                if (clazz != null && String.class.isAssignableFrom(clazz)) {
                    return ObjectInputFilter.Status.ALLOWED;
                }
                return ObjectInputFilter.Status.REJECTED;
            };
        });

        // 測試反序列化
        String test = "Hello, World!";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(test);
        out.flush();
        out.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);

        // 應用過濾器進行反序列化
        Object obj = in.readObject();
        System.out.println(obj); // 預期應能反序列化 String 類型
    }

}
