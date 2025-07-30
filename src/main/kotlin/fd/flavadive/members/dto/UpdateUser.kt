package fd.flavadive.members.dto

data class UpdateUserRequest(
    val email: String? = null,
    val nickname: String? = null,
    val phoneNumber: String? = null,
    val isNotificationEnabled: Boolean? = null,
)