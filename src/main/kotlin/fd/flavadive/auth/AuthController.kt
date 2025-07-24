package fd.flavadive.auth

import fd.flavadive.auth.dto.*
import fd.flavadive.common.response.ApiResponse
import fd.flavadive.common.response.LastInsertedId
import fd.flavadive.common.response.Success
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("api/users")
@RestController
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/sign-up")
    fun signUp(
        @Valid @RequestBody
        signUpRequest: SignUpRequest
    ): ResponseEntity<ApiResponse<LastInsertedId>> {
        val response = authService.signUp(signUpRequest)
        return ResponseEntity.ok(ApiResponse(LastInsertedId(response)))
    }

    @PostMapping("/sign-in")
    fun signIn(
        @Valid @RequestBody
        signInRequest: SignInRequest
    ): ResponseEntity<ApiResponse<SignInResponse>> {
        val result = authService.signIn(signInRequest)
        return ResponseEntity.ok(ApiResponse(result))
    }

    @PostMapping("/find-id")
    fun findEmail(
        @Valid @RequestBody
        request: FindEmailRequest
    ): ResponseEntity<ApiResponse<FindEmailResponse>> {
        val result = authService.findEmail(request)
        return ResponseEntity.ok(ApiResponse(result))
    }

    @PatchMapping("reset-password")
    fun resetPassword(
        @Valid @RequestBody
        request: ResetPasswordRequest
    ): ResponseEntity<ApiResponse<Success>> {
        val result = authService.resetPassword(request)
        return ResponseEntity.ok(ApiResponse(result))
    }

    @PostMapping("/logout")
    fun logout(
        @RequestHeader("Authorization") authHeader: String,
    ): ResponseEntity<ApiResponse<Success>> {
        val token = authHeader.substringAfter("Bearer ")
        val result = authService.logout(token)
        return ResponseEntity.ok(ApiResponse(result))
    }
}