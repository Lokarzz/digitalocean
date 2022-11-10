package io.github.lokarzz.speedtest.repository.digitalocean.local

import io.github.lokarzz.speedtest.repository.digitalocean.local.room.database.DigitalOceanDatabase
import io.github.lokarzz.speedtest.repository.model.digitalocean.history.DownloadHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalRepository @Inject constructor(private val db: DigitalOceanDatabase) {

    suspend fun saveToHistory(downloadHistory: DownloadHistory) {
        withContext(Dispatchers.IO) {
            db.historyDao().insertAll(downloadHistory)
        }
    }

    suspend fun fetchAllHistory(): Flow<List<DownloadHistory>> {
       return db.historyDao().fetchAllHistory()
    }

}