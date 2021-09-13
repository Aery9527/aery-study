package org.aery.study.spring.redis.entity;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("woman")
public class Woman extends Person {

    private String size;

    public Woman() {
    }

    public Woman(String name) {
        super(name);
    }

    public Woman(String name, int age) {
        super(name, age);
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
