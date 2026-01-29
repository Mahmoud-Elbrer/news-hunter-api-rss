package com.newshunter.news_fetcher_service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
         template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();

        // template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//
//        RedisStandaloneConfiguration config =
//                new RedisStandaloneConfiguration(
//                        "redis-11853.c8.us-east-1-3.ec2.cloud.redislabs.com",
//                        11853
//                );
//        config.setPassword(RedisPassword.of("6WZ9Jakv8VD8ltjiJGTJkLa7BYVN7872"));
//
//        LettuceClientConfiguration clientConfig =
//                LettuceClientConfiguration.builder()
//                        .useSsl()  // 🔥 مهم جدًا لـ Redis Cloud
//                        .build();
//
//        return new LettuceConnectionFactory(config, clientConfig);
//    }

}

// database-MKX5BV83
