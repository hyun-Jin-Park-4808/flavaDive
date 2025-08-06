package fd.flavadive.exception


import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(FlavaException::class)
    fun handleFlavaException(e: FlavaException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(e.errorCode.httpStatus)
            .body(ErrorResponse(e.errorCode.httpStatus.value(), e.errorCode.errorMessage))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        log.error(e.message, e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    ErrorCode.INTERNAL_SERVER_ERROR.httpStatus.value(),
                    e.message ?: ErrorCode.INTERNAL_SERVER_ERROR.errorMessage
                )
            )
    }
}

class ErrorResponse(
    val code: Int,
    val errorMessage: String,
)