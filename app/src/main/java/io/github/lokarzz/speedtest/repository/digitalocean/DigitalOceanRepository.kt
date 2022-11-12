package io.github.lokarzz.speedtest.repository.digitalocean

import io.github.lokarzz.speedtest.repository.digitalocean.local.LocalRepository
import io.github.lokarzz.speedtest.repository.digitalocean.remote.RemoteRepository
import io.github.lokarzz.speedtest.repository.digitalocean.remote.network.NetworkState
import io.github.lokarzz.speedtest.repository.model.base.UIState
import io.github.lokarzz.speedtest.repository.model.digitalocean.download.DownloadData
import io.github.lokarzz.speedtest.repository.model.digitalocean.history.DownloadHistory
import io.github.lokarzz.speedtest.repository.model.digitalocean.server.ServerData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DigitalOceanRepository @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {

    suspend fun downloadFile(fileSize: String): Flow<UIState<DownloadData>> {
        return remoteRepository.downloadFile(fileSize = fileSize)
    }

    suspend fun netWorkDownloadFile(fileSize: String): Flow<NetworkState> {
        return remoteRepository.netWorkDownloadFile(fileSize = fileSize)
    }

    suspend fun fetchServers(): Flow<UIState<List<ServerData>>> {
        return remoteRepository.fetchServers()
    }

    suspend fun setServer(server: String) {
        withContext(Dispatchers.Default) {
            remoteRepository.setServer(server = server)
        }
    }

    suspend fun saveToHistory(downloadHistory: DownloadHistory) {
        withContext(Dispatchers.Default) {
            localRepository.saveToHistory(downloadHistory = downloadHistory)
        }
    }

    suspend fun fetchAllHistory(): Flow<List<DownloadHistory>> {
        return localRepository.fetchAllHistory()
    }

    suspend fun setTorMode(torMode: Boolean) {
        remoteRepository.setTorMode(torMode)
    }
}