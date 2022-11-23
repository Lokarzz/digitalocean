package io.github.lokarzz.speedtest.ui.dashboard.dialog.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.lokarzz.speedtest.R
import io.github.lokarzz.speedtest.constants.AppConstants
import io.github.lokarzz.speedtest.databinding.ItemDownloadHistoryBinding
import io.github.lokarzz.speedtest.extensions.DateExtension.toDateFormat
import io.github.lokarzz.speedtest.repository.model.base.ApiError
import io.github.lokarzz.speedtest.repository.model.digitalocean.history.DownloadHistory
import java.util.concurrent.TimeUnit

class RvDownloadHistoryAdapter :
    RecyclerView.Adapter<RvDownloadHistoryAdapter.ViewHolder>() {

    val data = initAsyncListDiffer()

    private fun RecyclerView.Adapter<out RecyclerView.ViewHolder>.initAsyncListDiffer(): AsyncListDiffer<DownloadHistory> {
        return AsyncListDiffer(this, object : DiffUtil.ItemCallback<DownloadHistory>() {
            override fun areItemsTheSame(
                oldItem: DownloadHistory,
                newItem: DownloadHistory
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DownloadHistory,
                newItem: DownloadHistory
            ): Boolean {
                return oldItem == newItem
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDownloadHistoryBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView(data = data.currentList[position])
    }

    override fun getItemCount(): Int {
        return data.currentList.size
    }

    fun submitList(list: List<DownloadHistory>) {
        data.submitList(list)
    }


    class ViewHolder(
        private val binding: ItemDownloadHistoryBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        var data: DownloadHistory? = null
        fun initView(data: DownloadHistory) {
            this.data = data
            initServer()
            initDate()
            initSpeed()
            initTimeDownloaded()
            initFileSize()
        }

        private fun initFileSize() {
            val fileSize = data?.fileSize ?: return
            binding.fileSize = fileSize
        }

        private fun initTimeDownloaded() {
            val timeFinishedInMillis = data?.timeFinishedInMillis ?: 0L
            binding.timeDownloaded =
                String.format("%s%s", timeFinishedInMillis, context.getString(R.string.ms))
        }


        private fun initServer() {
            val server = data?.server ?: return
            val torMode = data?.torMode ?: return
            val isSuccess = data?.isSuccess ?: return
            val list = arrayListOf<String>().also {
                it.add(server)
                if (torMode) {
                    it.add(context.getString(R.string.another_network))
                }
                if (!isSuccess) {
                    val message = when (data?.errorType) {
                        ApiError.Status.NO_NETWORK.name -> {
                            context.getString(R.string.no_internet_connection)
                        }
                        else -> {
                            context.getString(R.string.unkown_issue)
                        }
                    }
                    it.add(message)
                }
            }
            binding.server = TextUtils.join(" • ", list)
        }


        private fun initDate() {
            val date = data?.date ?: return
            binding.date = date.toDateFormat(AppConstants.Date.MM_DD_YY_H_MM)
        }

        private fun initSpeed() {
            val fileSize = data?.fileSize?.toSizeBit() ?: return
            val timeInMillis = data?.timeFinishedInMillis ?: return
            val speedInMillis = fileSize / timeInMillis.toFloat()
            val speedInSec = (speedInMillis * TimeUnit.SECONDS.toMillis(1))
            binding.speed = String.format("%.2f%s", speedInSec, context.getString(R.string.mbps))
        }

        private fun String.toSizeBit(): Int {
            val sizeInMB = when (this) {
                AppConstants.FileSize.SIZE_10_MB -> {
                    10
                }
                AppConstants.FileSize.SIZE_100_MB -> {
                    100
                }
                AppConstants.FileSize.SIZE_1_GB -> {
                    1000
                }
                AppConstants.FileSize.SIZE_5_GB -> {
                    5000
                }
                else -> {
                    10
                }
            }
            return sizeInMB * 8
        }
    }
}