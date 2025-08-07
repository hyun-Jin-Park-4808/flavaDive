package fd.flavadive.members

import fd.flavadive.common.response.Success
import fd.flavadive.entities.Member
import fd.flavadive.exception.ErrorCode
import fd.flavadive.exception.FlavaException
import fd.flavadive.members.dto.GetMyInformationResponse
import fd.flavadive.members.dto.GetUserResponse
import fd.flavadive.members.dto.UpdateUserRequest
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

    @Transactional
    fun getMyInformation(member: Member): GetMyInformationResponse {
        return GetMyInformationResponse(
            email = member.email,
            name = member.name,
            nickname = member.nickname,
            phoneNumber = member.phoneNumber,
            mannerScore = member.mannerScore,
            isMonthlyEvaluator = member.isMonthlyEvaluator,
            businessRegistrationNumber = member.businessRegistrationNumber ?: "",
        )
    }

    @Transactional
    fun updateUser(email: String, request: UpdateUserRequest): Success {
        val member = memberRepository.findByEmail(email)
        ?: throw FlavaException(ErrorCode.NOT_FOUND)

        request.phoneNumber?.let {
            val existing = memberRepository.findByPhoneNumber(it)
            if(existing != null) {
                throw FlavaException(ErrorCode.ALREADY_EXISTED_PHONE_NUMBER)
            }
            member.phoneNumber = it }
        request.nickname?.let { member.nickname = it }
        request.isNotificationEnabled?.let { member.isNotificationEnabled = it }

        return Success(true)
    }

    @Transactional
    fun deleteUser(email: String): Success {
        val member = memberRepository.findByEmail(email)
        ?: throw FlavaException(ErrorCode.NOT_FOUND)
        memberRepository.delete(member)

        return Success(true)
    }
}