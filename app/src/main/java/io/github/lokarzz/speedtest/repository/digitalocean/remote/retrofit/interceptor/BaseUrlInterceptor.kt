package io.github.lokarzz.speedtest.repository.digitalocean.remote.retrofit.interceptor

import io.github.lokarzz.speedtest.constants.AppConstants
import okhttp3.Interceptor
import okhttp3.Response

class BaseUrlInterceptor : Interceptor {

    private var serverUrl = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        when {
            serverUrl.isNotEmpty() -> {
               val digitalOceanUrl = request.url.newBuilder()
                    .host(serverUrl)
                    .build()
                request.newBuilder()
                    .url(digitalOceanUrl)
                    .build().also {
                        request = it
                    }
            }
        }
        return chain.proceed(request)
    }

    fun setDigitalOceanServerUrl(server: String) {
        serverUrl = String.format(AppConstants.DigitalOcean.HOST, server)
    }
}