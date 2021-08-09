package org.aery.study.spring.redis.config;

import org.springframework.data.redis.core.convert.KeyspaceConfiguration;

public class CustomKeyspaceConfiguration extends KeyspaceConfiguration {

    @Override
    public boolean hasSettingsFor(Class<?> type) {
        return super.hasSettingsFor(type);
    }



}
