package fd.flavadive.members.dto

data class GetUserResponse(
    val nickname: String,
    val mannerScore: Long,
    val isMonthlyEvaluator: Boolean,
)

data class GetMyInformationResponse(
    val email: String,
    val name: String,
    val nickname: String,
    val phoneNumber: String,
    val mannerScore: Long,
    val isMonthlyEvaluator: Boolean,
    val businessRegistrationNumber: String? = null,
)
