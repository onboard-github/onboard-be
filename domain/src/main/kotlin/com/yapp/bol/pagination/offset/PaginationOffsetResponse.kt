package com.yapp.bol.pagination.offset

data class PaginationOffsetResponse<T>(
    val content: List<T>,
    val totalCount: Long,
    val hasNext: Boolean,
) {
    fun <U> mapContents(func: (T) -> U): PaginationOffsetResponse<U> {
        return PaginationOffsetResponse(
            content = content.map(func),
            totalCount = totalCount,
            hasNext = hasNext,
        )
    }
}
