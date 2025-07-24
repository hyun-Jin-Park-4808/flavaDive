package fd.flavadive.members.dto

data class GetUserResponse(
    val nickname: String,
    val mannerScore: Long,
    val isMonthlyEvaluator: Boolean,
)