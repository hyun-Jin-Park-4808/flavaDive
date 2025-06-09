package fd.flavadive.auth

import fd.flavadive.auth.dto.SignInRequest
import fd.flavadive.auth.dto.SignInResponse
import fd.flavadive.auth.dto.SignUpRequest
import fd.flavadive.common.response.ApiResponse
import fd.flavadive.common.response.LastInsertedId
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("api/users")
@RestController
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("sign-up")
    fun signUp(
        @Valid @RequestBody
        signUpRequest: SignUpRequest
    ): ResponseEntity<ApiResponse<LastInsertedId>> {
        val response = authService.signUp(signUpRequest)
        return ResponseEntity.ok(ApiResponse(LastInsertedId(response)))
    }

    @PostMapping("sign-in")
    fun signIn(
        @Valid @RequestBody
        signInRequest: SignInRequest
    ): ResponseEntity<ApiResponse<SignInResponse>> {
        val result = authService.signIn(signInRequest)
        return ResponseEntity.ok(ApiResponse(result))
    }
}