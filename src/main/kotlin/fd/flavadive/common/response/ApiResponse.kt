package fd.flavadive.common.response

data class ApiResponse<T>(
    val data: T? = null
)

data class Success(
    val success: Boolean
)

data class LastInsertedId(
    val lastInsertedId: Long
)

