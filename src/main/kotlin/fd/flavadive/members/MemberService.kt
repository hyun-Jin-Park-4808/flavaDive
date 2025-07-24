package fd.flavadive.members

import fd.flavadive.exception.ErrorCode
import fd.flavadive.exception.FlavaException
import fd.flavadive.members.dto.GetUserResponse
import fd.flavadive.repositories.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {

    @Transactional
    fun getUser(userId: Long): GetUserResponse {
        val member = memberRepository.findById(userId)
            .orElseThrow { FlavaException(ErrorCode.NOT_FOUND) }

        return GetUserResponse(
            nickname = member.nickname,
            mannerScore = member.mannerScore,
            isMonthlyEvaluator = member.isMonthlyEvaluator,
        )
    }
}