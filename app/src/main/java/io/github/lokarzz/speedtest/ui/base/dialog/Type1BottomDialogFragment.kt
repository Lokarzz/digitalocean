package io.github.lokarzz.speedtest.ui.base.dialog

import android.view.ViewGroup
import io.github.lokarzz.speedtest.databinding.BottomDialogFragmentType1Binding
import io.github.lokarzz.speedtest.ui.base.dialog.base.BaseBottomSheetDialogFragment

class Type1BottomDialogFragment :
    BaseBottomSheetDialogFragment<BottomDialogFragmentType1Binding>() {

    var title: CharSequence? = null
    var description: CharSequence? = null
    var okText: String? = null
    var cancelText: String? = null
    var onPressOk: (() -> Unit)? = null
    var onPressCancel: (() -> Unit)? = null

    override fun initViewBinding(container: ViewGroup?): BottomDialogFragmentType1Binding {
        return BottomDialogFragmentType1Binding.inflate(layoutInflater, container, false)
    }

    override fun initView() {
        initFields()
        initHandler()
    }

    private fun initHandler() {
        binding.handler = object : Handler {
            override fun onPressOk() {
                onPressOk?.let { it() }
                dismiss()
            }

            override fun onPressCancel() {
                onPressCancel?.let { it() }
                dismiss()
            }

        }
    }

    private fun initFields() {
        binding.title = title
        binding.description = description
        binding.okText = okText
        binding.cancelText = cancelText
    }

    interface Handler {
        fun onPressOk()
        fun onPressCancel()
    }
}