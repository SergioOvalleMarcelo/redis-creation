package redis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import redis.clients.jedis.Jedis;
import redis.service.RedisService;

@SpringBootApplication
public class Main{

	public static void main(String[] args) {
		
//		RedisClient redisClient = new RedisClient(
//				RedisURI.create("redis://localhost:6379"));
//		com.lambdaworks.redis.RedisConnection<String, String> connection = redisClient.connect();
//
//		System.out.println("Connected to Redis");
		
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		try {
			
	        jedis.connect();
	        
//	        No caso do redis precisar de autenticacao, descomentar linha abairo e passa o auth
	        
//	        jedis.auth("secret");
	        
	        ConfigurableApplicationContext appContext = SpringApplication.run(Main.class);
	        RedisService t =  appContext.getBean(RedisService.class);

			t.saveKey("Topic","CNPJ-ABERTO","CNPJ-TOKERIZADO");
			System.out.println(t.getKeyValue("Topic:CNPJ-ABERTO"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		jedis.close();
		
//		connection.close();
//		redisClient.shutdown();
		
	}
	
} 
