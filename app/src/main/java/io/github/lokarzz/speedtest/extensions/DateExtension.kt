package io.github.lokarzz.speedtest.extensions

import io.github.lokarzz.speedtest.constants.AppConstants
import java.text.SimpleDateFormat
import java.util.*

object DateExtension {

    /**
     * Fetch current time
     *
     * @sample 2012-10-01T09:45:00.000+02:00
     * @return this function return formatted time
     */
    fun fetchCurrentTime(): String {
        val targetSdf = SimpleDateFormat(AppConstants.Date.DATE_ISO, Locale.ENGLISH)
        return targetSdf.format(Date())
    }

    /**
     * To date format
     *
     * @param format the desired date format
     * @return this function converts a date format to targeted date format
     */
    fun String.toDateFormat(format: String): String {
        val targetSdf = SimpleDateFormat(format, Locale.ENGLISH)
        return targetSdf.format(Date(this.toTimeMillis()))
    }


    /**
     * To time millis
     *
     * @return this function returns date in milliseconds
     */
    private fun String.toTimeMillis(): Long {
        val sdf = SimpleDateFormat(AppConstants.Date.DATE_ISO, Locale.ENGLISH)
        return sdf.parse(this)?.time ?: 0
    }

}