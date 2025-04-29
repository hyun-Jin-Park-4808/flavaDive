package fd.flavadive.service

import fd.flavadive.auth.AuthService
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

@ExtendWith(MockKExtension::class)
class AuthServiceTest {
    @MockK
    lateinit var memberRepository: MemberRepository

    @MockK
    lateinit var passwordEncoder: PasswordEncoder

    @InjectMockKs
    lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private fun createRequest(
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

    fun setId(member: Member, id: Long?) {
        val field = member.javaClass.superclass.getDeclaredField("id")
        field.isAccessible = true
        field.set(member, id)
    }

    @Test
    fun `이미 존재하는 이메일이면 예외를 던진다`() {
        // given
        val request = createRequest()
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
        val request = createRequest(businessRegistrationNumber = "123-45-67890")
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
        val request = createRequest()
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
        val request = createRequest()
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
}