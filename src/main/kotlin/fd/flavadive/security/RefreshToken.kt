import org.springframework.data.annotation.Id

data class RefreshToken(
    @Id
    val email: String,
    var refreshToken: String
)