@file:Suppress("UNCHECKED_CAST")
package io.github.lokarzz.speedtest.extensions

import io.github.lokarzz.speedtest.extensions.GsonExtension.fetchData
import io.github.lokarzz.speedtest.repository.model.base.ApiError
import io.github.lokarzz.speedtest.repository.model.base.ErrorData
import io.github.lokarzz.speedtest.repository.model.base.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.UnknownHostException

object ApiExtension {

    /**
     * Fetch response
     *
     * @param T the type of a member in this function
     * @param apiCall the function to be called
     * @return this function return success/error UIState
     */
    suspend fun <T> fetchResponse(apiCall: suspend () -> Response<T>): UIState<T> {
        return withContext(Dispatchers.Default) {
            try {
                val response = apiCall()
                when {
                    response.isSuccessful && response.body() != null -> {
                        UIState.success(response.body())
                    }
                    else -> {
                        response.catchError()
                    }
                }
            } catch (e: Exception) {
                e.catchException()
            }
        }
    }

    /**
     * Catch exception
     *
     * @param T the type of a member in this function
     * @return this function return error status UIState
     */
    private fun <T> Exception.catchException(): UIState<T> {
        return UIState.error(
            ApiError(
                status = when (this) {
                    is UnknownHostException -> {
                        ApiError.Status.NO_NETWORK
                    }
                    else -> {
                        ApiError.Status.UNKNOWN
                    }
                }, message = message
            )
        )
    }

    /**
     * Catch exception
     *
     * @param T the type of a member in this function
     * @return this function return api error UIState
     */
    private fun <T> Response<T>.catchError(): UIState<T> {
        val errorMessage =
            errorBody()?.charStream()?.fetchData(ErrorData::class.java)?.errorMessage ?: ""
        return UIState.error(ApiError(ApiError.Status.API, message = errorMessage, code = code()))
    }

}