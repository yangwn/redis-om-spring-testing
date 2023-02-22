//package com.example.redisjsonindex.config;
//
//import org.springframework.beans.PropertyAccessor;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
///**
// * @author Bruce.Yang
// * @date 2023/2/7 -13:24
// */
//@Configuration
//@EnableCaching
//public class RedisCacheableSupport extends CachingConfigurerSupport {
//
////    /**
////     * 缓存管理器
////     */
////    @Bean
////    public RedisCacheManager cacheManager(JedisConnectionFactory jedisConnectionFactory) {
////        return RedisCacheManager.create(jedisConnectionFactory);
////    }
//
////    /**
////     * RedisTemplate配置
////     *
////     * @param jedisConnectionFactory
////     * @return
////     */
////    @Bean
////    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory ) {
////        //设置序列化
////        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
////        ObjectMapper om = new ObjectMapper();
////        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
////        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
////        jackson2JsonRedisSerializer.setObjectMapper(om);
////        //配置redisTemplate
////        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
////        redisTemplate.setConnectionFactory(jedisConnectionFactory);
////        RedisSerializer stringSerializer = new StringRedisSerializer();
////        redisTemplate.setKeySerializer(stringSerializer);//key序列化
////        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);//value序列化
////        redisTemplate.setHashKeySerializer(stringSerializer);//Hash key序列化
////        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);//Hash value序列化
////        redisTemplate.afterPropertiesSet();
////        return redisTemplate;
////    }
//
//}
