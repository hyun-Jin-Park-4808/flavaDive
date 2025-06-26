package fd.flavadive.exception

import org.springframework.http.HttpStatus


class FlavaException (
    val errorCode: ErrorCode,
) : RuntimeException()

enum class ErrorCode(val errorMessage: String, val httpStatus: HttpStatus) {
    INVALID_REQUEST("Invalid request", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD("Wrong password", HttpStatus.BAD_REQUEST),
    SAME_PASSWORD("Same password", HttpStatus.BAD_REQUEST),
    NOT_FOUND("Not found", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_EXPIRED("Token expired", HttpStatus.UNAUTHORIZED),
    ALREADY_EXISTED_EMAIL("Already existed email.", HttpStatus.CONFLICT)
}