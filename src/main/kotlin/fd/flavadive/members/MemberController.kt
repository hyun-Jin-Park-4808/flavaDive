package fd.flavadive.members

import fd.flavadive.common.response.ApiResponse
import fd.flavadive.members.dto.GetUserResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

}