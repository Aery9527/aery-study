package org.aery.study.spring.redis.config;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class CustomKeyspaceConfiguration extends KeyspaceConfiguration {

    private final ReentrantLock lock = new ReentrantLock();

    private final Map<Class<?>, KeyspaceSettings> settingsMap = new ConcurrentHashMap<>();

    @Override
    public boolean hasSettingsFor(Class<?> type) {
        Assert.notNull(type, "Type to lookup must not be null!");

        if (this.settingsMap.containsKey(type)) {
            return true;
        }

        lock(() -> {
            if (this.settingsMap.containsKey(type)) {
                return;
            }

            String keyspaceIncludeParents = fetchKeyspaceIncludeParents(type);

            List<Class<?>> duplicatedKeyspaceList = new ArrayList<>();
            for (KeyspaceSettings value : this.settingsMap.values()) {
                String existingKeyspace = value.getKeyspace();
                Class<?> existingType = value.getType();
                if (existingKeyspace.equals(keyspaceIncludeParents)) {
                    if (existingType.equals(type)) {
                        return;
                    } else {
                        duplicatedKeyspaceList.add(existingType);
                    }
                }
            }

            if (!duplicatedKeyspaceList.isEmpty()) {
                duplicatedKeyspaceList.add(type);
                throw new IllegalArgumentException("There are same keypsace \"" + keyspaceIncludeParents + "\" of " + duplicatedKeyspaceList);
            }

            this.settingsMap.put(type, new KeyspaceSettings(type, keyspaceIncludeParents));
        });

        return true;
    }

    @Override
    public KeyspaceSettings getKeyspaceSettings(Class<?> type) {
        hasSettingsFor(type);
        return this.settingsMap.get(type);
    }

    @Override
    public void addKeyspaceSettings(KeyspaceSettings keyspaceSettings) {
        throw new UnsupportedOperationException();
    }

    private void lock(Runnable action) {
        try {
            this.lock.lock();
            action.run();
        } finally {
            this.lock.unlock();
        }
    }

    private String fetchKeyspaceIncludeParents(Class<?> type) {
        type = ClassUtils.getUserClass(type);
        Class<?> superClass = type.getSuperclass();

        if (superClass == null) {
            return "";
        }

        String parentKeyspace = fetchKeyspaceIncludeParents(superClass);
        if (!parentKeyspace.isEmpty()) {
            parentKeyspace += "$";
        }

        String currentKeyspace;

        Class<?> userClass = ClassUtils.getUserClass(type);
        KeySpace keyspace = AnnotatedElementUtils.findMergedAnnotation(userClass, KeySpace.class);
        if (keyspace == null) {
            currentKeyspace = null;
        } else {
            Object keyspaceValue = AnnotationUtils.getValue(keyspace);
            currentKeyspace = keyspaceValue == null ? null : keyspaceValue.toString();
        }

        if (currentKeyspace == null || currentKeyspace.isEmpty()) {
            currentKeyspace = type.getSimpleName();
        }

        return parentKeyspace + currentKeyspace;
    }

}
