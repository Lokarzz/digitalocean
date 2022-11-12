package io.github.lokarzz.speedtest.constants

object AppConstants {


    object Date {
        const val DATE_ISO = "yyyy-MM-dd'T'HH:mm:ss"
        const val MM_YYYY = "MMM yyyy"
        const val MM_DD_YYYY = "MMM dd yyyy"
        const val MM_DD_YY_H_MM = "MM/dd/yy\nh:mm a"
    }

    object DigitalOcean {
        const val HOST = "http://speedtest-%s.digitalocean.com"
    }

    object FileSize {
        const val SIZE_10_MB = "10mb"
        const val SIZE_100_MB = "100mb"
        const val SIZE_1_GB = "1gb"
        const val SIZE_5_GB = "5gb"

        object Error {
            const val SIZE_NOT_SUPPORTED = "size_not_supported"
        }
    }

    object Tor {
        const val CHECK_TOR_PROJECT_URL = "https://check.torproject.org/"
        const val CONNECTED_MESSAGE = "Congratulations. This browser is configured to use Tor."
    }


}