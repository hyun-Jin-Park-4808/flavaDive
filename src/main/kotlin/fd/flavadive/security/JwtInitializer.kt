package fd.flavadive.security

import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtInitializer(
    @Value("\${jwt.secret}") private val secret: String
) {
    @PostConstruct
    fun init() {
        TokenProvider.SECRET = secret
        TokenProvider.KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))
    }
}