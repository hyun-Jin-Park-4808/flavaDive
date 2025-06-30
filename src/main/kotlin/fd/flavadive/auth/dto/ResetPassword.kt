package fd.flavadive.auth.dto

data class ResetPasswordRequest(
    val token: String,
    val newPassword: String
)
