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

    @Reference // 但查詢時若有該資料, 會直接幫你序列化好放進來
    private Woman mother;

    @Reference // 預設這邊會用Person的keyspace去儲存, 不會用子類的keyspace, 所以這樣reference會無法找到明確的子類資料
    private List<Person> favoriteArtists;

    @Reference // 若要reference子類就要明確指定型別了...
    private List<Man> favoriteMans;

    @Reference
    private List<Woman> favoriteWomans;

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

    public List<Person> getFavoriteArtists() {
        return favoriteArtists;
    }

    public void setFavoriteArtists(List<Person> favoriteArtists) {
        this.favoriteArtists = favoriteArtists;
    }

    public List<Man> getFavoriteMans() {
        return favoriteMans;
    }

    public void setFavoriteMans(List<Man> favoriteMans) {
        this.favoriteMans = favoriteMans;
    }

    public List<Woman> getFavoriteWomans() {
        return favoriteWomans;
    }

    public void setFavoriteWomans(List<Woman> favoriteWomans) {
        this.favoriteWomans = favoriteWomans;
    }
}
