package fd.flavadive.security

import RefreshToken
import fd.flavadive.exception.ErrorCode
import fd.flavadive.exception.FlavaException
import fd.flavadive.repositories.MemberRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.util.StringUtils
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.SecretKey

@Slf4j
class TokenProvider {
    private val redisTemplate: RedisTemplate<String, String>? = null
    private val memberRepository: MemberRepository? = null

    fun generateAccessToken(email: String?): String {
        val claims: Claims = Jwts.claims().setSubject(email)

        val now = Date()
        val expiredDate = Date(now.time + ACCESS_TOKEN_EXPIRE_TIME)

        return Jwts.builder()
            .setClaims(claims) // 발행 유저 정보 저장
            .setIssuedAt(now) // 토큰 생성 시간
            .setExpiration(expiredDate) // 토큰 만료 시간
            .signWith(KEY) // 사용할 암호화 알고리즘, 비밀키
            .compact() // 생성
    }

    fun generateAndSaveRefreshToken(email: String): String {
        val refreshToken = RefreshToken(email, UUID.randomUUID().toString())
        val token: String = refreshToken.refreshToken

        redisTemplate?.opsForValue()?.set(
            email,  // redis에서 사용할 key
            token,  // redis에서 사용할 value
            REFRESH_TOKEN_EXPIRE_TIME,  // refresh token이 저장되는 기간
            TimeUnit.MILLISECONDS
        )
        return token
    }

    fun getAuthentication(token: String?): Authentication {
        val email = this.getUserEmail(token) // 토큰에서 이메일 추출
        val userDetails: UserDetails = memberRepository?.findByEmail(email)
            ?: throw FlavaException(ErrorCode.NOT_FOUND)

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUserEmail(token: String?): String {
        return parseClaims(token).subject
    }

    fun validateToken(token: String): Boolean {
        if (!StringUtils.hasText(token)) {
            return false
        }
        validateTokenNotLoggedOut(token)
        val claims: Claims = this.parseClaims(token)
        return !claims.expiration.before(Date())
    }

    private fun validateTokenNotLoggedOut(accessToken: String) {
        if (accessToken == redisTemplate?.opsForValue()?.get(getUserEmail(accessToken))) {
            throw FlavaException(ErrorCode.TOKEN_EXPIRED)
        }
    }

    private fun parseClaims(token: String?): Claims {
        try {
            return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).body
        } catch (e: ExpiredJwtException) {
            log.error("만료된 토큰입니다.")
            return e.claims
        }
    }

    fun getExpiration(token: String?): Long {
        val expiration: Date =
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).body.expiration
        val now = Date().time
        return (expiration.time - now)
    }

    fun generateResetToken(email: String?): String {
        val claims: Claims = Jwts.claims().setSubject(email)

        val now = Date()
        val expiredDate = Date(now.time + RESET_TOKEN_EXPIRE_TIME)

        return Jwts.builder()
            .setClaims(claims) // 발행 유저 정보 저장
            .setIssuedAt(now) // 토큰 생성 시간
            .setExpiration(expiredDate) // 토큰 만료 시간
            .signWith(KEY) // 사용할 암호화 알고리즘, 비밀키
            .compact() // 생성
    }

    fun validateResetToken(token: String?): Boolean {
        if (!StringUtils.hasText(token)) {
            return false
        }
        val claims: Claims = this.parseClaimsForResetToken(token)
        return !claims.expiration.before(Date())
    }

    private fun parseClaimsForResetToken(token: String?): Claims {
        try {
            return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).body
        } catch (e: ExpiredJwtException) {
            log.error("만료된 토큰입니다.")
            return e.claims
        }
    }

    fun getUserEmailForResetToken(token: String?): String {
        return parseClaimsForResetToken(token).subject
    }

    companion object {
        private const val ACCESS_TOKEN_EXPIRE_TIME = (1000 * 60 * 60).toLong()
        private const val REFRESH_TOKEN_EXPIRE_TIME = (1000 * 60 * 60 * 24 * 14).toLong()
        private const val RESET_TOKEN_EXPIRE_TIME = (1000 * 60 * 60).toLong()
        lateinit var SECRET: String
        lateinit var KEY: SecretKey
    }
}