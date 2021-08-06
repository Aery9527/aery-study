package org.aery.study.spring.redis.entity;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("man")
public class Man extends Person {

    private int size;

    public Man() {
    }

    public Man(String name) {
        super(name);
    }

    public Man(String name, int age) {
        super(name, age);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
