package fd.flavadive.auth.dto

data class ResetPasswordRequest(
    val phoneNumber: String,
    val newPassword: String
)
