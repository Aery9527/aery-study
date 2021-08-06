package org.aery.study.spring.redis.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@RedisHash("person")
public class Person {

    @Id
    private String name;

    @Indexed
    private int age;

    @Reference // 需要額外save, 從PersonRepository.save, 不會主動儲存
    private Man father;

    @Reference // 但查詢時會直接幫你查出來序列化好放進來
    private Woman mother;

    @Reference
    private List<Person> favoriteArtist;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Man getFather() {
        return father;
    }

    public void setFather(Man father) {
        this.father = father;
    }

    public Woman getMother() {
        return mother;
    }

    public void setMother(Woman mother) {
        this.mother = mother;
    }

    public List<Person> getFavoriteArtist() {
        return favoriteArtist;
    }

    public void setFavoriteArtist(List<Person> favoriteArtist) {
        this.favoriteArtist = favoriteArtist;
    }
}
