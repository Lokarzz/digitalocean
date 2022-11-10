package io.github.lokarzz.speedtest.repository.digitalocean.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.lokarzz.speedtest.repository.digitalocean.local.room.dao.HistoryDao
import io.github.lokarzz.speedtest.repository.model.digitalocean.history.DownloadHistory


@Database(entities = [DownloadHistory::class], version = 1)
abstract class DigitalOceanDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        fun initRoom(context: Context): DigitalOceanDatabase {
            return Room.databaseBuilder(
                context,
                DigitalOceanDatabase::class.java, "digital-ocean-database"
            ).fallbackToDestructiveMigration().build()
        }
    }
}