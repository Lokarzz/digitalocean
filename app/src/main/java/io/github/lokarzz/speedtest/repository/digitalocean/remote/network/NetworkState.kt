package io.github.lokarzz.speedtest.repository.digitalocean.remote.network

data class NetworkState(
    var progress: Int? = 0,
    var status: Status? = null,
    var timeFinishedInMillis: Long? = null,
    var fileSize: String? = null,
    var errMessage: String? = null,
    var throwable: Throwable? = null
) {
    enum class Status {
        DONE, IN_PROGRESS, ERROR
    }

    companion object {
        fun done(timeFinishedInMillis: Long, fileSize: String): NetworkState {
            return NetworkState(
                status = Status.DONE,
                timeFinishedInMillis = timeFinishedInMillis,
                fileSize = fileSize,
                progress = 100
            )
        }

        fun inProgress(progress: Int): NetworkState {
            return NetworkState(
                status = Status.IN_PROGRESS,
                progress = progress,
            )
        }

        fun error(throwable: Throwable, fileSize: String): NetworkState {
            return NetworkState(
                status = Status.ERROR,
                errMessage = throwable.message,
                throwable = throwable,
                fileSize = fileSize
            )
        }

    }
}