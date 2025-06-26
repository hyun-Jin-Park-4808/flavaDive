package fd.flavadive.auth

import fd.flavadive.auth.dto.*
import fd.flavadive.common.enums.Role
import fd.flavadive.entities.Member
import fd.flavadive.exception.ErrorCode
import fd.flavadive.exception.FlavaException
import fd.flavadive.repositories.MemberRepository
import fd.flavadive.security.TokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
) {
    @Transactional
    fun signUp(signUpRequest: SignUpRequest): Long {
        if (memberRepository.existsByEmail(signUpRequest.email) == true) {
            throw FlavaException(ErrorCode.ALREADY_EXISTED_EMAIL)
        }
            signUpRequest.businessRegistrationNumber?.let {
                signUpRequest.role = Role.ADMIN
            }
        signUpRequest.password = passwordEncoder.encode(signUpRequest.password)

        return memberRepository.save(signUpRequest.toEntity()).id
            ?: throw FlavaException(ErrorCode.INVALID_REQUEST)
    }

    @Transactional
    fun signIn(signInRequest: SignInRequest): SignInResponse {
        authenticate(signInRequest)

        val accessToken = tokenProvider.generateAccessToken(signInRequest.email)
        val refreshToken = tokenProvider.generateAndSaveRefreshToken(signInRequest.email)
        return SignInResponse(accessToken, refreshToken)
    }

    fun authenticate(signInRequest: SignInRequest): Member {
        val member = memberRepository.findByEmail(signInRequest.email)
            ?: throw FlavaException(ErrorCode.NOT_FOUND)
        if (!passwordEncoder.matches(signInRequest.password, member.password)) {
            throw FlavaException(ErrorCode.WRONG_PASSWORD)
        }
        return member
    }

    @Transactional
    fun findEmail(findEmailRequest: FindEmailRequest): FindEmailResponse {
        val email = memberRepository.findByPhoneNumber(findEmailRequest.phoneNumber)?.email
            ?: throw FlavaException(ErrorCode.NOT_FOUND)
        return FindEmailResponse(email)
    }

    @Transactional
    fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Boolean {
        val member = memberRepository.findByPhoneNumber(resetPasswordRequest.phoneNumber)
            ?: throw FlavaException(ErrorCode.NOT_FOUND)
        if (passwordEncoder.matches(resetPasswordRequest.newPassword, member.password)) {
            throw FlavaException(ErrorCode.SAME_PASSWORD)
        }

        member.password = passwordEncoder.encode(resetPasswordRequest.newPassword)

        return true
    }
}