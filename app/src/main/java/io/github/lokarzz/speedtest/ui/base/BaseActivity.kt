package io.github.lokarzz.speedtest.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    abstract fun initViewBinding(): T
    abstract fun initView()
    private var _binding: T? = null
    val binding by lazy {
        _binding!!
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(initViewBinding()) {
            _binding = this
            setContentView(root)
        }
        initView()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}