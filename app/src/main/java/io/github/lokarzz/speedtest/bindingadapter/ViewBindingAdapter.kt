package io.github.lokarzz.speedtest.bindingadapter

import android.view.View
import android.webkit.WebView
import androidx.databinding.BindingAdapter

object ViewBindingAdapter {

    /**
     * Show
     *
     * @param show the visibility to apply on the view
     */
    @BindingAdapter("show")
    @JvmStatic
    fun View.show(show: Boolean) {
        this.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Show
     *
     * @param show the visibility to apply on the view
     */
    @BindingAdapter("enable")
    @JvmStatic
    fun View.enable(enable: Boolean) {
        this.isEnabled = enable
    }


}