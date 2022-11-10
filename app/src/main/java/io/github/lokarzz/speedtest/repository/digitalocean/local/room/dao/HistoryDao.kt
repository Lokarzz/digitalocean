package io.github.lokarzz.speedtest.repository.digitalocean.local.room.dao

import androidx.room.*
import io.github.lokarzz.speedtest.repository.model.digitalocean.history.DownloadHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun fetchAllHistory(): Flow<List<DownloadHistory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg movieData: DownloadHistory)

    @Delete
    fun deleteHistory(movieData: DownloadHistory)

    @Update
    fun updateHistory(movieData: DownloadHistory)

}