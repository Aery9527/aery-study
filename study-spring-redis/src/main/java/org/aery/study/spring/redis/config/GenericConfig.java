package org.aery.study.spring.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.keyvalue.core.event.KeyValueEvent;
import org.springframework.data.keyvalue.core.mapping.KeySpaceResolver;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.mapping.BasicRedisPersistentEntity;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.core.mapping.RedisPersistentEntity;
import org.springframework.data.redis.listener.adapter.RedisListenerExecutionFailedException;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.util.TypeInformation;

import java.lang.reflect.Field;

@Configuration
@EnableRedisRepositories(
//        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP
//        keyspaceConfiguration = CustomKeyspaceConfiguration.class // 也會是spring的bean, 因此可以使用autowrite
)
//@EnableTransactionManagement
public class GenericConfig {

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
//        return new LettuceConnectionFactory(config); // use Lettuce
//    }

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
//        return new JedisConnectionFactory(config); // use Jedis
//    }

    public class CustomRedisMappingContext extends RedisMappingContext {

        private final KeySpaceResolver fallbackKeySpaceResolver;

        private final TimeToLiveAccessor timeToLiveAccessor;

        public CustomRedisMappingContext(MappingConfiguration mappingConfiguration) throws NoSuchFieldException, IllegalAccessException {
            super(mappingConfiguration);

            Field resolverField = RedisMappingContext.class.getDeclaredField("fallbackKeySpaceResolver");
            resolverField.setAccessible(true);
            this.fallbackKeySpaceResolver = (KeySpaceResolver) resolverField.get(this);
            resolverField.setAccessible(false);

            Field timeToLiveAccessorField = RedisMappingContext.class.getDeclaredField("timeToLiveAccessor");
            timeToLiveAccessorField.setAccessible(true);

            this.timeToLiveAccessor = new CustomTimeToLiveAccessor((TimeToLiveAccessor) timeToLiveAccessorField.get(this));
            timeToLiveAccessorField.setAccessible(false);
        }

        @Override
        protected <T> RedisPersistentEntity<T> createPersistentEntity(TypeInformation<T> typeInformation) {
            return new CustomBasicRedisPersistentEntity<>(typeInformation, this.fallbackKeySpaceResolver, this.timeToLiveAccessor);
        }
    }

    /**
     * 為所有keyspace加上prefix
     */
    public static class CustomBasicRedisPersistentEntity<T> extends BasicRedisPersistentEntity<T> {

        private final String keyspace;

        public CustomBasicRedisPersistentEntity(TypeInformation<T> information, KeySpaceResolver fallbackKeySpaceResolver, TimeToLiveAccessor timeToLiveAccessor) {
            super(information, fallbackKeySpaceResolver, timeToLiveAccessor);

            String prefix = "kerker:"; // 替所有的keyspace加上prefix
            Class<T> type = information.getType();
            this.keyspace = prefix + fallbackKeySpaceResolver.resolveKeySpace(type);
        }

        @Override
        public String getKeySpace() {
            return this.keyspace;
        }
    }

    /**
     * 為所有資料加上預設的TTL
     */
    public static class CustomTimeToLiveAccessor implements TimeToLiveAccessor {

        private final TimeToLiveAccessor rawTimeToLiveAccessor;

        public CustomTimeToLiveAccessor(TimeToLiveAccessor rawTimeToLiveAccessor) {
            this.rawTimeToLiveAccessor = rawTimeToLiveAccessor;
        }

        @Override
        public Long getTimeToLive(Object source) {
            Long ttl = this.rawTimeToLiveAccessor.getTimeToLive(source);
            if (ttl == null || ttl <= 0) {
                return 60L * 30; // 預設30分鐘
            } else {
                return ttl;
            }
        }

        @Override
        public boolean isExpiringEntity(Class<?> type) {
            return true; // 因為修改邏輯always有TTL, 所以邊直接為傳true
        }
    }

    /**
     * 用來檢查id是否有非法字元(可以再加上對{@link org.springframework.data.redis.core.index.Indexed}的檢查)
     */
    public static class RedisIdVerifier {

        @EventListener
        public void beforeInsert(KeyValueEvent.BeforeInsertEvent<?> event) {
            String keyspace = event.getKeyspace();
            Object key = event.getKey();
            Object payload = event.getPayload();
            Class<?> type = event.getType();
            Object source = event.getSource();
            verify(key.toString(), event);
        }

        @EventListener
        public void beforeUpdate(KeyValueEvent.BeforeUpdateEvent<?> event) {
            String keyspace = event.getKeyspace();
            Object key = event.getKey();
            Object payload = event.getPayload();
            Class<?> type = event.getType();
            Object source = event.getSource();
            verify(key.toString(), event);
        }

        public void verify(String id, KeyValueEvent<?> event) {
            if (id.contains(":")) {
                throw new RedisListenerExecutionFailedException("id can't contains `:` of " + event);
            }
        }
    }

    /**
     * 一定要這個bean name才會生效
     *
     * @return
     */
    @Bean
    public RedisMappingContext keyValueMappingContext() throws NoSuchFieldException, IllegalAccessException {
        IndexConfiguration indexConfiguration = new IndexConfiguration();
        KeyspaceConfiguration keyspaceConfiguration = new CustomKeyspaceConfiguration();
        MappingConfiguration mappingConfiguration = new MappingConfiguration(indexConfiguration, keyspaceConfiguration);
        return new CustomRedisMappingContext(mappingConfiguration);
    }

    @Bean
    public RedisIdVerifier redisIdVerifier() {
        return new RedisIdVerifier();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Object> jacksonSeial = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jacksonSeial.setObjectMapper(om);

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(jacksonSeial);
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(jacksonSeial);
//        redisTemplate.setDefaultSerializer(jacksonSeial);
//        redisTemplate.setEnableTransactionSupport(true);

        return redisTemplate;
    }

    @Bean
    public ValueOperations<String, Object> redisValueOps(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public HashOperations<String, Object, Object> redisHashOps(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    public ListOperations<String, Object> redisListOps(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    public SetOperations<String, Object> redisSetOps(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean
    public ZSetOperations<String, Object> redisZsetOps(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

}
