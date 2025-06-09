package fd.flavadive.auth.dto

data class SignInRequest(
    val email: String,
    val password: String
)

data class SignInResponse(
    val accessToken: String,
    val refreshToken: String
)
