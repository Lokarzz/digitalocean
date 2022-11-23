package io.github.lokarzz.speedtest.repository.digitalocean.remote

import io.github.lokarzz.speedtest.extensions.ApiExtension.fetchResponse
import io.github.lokarzz.speedtest.repository.digitalocean.remote.network.DigitalOceanNetwork
import io.github.lokarzz.speedtest.repository.digitalocean.remote.network.NetworkState
import io.github.lokarzz.speedtest.repository.digitalocean.remote.retrofit.IDigitalOceanService
import io.github.lokarzz.speedtest.repository.digitalocean.remote.retrofit.interceptor.BaseUrlInterceptor
import io.github.lokarzz.speedtest.repository.model.base.LoadingData
import io.github.lokarzz.speedtest.repository.model.base.UIState
import io.github.lokarzz.speedtest.repository.model.digitalocean.download.DownloadData
import io.github.lokarzz.speedtest.repository.model.digitalocean.server.ServerData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val iDigitalOceanService: IDigitalOceanService,
    private val baseUrlInterceptor: BaseUrlInterceptor,
    private val digitalOceanNetwork: DigitalOceanNetwork,
) {


    suspend fun netWorkDownloadFile(fileSize: String): Flow<NetworkState> {
        return digitalOceanNetwork.downloadFile(fileSize)
    }

    suspend fun fetchServers(): Flow<UIState<List<ServerData>>> {
        return flow {
            val nycServers = ServerData(countryISO = "NYC", server = listOf("nyc1", "nyc2", "nyc3"))
            val amsServers = ServerData(countryISO = "AMS", server = listOf("ams2", "ams3"))
            val sfoServers = ServerData(countryISO = "SFO", server = listOf("sfo1", "sfo2", "sfo3"))
            val sgpServers = ServerData(countryISO = "SGP", server = listOf("sgp1"))
            val lonServers = ServerData(countryISO = "LON", server = listOf("lon1"))
            val fraServers = ServerData(countryISO = "FRA", server = listOf("fra1"))
            val torServers = ServerData(countryISO = "TOR", server = listOf("tor1"))
            val blrServers = ServerData(countryISO = "BLR", server = listOf("blr1"))
            emit(
                UIState.success(
                    data = listOf(
                        nycServers,
                        amsServers,
                        sfoServers,
                        sgpServers,
                        lonServers,
                        fraServers,
                        torServers,
                        blrServers
                    )
                )
            )
        }.onStart {
            emit(UIState.loading(LoadingData(true)))
        }.onCompletion {
            emit(UIState.loading(LoadingData(false)))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setServer(server: String) {
        withContext(Dispatchers.Default) {
            baseUrlInterceptor.setDigitalOceanServerUrl(server)
            digitalOceanNetwork.setDigitalOceanServerUrl(server)
        }
    }

    suspend fun setTorMode(torMode: Boolean) {
        withContext(Dispatchers.Default) {
            digitalOceanNetwork.connectToTorNetwork(torMode)
        }
    }
}