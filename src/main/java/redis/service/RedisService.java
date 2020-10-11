package redis.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisService {

	@Autowired
	@Qualifier(value="redisTemplate")
	protected RedisTemplate<String, String> redisTemplate;

	public void saveKey(String topic, String key, String value) {
		String hashValue = topic + ":" + key;

		//value = "{\"field\":\"valor\"}";
		

		try {
			if (!isKeyRecorded(hashValue)) {
				this.redisTemplate.execute((RedisCallback<String>) connection -> {
					getRedisConnection().hashCommands().hSetNX(
							hashValue.getBytes()
							,"value".getBytes()
							,value.getBytes()
					);
					this.redisTemplate.expire(hashValue, 3600, TimeUnit.SECONDS);
//					getRedisConnection().expire(key.getBytes(), 30);
					
//					jedis.expire(key, cacheSeconds);
					
//					getRedisConnection().setEx(
//							hashValue.getBytes()
//							,3600
//							,value.getBytes()
//					);
					
					
//				getRedisConnection().ttl(hashValue.getBytes(), TimeUnit.values());
					
				return null;
				});
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	private boolean isKeyRecorded(String key){
		byte[] retorno = getRedisConnection().get(key.getBytes());
		return retorno != null && retorno.length > 0;
	}

	public String getKeyValue(String key){
		String result = "";
		try {
				
				result = this.redisTemplate.execute((RedisCallback<String>) connection -> {
				byte[] b = getRedisConnection().hashCommands().hGet(key.getBytes(), "value".getBytes());
//				byte[] a = getRedisConnection().hashCommands().hGet(key.getBytes(), "id".getBytes());
				
					
				String s = redisTemplate.getDefaultSerializer().deserialize(b).toString();
//				String s2 = redisTemplate.getDefaultSerializer().deserialize(a).toString();
				
				
				/*	
				byte[] b = getRedisConnection().dump(key.getBytes());
				*/
				//String s = new String(b, StandardCharsets.UTF_8);
				return s;
			});
		}catch(NullPointerException e){
			result = "404 - " + key + " nao foi encontrado";
		}

		return result;
	}

	private RedisConnection getRedisConnection(){
		try {
			return this.redisTemplate.getConnectionFactory().getConnection();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		return null;
	}
}
