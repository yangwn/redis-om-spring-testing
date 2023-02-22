package com.example.redisjsonindex.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.providers.PooledConnectionProvider;

/**
 * @author Bruce.Yang
 * @date 2023/2/7 -12:10
 */
@Configuration
public class UnifiedJedisClientConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                // 转换为格式化的json
                .enable(SerializationFeature.INDENT_OUTPUT)
                // 如json中存在新增字段且是实体类中不存在的, 不进行报错
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)
                .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
                .setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT)
                .registerModule(new JavaTimeModule());
    }

    @Bean
    public UnifiedJedis unifiedJedis() {
        HostAndPort hostAndPort = new HostAndPort(redisProperties.getHost(), redisProperties.getPort());
        DefaultJedisClientConfig jedisClientConfig = DefaultJedisClientConfig.builder()
                .timeoutMillis(redisProperties.getTimeout().toMillisPart())
                .connectionTimeoutMillis(redisProperties.getConnectTimeout().toMillisPart())
                .password(redisProperties.getPassword())
                .build();
        PooledConnectionProvider provider = new PooledConnectionProvider(hostAndPort, jedisClientConfig);
        return new UnifiedJedis(provider);
    }

    @Bean
    public ClazzInstanceSupport clazzInstanceSupport() {
        return new ClazzInstanceSupport();
    }

}
