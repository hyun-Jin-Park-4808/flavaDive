package fd.flavadive.service

import fd.flavadive.auth.AuthService
import fd.flavadive.auth.dto.*
import fd.flavadive.common.enums.Role
import fd.flavadive.common.response.Success
import fd.flavadive.entities.Member
import fd.flavadive.exception.ErrorCode
import fd.flavadive.exception.FlavaException
import fd.flavadive.repositories.MemberRepository
import fd.flavadive.security.TokenProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class AuthServiceTest {
    @MockK
    lateinit var memberRepository: MemberRepository

    @MockK
    lateinit var passwordEncoder: PasswordEncoder

    @MockK
    lateinit var tokenProvider: TokenProvider

    @MockK
    lateinit var redisTemplate: RedisTemplate<String, String>

    @InjectMockKs
    lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private fun createSignUpRequest(
        email: String = "test@example.com",
        password: String = "password123",
        businessRegistrationNumber: String? = null
    ) = SignUpRequest(
        email = email,
        password = password,
        name = "홍길동",
        nickname = "길동이",
        phoneNumber = "01012345678",
        isNotificationEnabled = true,
        businessRegistrationNumber = businessRegistrationNumber,
        businessRegistrationImagePath = null
    )

    private fun setId(member: Member, id: Long?) {
        val field = member.javaClass.superclass.getDeclaredField("id")
        field.isAccessible = true
        field.set(member, id)
    }

    @Test
    fun `회원가입-이미 존재하는 이메일이면 예외를 던진다`() {
        // given
        val request = createSignUpRequest()
        every { memberRepository.existsByEmail("test@example.com") } returns true

        // when & then
        val exception = assertThrows<FlavaException> {
            authService.signUp(request)
        }
        assertEquals(ErrorCode.ALREADY_EXISTED_EMAIL, exception.errorCode)
    }

    @Test
    fun `회원가입-사업자 등록번호가 있으면 Role은 ADMIN으로 설정된다`() {
        // given
        val request = createSignUpRequest(businessRegistrationNumber = "123-45-67890")
        val dummyMember = request.toEntity()
        setId(dummyMember, 1L)
        every { memberRepository.existsByEmail(any()) } returns false
        every { passwordEncoder.encode(any()) } returns "hashed"
        every { memberRepository.save(any()) } returns dummyMember

        // when
        val result = authService.signUp(request)

        // then
        assertEquals(1L, result)
        assertEquals(Role.ADMIN, request.role)
    }


    @Test
    fun `회원가입-비밀번호는 암호화되어 저장된다`() {
        // given
        val request = createSignUpRequest()
        val dummyMember = request.toEntity()
        setId(dummyMember, 1L)
        every { memberRepository.existsByEmail(any()) } returns false
        every { passwordEncoder.encode("password123") } returns "encoded"
        every { memberRepository.save(any()) } returns dummyMember

        // when
        val result = authService.signUp(request)

        // then
        assertEquals(1L, result)
        assertEquals("encoded", request.password)
    }

    @Test
    fun `회원가입-저장된 Member의 id가 null이면 INVALID_REQUEST 예외를 던진다`() {
        // given
        val request = createSignUpRequest()
        val dummyMember = request.toEntity()
        setId(dummyMember, null)
        every { memberRepository.existsByEmail(any()) } returns false
        every { passwordEncoder.encode(any()) } returns "encoded"
        every { memberRepository.save(any()) } returns dummyMember


        // when & then
        val exception = assertThrows<FlavaException> {
            authService.signUp(request)
        }
        assertEquals(ErrorCode.INVALID_REQUEST, exception.errorCode)
    }

    private fun createSignInRequest(
        email: String = "test@example.com",
        password: String = "password123"
    ) = SignInRequest(
        email = email,
        password = password
    )

    private fun createTestMember(
        email: String = "test@example.com"
    ) = Member(
        email = email,
        password = "encodedPassword",
        role = Role.GENERAL,
        name = "test",
        nickname = "testNickname",
        phoneNumber = "01012345678",
        isNotificationEnabled = true
    )

    private fun createFindEmailRequest(
        phoneNumber: String = "123-45-67890",
    ) = FindEmailRequest(
        phoneNumber = phoneNumber,
    )

    private fun createResetPasswordRequest(
        token: String = "resetToken",
        newPassword: String = "newPassword123"
    ) = ResetPasswordRequest(
        token = token,
        newPassword = newPassword
    )

    @Test
    fun `로그인-멤버가 존재하고, 패스워드가 일치한다`() {
        // given
        val request = createSignInRequest()
        val foundMember = createTestMember()
        val expectedAccessToken = "mocked.jwt.token"
        val expectedRefreshToken = "mocked.jwt.refresh.token"
        every { memberRepository.findByEmail(request.email) } returns foundMember
        every { passwordEncoder.matches(request.password, foundMember.password) } returns true
        every { tokenProvider.generateAccessToken(request.email) } returns expectedAccessToken
        every { tokenProvider.generateAndSaveRefreshToken(request.email) } returns expectedRefreshToken

        // when
        val result = authService.signIn(request)

        // then
        assertEquals(expectedAccessToken, result.accessToken)
        assertEquals(expectedRefreshToken, result.refreshToken)
    }

    @Test
    fun `로그인-멤버가 존재하지 않으면 예외를 던진다`() {
        // given
        val request = createSignInRequest()
        every { memberRepository.findByEmail(request.email) } returns null

        // when & then
        val exception = assertThrows<FlavaException> {
            authService.signIn(request)
        }
        assertEquals(ErrorCode.NOT_FOUND, exception.errorCode)
    }

    @Test
    fun `로그인-비밀번호가 틀리면 예외를 던진다`() {
        // given
        val request = createSignInRequest()
        val foundMember = createTestMember()
        every { memberRepository.findByEmail(request.email) } returns foundMember
        every { passwordEncoder.matches(request.password, foundMember.password) } returns false

        // when & then
        val exception = assertThrows<FlavaException> {
            authService.signIn(request)
        }
        assertEquals(ErrorCode.WRONG_PASSWORD, exception.errorCode)
    }

    @Test
    fun `아이디 찾기-멤버가 존재하지 않으면 예외를 던진다`() {
        // given
        val request = createFindEmailRequest()
        every { memberRepository.findByPhoneNumber(request.phoneNumber) } returns null

        // when & then
        val exception = assertThrows<FlavaException> {
            authService.findEmail(request)
        }
        assertEquals(ErrorCode.NOT_FOUND, exception.errorCode)
    }

    @Test
    fun `아이디 찾기-멤버가 존재한다`() {
        // given
        val request = createFindEmailRequest()
        val foundMember = createTestMember()

        every { memberRepository.findByPhoneNumber(request.phoneNumber) } returns foundMember

        // when
        val result = authService.findEmail(request)

        // then
        assertEquals(foundMember.email, result.email)
    }

    // 비밀번호 초기화
    @Test
    fun `비밀번호 찾기-토큰이 만료되었으면 예외를 던진다`() {
        // given
        val request = createResetPasswordRequest()
        every { tokenProvider.validateResetToken(request.token) } returns false

        // when & then
        val exception = assertThrows<FlavaException> {
            authService.resetPassword(request)
        }
        assertEquals(ErrorCode.TOKEN_EXPIRED, exception.errorCode)
    }

    @Test
    fun `비밀번호 찾기-토큰이 이미 사용되었으면 예외를 던진다`() {
        // given
        val request = createResetPasswordRequest()
        every { tokenProvider.validateResetToken(request.token) } returns true
        every { tokenProvider.getUserEmailForResetToken(request.token) } returns "email"
        every { redisTemplate.opsForValue().get("reset:email") } returns null

        // when & then
        val exception = assertThrows<FlavaException> {
            authService.resetPassword(request)
        }
        assertEquals(ErrorCode.ALREADY_USED_TOKEN, exception.errorCode)
    }

    @Test
    fun `비밀번호 찾기-토큰 이메일 정보에 해당하는 멤버가 없으면 예외를 던진다`() {
        // given
        val request = createResetPasswordRequest()
        every { tokenProvider.validateResetToken(request.token) } returns true
        every { tokenProvider.getUserEmailForResetToken(request.token) } returns "email"
        every { redisTemplate.opsForValue().get("reset:email") } returns request.token
        every { memberRepository.findByEmail("email") } returns null

        // when & then
        val exception = assertThrows<FlavaException> {
            authService.resetPassword(request)
        }
        assertEquals(ErrorCode.NOT_FOUND, exception.errorCode)
    }

    @Test
    fun `비밀번호 찾기-기존 비밀번호와 같은 비밀번호를 입력하면 예외를 던진다`() {
        // given
        val request = createResetPasswordRequest()
        val foundMember = createTestMember()
        every { tokenProvider.validateResetToken(request.token) } returns true
        every { tokenProvider.getUserEmailForResetToken(request.token) } returns "email"
        every { redisTemplate.opsForValue().get("reset:email") } returns request.token
        every { memberRepository.findByEmail("email") } returns foundMember
        every { passwordEncoder.matches(request.newPassword, foundMember.password) } returns true

        // when & then
        val exception = assertThrows<FlavaException> {
            authService.resetPassword(request)
        }
        assertEquals(ErrorCode.SAME_PASSWORD, exception.errorCode)
    }

    @Test
    fun `비밀번호 찾기-토큰이 유효하고, 멤버가 존재하고, 새로운 패스워드를 입력한다`() {
        // given
        val request = createResetPasswordRequest()
        val foundMember = createTestMember()
        every { tokenProvider.validateResetToken(request.token) } returns true
        every { tokenProvider.getUserEmailForResetToken(request.token) } returns "email"
        every { redisTemplate.opsForValue().get("reset:email") } returns request.token
        every { memberRepository.findByEmail("email") } returns foundMember
        every { passwordEncoder.matches(request.newPassword, foundMember.password) } returns false
        every { passwordEncoder.encode(request.newPassword) } returns "encodedNewPassword"
        every { redisTemplate.delete("reset:email") } returns true
        // when
        val result = authService.resetPassword(request)

        // then
        assertEquals(Success(true), result)
    }

    @Test
    fun `로그아웃-유저에 대한 리프레시 토큰이 레디스에 없으면 예외를 던진다`() {
        // given
        val accessToken = "access-token"
        every { tokenProvider.getUserEmail(accessToken) } returns "email@email.com"
        every { redisTemplate.opsForValue().get("email@email.com") } returns null

        // when & then
        val exception = assertThrows<FlavaException> {
            authService.logout(accessToken)
        }
        assertEquals(ErrorCode.TOKEN_EXPIRED, exception.errorCode)
    }

    @Test
    fun `로그아웃-레디스에 리프레시 토큰이 존재하면 로그아웃에 성공한다`() {
        // given
        val accessToken = "access-token"
        every { tokenProvider.getUserEmail(accessToken) } returns "email@email.com"
        every { redisTemplate.opsForValue().get("email@email.com") } returns "refresh-token"
        every { redisTemplate.delete("email@email.com") } returns true
        every { redisTemplate.opsForValue().set("email@email.com", accessToken) } just runs
        // when
        val result = authService.logout(accessToken)

        // then
        assertEquals(Success(true), result)
    }
}