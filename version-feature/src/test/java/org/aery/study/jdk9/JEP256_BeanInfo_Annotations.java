package org.aery.study.jdk9;

import java.beans.*;
import java.io.Serializable;

public class JEP256_BeanInfo_Annotations {

    /**
     * 1.一個public的無參數建構子 <br>
     * 2.屬性可以透過get、set、is（boolean）方法或遵循特定命名規則的其他方法存取, 俗稱getter/setter <br>
     * 3.可序列化 <br>
     */
    @JavaBean(description = "This is a person bean") // java 9
    public static class Person implements Serializable {
        private String name;
        private int age;

        public Person() {
        }

        @BeanProperty(description = "The name of the person") // java 9
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @BeanProperty(description = "The age of the person")
        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    /**
     * java 9 之前需要自定義 {@link Person} 的 BeanInfo, 用來告訴 {@link Introspector} 如何解析 Person 的屬性
     */
    public static class PersonBeanInfo extends SimpleBeanInfo {
        @Override
        public PropertyDescriptor[] getPropertyDescriptors() {
            try {
                PropertyDescriptor nameDescriptor = new PropertyDescriptor("name", Person.class);
                PropertyDescriptor ageDescriptor = new PropertyDescriptor("age", Person.class);
                return new PropertyDescriptor[]{nameDescriptor, ageDescriptor};
            } catch (IntrospectionException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}
