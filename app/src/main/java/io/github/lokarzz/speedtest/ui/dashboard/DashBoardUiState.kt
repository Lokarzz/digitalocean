package io.github.lokarzz.speedtest.ui.dashboard

import io.github.lokarzz.speedtest.repository.digitalocean.remote.network.NetworkState
import io.github.lokarzz.speedtest.repository.model.base.ApiError
import io.github.lokarzz.speedtest.repository.model.digitalocean.download.DownloadData
import io.github.lokarzz.speedtest.repository.model.digitalocean.history.DownloadHistory
import io.github.lokarzz.speedtest.repository.model.digitalocean.server.ServerData

data class DashBoardUiState(
    val downloadLoading: Boolean? = null,
    val supportedServer: List<ServerData>? = null,
    val downloadData: DownloadData? = null,
    val apiError: ApiError? = null,
    val currentBaseUrl: String? = null,
    val selectedFileSize: String? = null,
    val downloadHistory: List<DownloadHistory>? = null,
    val errorMessage: String? = null,
    val progress: Int? = null,
    val torMode: Boolean? = false,
)
