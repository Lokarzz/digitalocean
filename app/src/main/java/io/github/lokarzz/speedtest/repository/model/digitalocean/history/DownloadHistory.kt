package io.github.lokarzz.speedtest.repository.model.digitalocean.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class DownloadHistory(
    @PrimaryKey var id: Int? = null,
    var timeFinishedInMillis: Long? = null,
    var fileSize: String? = null,
    var server: String? = null,
    var date: String? = null,
    var torMode: Boolean? = null,
    var isSuccess: Boolean = true,
    var errorType: String? = null
)
