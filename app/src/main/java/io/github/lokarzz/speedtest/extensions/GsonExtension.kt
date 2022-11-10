package io.github.lokarzz.speedtest.extensions

import com.google.gson.Gson
import java.io.Reader

object GsonExtension {

    /**
     * Fetch data
     *
     * @param T the type of a member in this function
     * @param clazz the data class to be used in Gson
     * @return this function returns a data class from Reader
     */
    fun <T> Reader.fetchData(clazz: Class<T>?): T? {
        val gson = Gson()
        return try {
            gson.fromJson(this, clazz)
        } catch (e: Exception) {
            null
        }
    }

}