package com.mf.jira.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public <V> RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate <String, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // 使用GenericJackson2JsonRedisSerializer 替换默认序列化
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置key和value的序列化规则
        redisTemplate.setKeySerializer(genericJackson2JsonRedisSerializer);
        // 设置hashkey和hashvalue的序列化规则
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
