package com.leverx.dealerstat.config;

import com.leverx.dealerstat.model.ConfirmationUserCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "com.leverx.dealerstat.repository.redis")
public class RedisConfig {

  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    return new JedisConnectionFactory();
  }

  @Bean
  public RedisTemplate<String, ConfirmationUserCode> redisTemplate() {
    final RedisTemplate<String, ConfirmationUserCode> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory());
    return template;
  }
}
