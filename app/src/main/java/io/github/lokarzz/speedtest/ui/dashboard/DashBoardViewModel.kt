package io.github.lokarzz.speedtest.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.lokarzz.speedtest.constants.AppConstants
import io.github.lokarzz.speedtest.extensions.DateExtension.fetchCurrentTime
import io.github.lokarzz.speedtest.repository.digitalocean.DigitalOceanRepository
import io.github.lokarzz.speedtest.repository.model.base.UIState
import io.github.lokarzz.speedtest.repository.model.digitalocean.history.DownloadHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val digitalOceanRepository: DigitalOceanRepository,
) : ViewModel() {


    private val _uiState by lazy {
        MutableStateFlow(DashBoardUiState())
    }

    val uiState by lazy {
        _uiState.asStateFlow()
    }

    init {
        fetchServers()
    }

    private fun fetchServers() {
        viewModelScope.launch {
            digitalOceanRepository.fetchServers().collect { state ->
                when (state.status) {
                    UIState.Status.SUCCESS -> {
                        _uiState.update {
                            it.copy(supportedServer = state.data)
                        }
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }
    }

    private fun saveToHistory() {
        val downloadData = uiState.value.downloadData ?: return
        val currentBaseUrl = uiState.value.currentBaseUrl ?: return
        viewModelScope.launch {
            digitalOceanRepository.saveToHistory(
                DownloadHistory(
                    timeFinishedInMillis = downloadData.timeFinishedInMillis,
                    fileSize = downloadData.fileSize,
                    server = currentBaseUrl,
                    date = fetchCurrentTime()
                )
            )
        }
    }

    fun downloadFile() {
        viewModelScope.launch {
            val fileSize = uiState.value.selectedFileSize ?: AppConstants.FileSize.SIZE_10_MB
            digitalOceanRepository.downloadFile(fileSize = fileSize).collect { state ->
                when (state.status) {
                    UIState.Status.SUCCESS -> {
                        _uiState.update {
                            it.copy(downloadData = state.data)
                        }
                        saveToHistory()
                    }
                    UIState.Status.ERROR -> {
                        _uiState.update {
                            it.copy(apiError = state.error)
                        }
                    }
                    UIState.Status.LOADING -> {
                        _uiState.update {
                            it.copy(downloadLoading = state.loadingData?.isLoading == true)
                        }
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }
    }

    fun setServerUrl(server: String) {
        viewModelScope.launch {
            digitalOceanRepository.setServer(server = server)
            _uiState.update {
                it.copy(
                    currentBaseUrl = String.format(
                        AppConstants.DigitalOcean.HOST,
                        server
                    )
                )
            }
        }
    }

    fun setSelectedFileSize(fileSize: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedFileSize = fileSize) }
        }
    }


    fun fetchDownloadHistory() {
        viewModelScope.launch {
            digitalOceanRepository.fetchAllHistory().collect { state ->
                _uiState.update { it.copy(downloadHistory = state) }
            }
        }
    }
}