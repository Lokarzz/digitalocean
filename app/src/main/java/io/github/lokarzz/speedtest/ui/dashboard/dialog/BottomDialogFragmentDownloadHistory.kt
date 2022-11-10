package io.github.lokarzz.speedtest.ui.dashboard.dialog

import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.lokarzz.speedtest.R
import io.github.lokarzz.speedtest.databinding.BottomDialogFragmentDownloadHistoryBinding
import io.github.lokarzz.speedtest.extensions.CoroutineExtension.observeUiState
import io.github.lokarzz.speedtest.ui.base.dialog.base.BaseBottomSheetDialogFragment
import io.github.lokarzz.speedtest.ui.dashboard.DashBoardUiState
import io.github.lokarzz.speedtest.ui.dashboard.DashBoardViewModel
import io.github.lokarzz.speedtest.ui.dashboard.dialog.adapter.RvDownloadHistoryAdapter

@AndroidEntryPoint
class BottomDialogFragmentDownloadHistory :
    BaseBottomSheetDialogFragment<BottomDialogFragmentDownloadHistoryBinding>() {
    private var uiState: DashBoardUiState? = null
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private val adapter by lazy {
        RvDownloadHistoryAdapter()
    }

    override fun initViewBinding(container: ViewGroup?): BottomDialogFragmentDownloadHistoryBinding {
        return BottomDialogFragmentDownloadHistoryBinding.inflate(layoutInflater, container, false)
    }

    override fun initView() {
        viewModel.fetchDownloadHistory()
        observeState()
        initRv()

    }

    private fun initRv() {
        binding.rv.adapter = adapter
    }

    private fun observeState() {
        observeUiState(viewModel.uiState) {
            uiState = it
            initUiState()
        }
    }

    private fun initUiState() {
        initAdapterList()
        initDescription()
    }

    private fun initDescription() {
        val emptyHistory = uiState?.downloadHistory.isNullOrEmpty()
        binding.description = if (emptyHistory) getString(R.string.no_download_history) else null
    }

    private fun initAdapterList() {
        val downloadHistory = uiState?.downloadHistory ?: return
        adapter.submitList(downloadHistory)
    }


}