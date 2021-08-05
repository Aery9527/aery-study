package org.aery.study.spring.redis.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.*;

@RedisHash(value = "key1:key2", timeToLive = DataEntity.TTL.SECOND)
public class DataEntity {

    public static class TTL {
        public static final long SECOND = 3;
    }

    public static class Detail {
        private String a;

        private int b;

        public Detail() {
        }

        public Detail(String a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Detail detail = (Detail) o;
            return b == detail.b && Objects.equals(a, detail.a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    public DataEntity() {
    }

    public DataEntity(String id) {
        this.id = id;
    }

    @Id // 該數據唯一id, redis會建立索引
    private String id;

    @Indexed // 單純建立索引, 可以用來一次搜尋多個資料用的
    private String index1;

    @Indexed
    private String index2;

    private String parameter1;

    private String parameter2;

    //    @TimeToLive // 這個定義後會蓋過class上定義的ttl, <=0等於無ttl
    private int ttl1;

    //    @TimeToLive // 若定義多個會以最前面那個為主
    private int ttl2;

    private Date date;

    private Map<String, Detail> map;

    private List<String> list;

    private Set<String> set;

    @Override
    public String toString() {
        return "DataEntity{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataEntity that = (DataEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndex1() {
        return index1;
    }

    public void setIndex1(String index1) {
        this.index1 = index1;
    }

    public String getIndex2() {
        return index2;
    }

    public void setIndex2(String index2) {
        this.index2 = index2;
    }

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    public int getTtl1() {
        return ttl1;
    }

    public void setTtl1(int ttl1) {
        this.ttl1 = ttl1;
    }

    public int getTtl2() {
        return ttl2;
    }

    public void setTtl2(int ttl2) {
        this.ttl2 = ttl2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, Detail> getMap() {
        return map;
    }

    public void setMap(Map<String, Detail> map) {
        this.map = map;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }
}
