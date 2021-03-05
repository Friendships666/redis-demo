package org.example.config;

import org.example.intfa.SmileNameHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

@Configuration
@Import(SmileNameHandler.class)
public class RedisConfig {

    @Bean
    public Jedis jedis(){
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.auth("123456");
        return jedis;
    }
}
