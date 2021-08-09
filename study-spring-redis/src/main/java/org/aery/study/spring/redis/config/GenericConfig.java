package org.aery.study.spring.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.keyvalue.core.mapping.KeySpaceResolver;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.mapping.BasicRedisPersistentEntity;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.core.mapping.RedisPersistentEntity;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.util.TypeInformation;

import java.lang.reflect.Field;

@Configuration
@EnableRedisRepositories(
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
            this.timeToLiveAccessor = (TimeToLiveAccessor) timeToLiveAccessorField.get(this);
            timeToLiveAccessorField.setAccessible(false);
        }

        @Override
        protected <T> RedisPersistentEntity<T> createPersistentEntity(TypeInformation<T> typeInformation) {
            return new BasicRedisPersistentEntity<T>(typeInformation, this.fallbackKeySpaceResolver, this.timeToLiveAccessor) {
                @Override
                public String getKeySpace() {
                    return "kerker:" + super.getKeySpace();
                }
            };
        }
    }

    /**
     * 一定要這個bean name才會生效
     *
     * @return
     */
    @Bean
    public RedisMappingContext keyValueMappingContext() throws NoSuchFieldException, IllegalAccessException { // 一定要這個bean name
        IndexConfiguration indexConfiguration = new IndexConfiguration();
        KeyspaceConfiguration keyspaceConfiguration = new CustomKeyspaceConfiguration();
        MappingConfiguration mappingConfiguration = new MappingConfiguration(indexConfiguration, keyspaceConfiguration);
        return new CustomRedisMappingContext(mappingConfiguration);
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
