package io.github.lokarzz.speedtest.repository.model.base

data class UIState<T>(
    val status: Status? = null,
    val data: T? = null,
    val error: ApiError? = null,
    val loadingData: LoadingData? = null
) {

    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(data: T?): UIState<T> {
            return UIState(status = Status.SUCCESS, data = data)
        }

        fun <T> error(data: ApiError?): UIState<T> {
            return UIState(status = Status.ERROR, error = data)
        }


        fun <T> loading(loadingData: LoadingData?): UIState<T> {
            return UIState(status = Status.LOADING, loadingData = loadingData)
        }
    }

}
