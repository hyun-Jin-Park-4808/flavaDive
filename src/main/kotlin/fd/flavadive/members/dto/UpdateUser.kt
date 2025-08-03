package fd.flavadive.members.dto

data class UpdateUserRequest(
    val nickname: String? = null,
    val phoneNumber: String? = null,
    val isNotificationEnabled: Boolean? = null,
)