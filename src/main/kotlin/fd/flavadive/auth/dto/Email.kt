package fd.flavadive.auth.dto


data class FindEmailRequest(
    val phoneNumber: String
)

data class FindEmailResponse(
    val email: String
)


data class CheckEmailRequest(
    val email: String
)

data class CheckEmailResponse(
    val available: Boolean
)