package fd.flavadive.service

import fd.flavadive.common.enums.Role
import fd.flavadive.entities.Member
import fd.flavadive.exception.ErrorCode
import fd.flavadive.exception.FlavaException
import fd.flavadive.members.MemberService
import fd.flavadive.repositories.MemberRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class MemberServiceTest {
    @MockK
    lateinit var memberRepository: MemberRepository

    @InjectMockKs
    lateinit var memberService: MemberService

    @BeforeEach
    fun setUp() { // 인스턴스 안에 정의된 Mock 필드들 초기화
        MockKAnnotations.init(this) // this: 현재 테스트 클래스의 인스턴스
    }

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

    @Test
    fun `회원 조회-회원이 없으면 예외를 던진다` () {
        // given
        val userId = 1L
        every { memberRepository.findById(userId) } returns Optional.empty()

        // when & then
        val exception = assertThrows<FlavaException> {
            memberService.getUser(userId)
        }
        assertEquals(ErrorCode.NOT_FOUND, exception.errorCode)
    }

    @Test
    fun `회원 조회-회원이 존재한다` () {
        // given
        val userId = 1L
        val foundMember = createTestMember()
        every { memberRepository.findById(userId) } returns Optional.of(foundMember)

        // when
        val result = memberService.getUser(userId)

        // then
        assertEquals(foundMember.nickname, result.nickname)
        assertEquals(foundMember.mannerScore, result.mannerScore)
        assertEquals(foundMember.isMonthlyEvaluator, result.isMonthlyEvaluator)
    }
}