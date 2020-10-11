package redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories("redis.repository")
@ComponentScan("redis")
public class RedisConfig {

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}

	@Bean("redisTemplate")
	@Primary
	public <T>RedisTemplate<String, T> redisTemplate(
			final StringRedisSerializer serializer,
			final RedisConnectionFactory factory) {

		RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
		redisTemplate.setDefaultSerializer(serializer);
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(serializer);
		redisTemplate.setHashValueSerializer(serializer);
		redisTemplate.afterPropertiesSet();

		return redisTemplate;
	}
	
//	 @Bean
//    @Primary
//    public CacheManager cacheManager(RedisTemplate redisTemplate) {
//		 RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        // Number of seconds before expiration. Defaults to unlimited (0)
//        cacheManager.setDefaultExpiration(20);
//        cacheManager.setUsePrefix(true);
//        return cacheManager;
//    }

	@Bean("serializer")
	public StringRedisSerializer serializer() {
		return new StringRedisSerializer();
	}

}