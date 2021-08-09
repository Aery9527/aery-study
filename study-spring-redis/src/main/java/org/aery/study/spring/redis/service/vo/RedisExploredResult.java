package org.aery.study.spring.redis.service.vo;

import org.springframework.data.redis.connection.DataType;

import java.util.*;
import java.util.function.BiConsumer;

public class RedisExploredResult {

    public static class Tuple {
        private final DataType dataType;
        private final Object data;
        private String msg;

        public Tuple(DataType dataType, Object data) {
            this.dataType = dataType;
            this.data = data;
        }

        @Override
        public String toString() {
            return toString(null);
        }

        public String toString(String key) {
            if (this.msg == null) {
                this.msg = String.format("%-5s", this.dataType) + ": ";
                this.msg += (key == null ? "" : "<" + key + "> ") + this.data;
            }
            return this.msg;
        }

        public String getDataToString() {
            return (String) this.data;
        }

        public List<String> getDataToList() {
            return (List<String>) this.data;
        }

        public Set<String> getDataToSet() {
            return (Set<String>) this.data;
        }

        public LinkedHashSet<String> getDataToZset() {
            return (LinkedHashSet<String>) this.data;
        }

        public Map<String, String> getDataToHash() {
            return (Map<String, String>) this.data;
        }

        public DataType getDataType() {
            return dataType;
        }

        public Object getData() {
            return data;
        }
    }

    private Map<String, Tuple> datas = new TreeMap<>();

    public void put(String key, DataType dataType, Object data) {
        this.datas.put(key, new Tuple(dataType, data));
    }

    public void foreach(BiConsumer<String, Tuple> consumer) {
        this.datas.forEach(consumer);
    }

    public Optional<Tuple> get(String key) {
        Tuple tuple = this.datas.get(key);
        return Optional.ofNullable(tuple);
    }

    public Map<String, Tuple> getDatas() {
        return Collections.unmodifiableMap(this.datas);
    }

}
