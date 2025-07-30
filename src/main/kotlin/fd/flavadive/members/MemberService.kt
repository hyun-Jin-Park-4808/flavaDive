package fd.flavadive.members

import fd.flavadive.common.response.ApiResponse
import fd.flavadive.common.response.Success
import fd.flavadive.exception.ErrorCode
import fd.flavadive.exception.FlavaException
import fd.flavadive.members.dto.GetUserResponse
import fd.flavadive.members.dto.UpdateUserRequest
import fd.flavadive.repositories.MemberRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

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

    @Transactional
    fun updateUser(email: String, request: UpdateUserRequest): Success {
        val member = memberRepository.findByEmail(email)
        ?: throw FlavaException(ErrorCode.NOT_FOUND)

        request.email?.let { member.email = it }
        request.nickname?.let { member.nickname = it }
        request.phoneNumber?.let { member.phoneNumber = it }
        request.isNotificationEnabled?.let { member.isNotificationEnabled = it }

        return Success(true)
    }
}