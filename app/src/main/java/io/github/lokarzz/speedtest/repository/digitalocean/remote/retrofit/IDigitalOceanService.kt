package io.github.lokarzz.speedtest.repository.digitalocean.remote.retrofit

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Streaming

interface IDigitalOceanService {

    @Streaming
    @GET("/{fileSize}.test")
    suspend fun downloadFile(@Path("fileSize") fileSize: String): Response<ResponseBody>

}