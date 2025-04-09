package fd.flavadive.common.response

data class ApiResponse<T>(
    val data: T? = null
)

data class AffectedRows(
    val affectedRows: Int
)

data class LastInsertedId(
    val lastInsertedId: Long
)

