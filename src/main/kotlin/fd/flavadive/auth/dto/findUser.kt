package fd.flavadive.auth.dto


data class FindEmailRequest(
    val phoneNumber: String
)

data class FindEmailResponse(
    val email: String
)