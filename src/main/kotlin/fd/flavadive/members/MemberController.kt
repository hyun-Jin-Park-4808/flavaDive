package fd.flavadive.members

import fd.flavadive.common.response.ApiResponse
import fd.flavadive.common.response.Success
import fd.flavadive.members.dto.GetUserResponse
import fd.flavadive.members.dto.UpdateUserRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RequestMapping("api/users")
@RestController
class MemberController(
    private val memberService: MemberService,
) {
    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: Long,
    ): ResponseEntity<ApiResponse<GetUserResponse>> {
        val result = memberService.getUser(userId)
        return ResponseEntity.ok(ApiResponse(result))
    }

    @PatchMapping()
    fun updateUser(
        @Valid @RequestBody
        request: UpdateUserRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Success>> {
        val email = principal.name
        val result = memberService.updateUser(email, request)
        return ResponseEntity.ok(ApiResponse(result))
    }
}