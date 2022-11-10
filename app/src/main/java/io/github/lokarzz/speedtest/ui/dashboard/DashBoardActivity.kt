package io.github.lokarzz.speedtest.ui.dashboard

import android.widget.ArrayAdapter
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.lokarzz.speedtest.R
import io.github.lokarzz.speedtest.constants.AppConstants
import io.github.lokarzz.speedtest.databinding.ActivityDashboardBinding
import io.github.lokarzz.speedtest.extensions.CoroutineExtension.observeUiState
import io.github.lokarzz.speedtest.ui.base.BaseActivity
import io.github.lokarzz.speedtest.ui.base.dialog.Type1BottomDialogFragment
import io.github.lokarzz.speedtest.ui.dashboard.dialog.BottomDialogFragmentDownloadHistory

@AndroidEntryPoint
class DashBoardActivity : BaseActivity<ActivityDashboardBinding>() {

    val viewModel by viewModels<DashBoardViewModel>()
    private var uiState: DashBoardUiState? = null

    override fun initViewBinding(): ActivityDashboardBinding {
        return ActivityDashboardBinding.inflate(layoutInflater)
    }

    override fun initView() {
        observeState()
        initHandler()
        initFileSizeDownload()
    }

    private fun initFileSizeDownload() {

        binding.rgFileSize.setOnCheckedChangeListener { _, checkedId ->
            val fileSize = when (checkedId) {
                binding.rb10Mb.id -> {
                    AppConstants.FileSize.SIZE_10_MB
                }
                binding.rb100Mb.id -> {
                    AppConstants.FileSize.SIZE_100_MB
                }
                binding.rb1Gb.id -> {
                    AppConstants.FileSize.SIZE_1_GB
                }
                binding.rb5Gb.id -> {
                    AppConstants.FileSize.SIZE_5_GB
                }
                else -> {
                    AppConstants.FileSize.SIZE_10_MB

                }
            }
            viewModel.setSelectedFileSize(fileSize)
        }
    }

    private fun observeState() {
        observeUiState(viewModel.uiState) {
            uiState = it
            initUiState()
        }
    }

    private fun initUiState() {
        initCountry()
        initDownloadData()
        initBaseUrl()
        initDownloadLoading()
        initErrorMessage()
    }

    private fun initErrorMessage() {
        uiState?.errorMessage ?: return
        Type1BottomDialogFragment().also {
            it.title = getString(R.string.oops)
            it.description = getString(R.string.file_size_not_supported)
            it.onDismiss = { viewModel.clearError() }
            it.show(supportFragmentManager, "")
        }

    }

    private fun initDownloadLoading() {
        val loading = uiState?.downloadLoading ?: false
        binding.isLoading = loading
    }

    private fun initBaseUrl() {
        val currentBaseUrl = uiState?.currentBaseUrl ?: return
        binding.baseUrl = currentBaseUrl
    }

    private fun initDownloadData() {
        val downloadData = uiState?.downloadData ?: return
        val downloadedFile =
            String.format("%s: %s", getString(R.string.download_file), downloadData.fileSize)
        val timeFinished =
            String.format(
                "%s: %s%s",
                getString(R.string.time),
                downloadData.timeFinishedInMillis,
                getString(R.string.ms),
            )
        binding.downloadResult = String.format("%s\n%s", downloadedFile, timeFinished)
    }

    private fun initCountry() {
        val countryList = uiState?.supportedServer?.map { it.countryISO } ?: return
        with(binding.actvCountry) {
            if (text.isEmpty()) {
                setText(countryList[0])
                initCountryServer(0)
            }
            setAdapter(
                ArrayAdapter(
                    this@DashBoardActivity, android.R.layout.simple_list_item_1, countryList
                )
            )
            setOnItemClickListener { _, _, position, _ ->
                initCountryServer(position)
            }
        }

    }

    private fun initCountryServer(position: Int) {
        val country = uiState?.supportedServer?.get(position) ?: return
        val servers = country.server?.map { it } ?: return
        with(binding.actvCountryServers) {
            setText(servers[0])
            viewModel.setServerUrl(server = servers[0])
            setAdapter(
                ArrayAdapter(
                    this@DashBoardActivity, android.R.layout.simple_list_item_1, servers
                )
            )
            setOnItemClickListener { _, _, pos, _ ->
                viewModel.setServerUrl(server = servers[pos])
            }
        }
    }


    private fun showHistoryDialog() {
        BottomDialogFragmentDownloadHistory().show(supportFragmentManager, "")
    }

    private fun initHandler() {
        binding.handler = object : Handler {
            override fun onClickDownload() {
                viewModel.downloadFile()
            }

            override fun onClickHistory() {
                showHistoryDialog()
            }
        }
    }


    interface Handler {
        fun onClickDownload()
        fun onClickHistory()
    }
}