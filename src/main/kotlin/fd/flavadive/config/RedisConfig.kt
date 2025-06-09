package fd.flavadive.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableRedisRepositories
class RedisConfig(
    @Value("\${spring.data.redis.port}") private val redisPort: Int,
    @Value("\${spring.data.redis.host}") private val redisHost: String
) {
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory =
        LettuceConnectionFactory(redisHost, redisPort)

    @Bean
    fun redisTemplate(): RedisTemplate<String, String> =
        RedisTemplate<String, String>().apply {
            keySerializer = StringRedisSerializer()
            valueSerializer = StringRedisSerializer()
            setConnectionFactory(redisConnectionFactory())
        }
}