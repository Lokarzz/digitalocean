package io.github.lokarzz.speedtest.repository.digitalocean.remote.network

import io.github.lokarzz.speedtest.BuildConfig
import io.github.lokarzz.speedtest.constants.AppConstants
import io.github.lokarzz.speedtest.repository.digitalocean.remote.network.helper.TorHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import javax.inject.Inject

class DigitalOceanNetwork @Inject constructor(private val torHelper: TorHelper) {

    private var serverUrl = ""
    private var connectTorNetwork = false
    private val proxy by lazy {
        Proxy(Proxy.Type.SOCKS, InetSocketAddress("localhost", 9050))
    }


    suspend fun downloadFile(fileSize: String): Flow<NetworkState> {
        return flow {
//            if (BuildConfig.DEBUG) {
//                println("DigitalOceanNetwork isTorConnected: ${torHelper.isTorConnected(connectTorNetwork)}")
//            }
            val url = URL(String.format("%s/%s.test", serverUrl, fileSize))
            val conn: HttpURLConnection =
                (if (connectTorNetwork) url.openConnection(proxy) else url.openConnection()) as HttpURLConnection
            conn.connect()
            val stream = conn.inputStream
            val data = ByteArray(8192)
            var read: Int
            var progress = 0
            val contentLength = conn.getHeaderField("Content-Length")?.toLong() ?: 1L
            val startTime = System.currentTimeMillis()
            try {
                while (stream.read(data).also { read = it } != -1) {
                    progress += read
                    val progressPercentage =
                        ((progress.toFloat() / contentLength.toFloat()) * 100).toInt()
                    emit(
                        NetworkState.inProgress(
                            progress = progressPercentage,
                        )
                    )
                }
            } catch (e: Exception) {
                // do nothing
            }

            emit(
                NetworkState.done(
                    timeFinishedInMillis = System.currentTimeMillis() - startTime,
                    fileSize = fileSize
                )
            )
        }.catch {
            emit(NetworkState.error(errMessage = it.message))
        }.flowOn(Dispatchers.IO)
    }

    fun setDigitalOceanServerUrl(server: String) {
        serverUrl = String.format(AppConstants.DigitalOcean.HOST, server)
    }

    fun connectToTorNetwork(connect: Boolean) {
        connectTorNetwork = connect
    }
}