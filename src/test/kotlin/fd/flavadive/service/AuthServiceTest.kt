package fd.flavadive.service

import fd.flavadive.auth.AuthService
import fd.flavadive.auth.dto.SignInRequest
import fd.flavadive.auth.dto.SignUpRequest
import fd.flavadive.auth.dto.toEntity
import fd.flavadive.entities.Member
import fd.flavadive.exception.ErrorCode
import fd.flavadive.exception.FlavaException
import fd.flavadive.repositories.MemberRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.Test
import fd.flavadive.common.enums.Role
import fd.flavadive.security.TokenProvider

@ExtendWith(MockKExtension::class)
class AuthServiceTest {
    @MockK
    lateinit var memberRepository: MemberRepository

    @MockK
    lateinit var passwordEncoder: PasswordEncoder

    @MockK
    lateinit var tokenProvider: TokenProvider

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
    fun `이미 존재하는 이메일이면 예외를 던진다`() {
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
    fun `사업자 등록번호가 있으면 Role은 ADMIN으로 설정된다`() {
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
    fun `비밀번호는 암호화되어 저장된다`() {
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
    fun `저장된 Member의 id가 null이면 INVALID_REQUEST 예외를 던진다`() {
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
        email: String = "test@example.com",
        password: String = "password123"
    ) = Member(
        email = email,
        password = "encodedPassword",
        role = Role.GENERAL,
        name = "test",
        nickname = "testNickname",
        phoneNumber = "01012345678",
        isNotificationEnabled = true
    )

    @Test
    fun `멤버가 존재하고, 패스워드가 일치한다`() {
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
    fun `멤버가 존재하지 않으면 예외를 던진다`() {
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
    fun `비밀번호가 틀리면 예외를 던진다`() {
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
}