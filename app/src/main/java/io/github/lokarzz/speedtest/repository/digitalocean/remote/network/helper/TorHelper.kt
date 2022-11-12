package io.github.lokarzz.speedtest.repository.digitalocean.remote.network.helper

import io.github.lokarzz.speedtest.constants.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import javax.inject.Inject

class TorHelper @Inject constructor() {

    suspend fun isTorConnected(connectTorNetwork: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            val url =
                URL(AppConstants.Tor.CHECK_TOR_PROJECT_URL)
            val proxy = Proxy(Proxy.Type.SOCKS, InetSocketAddress("localhost", 9050))
            val conn: HttpURLConnection =
                (if (connectTorNetwork) url.openConnection(proxy) else url.openConnection()) as HttpURLConnection

            conn.requestMethod = "GET"
            conn.connect()
            val stream = conn.inputStream
            var line: String
            val bufferedReader = BufferedReader(InputStreamReader(stream))
            try {
                while (bufferedReader.readLine().also { line = it } != null) {
                    if (line.contains(AppConstants.Tor.CONNECTED_MESSAGE)) {
                        return@withContext true
                    }
                }
            } catch (e: Exception) {
                // do nothing
            }
            stream.close()
            bufferedReader.close()
            false
        }
    }

}