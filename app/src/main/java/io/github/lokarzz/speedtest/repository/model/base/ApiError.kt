package io.github.lokarzz.speedtest.repository.model.base

data class ApiError(
    val status: Status? = Status.UNKNOWN, val code: Int? = 0, val message: String? = ""
) {
    enum class Status {
        UNKNOWN, NO_NETWORK, API, EXCEPTION
    }
}