package fd.flavadive.security

import fd.flavadive.exception.FlavaException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val tokenProvider: TokenProvider,
): OncePerRequestFilter() {

    companion object {
        const val TOKEN_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val pathMatcher = AntPathMatcher()
        val requestUri = request.requestURI
        println("Incoming request: $requestUri")

        for (endpoint in getPermitAllEndpoints()) {
            if (pathMatcher.match(endpoint, request.requestURI)) {
                println("Matched permitAll endpoint: $endpoint")
                filterChain.doFilter(request, response)
                return
            }
        }
        try {
            val token = resolveTokenFromRequest(request)
            if (token == null) {
                filterChain.doFilter(request, response)  // 토큰이 없어도 다음 필터로 진행
                return
            }
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) { // 토큰이 실제 텍스트를 가지고 있으며, 유효한지 검증(레디스에 동일 토큰이 없어야 함)
                val auth: Authentication = tokenProvider.getAuthentication(token) // 토큰에서 인증 정보 가져옴.
                SecurityContextHolder.getContext().authentication = auth // 인증 정보를 보안 컨텍스트에 설정
            } else {
                throw BadCredentialsException("Invalid token") // 토큰이 없거나 유효하지 않다면 에러 발생
            }
            filterChain.doFilter(request, response) // 다음 필터로 요청과 응답 전달
        } catch (e: AuthenticationException) {
            println("AuthenticationException: ${e.message}")
            jwtAuthenticationEntryPoint.commence(request, response, e)
        } catch (e: FlavaException) {
            println("FlavaException: ${e.message}")
            jwtAuthenticationEntryPoint.customCommence(response)
        }
    }

    private fun resolveTokenFromRequest(request: HttpServletRequest): String? {
        val token = request.getHeader(TOKEN_HEADER)

        return if (!token.isNullOrEmpty() && token.startsWith(TOKEN_PREFIX)) {
            token.substring(TOKEN_PREFIX.length) // Bearer 제외한 실제 토큰 부분 반환
        } else {
            null
        }
    }

    private fun getPermitAllEndpoints(): Set<String> {
        return hashSetOf(
            "/api/users/sign-up",
            "/api/auth/sign-in/**"
        )
    }
}