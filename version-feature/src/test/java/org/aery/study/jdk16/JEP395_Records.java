package org.aery.study.jdk16;

public class JEP395_Records {

    public record Person(String name, int age) {
    }

    public static void main(String[] args) {
        Person person = new Person("Aery", 30);
        System.out.println(person.name());
        System.out.println(person.age());
        System.out.println(person);
    }

}
