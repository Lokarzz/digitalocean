package io.github.lokarzz.speedtest.extensions

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment

object ViewExtensions {

    /**
     * Go to screen
     *
     * this function will call an activity and removing all activity stack
     *
     * @param cls The component class that is to be used for the intent
     * @param bundle the bundle to be used for the intent
     * @receiver [AppCompatActivity]
     */
    fun AppCompatActivity.goToScreen(cls: Class<out AppCompatActivity>, bundle: Bundle? = null) {
        startActivity(Intent(this, cls).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            bundle?.let {
                putExtras(bundle)
            }
        })
    }


    /**
     * Pop up screen
     *
     * this function will call an activity
     *
     * @param cls The component class that is to be used for the intent
     * @param bundle the bundle to be used for the intent
     * @receiver [AppCompatActivity]
     */
    fun AppCompatActivity.popUpScreen(cls: Class<out AppCompatActivity>, bundle: Bundle? = null) {
        startActivity(Intent(this, cls).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            bundle?.let {
                putExtras(bundle)
            }
        })
    }


    /**
     * Pop up screen
     *
     * this function will call an activity
     *
     * @param cls The component class that is to be used for the intent
     * @param bundle the bundle to be used for the intent
     * @receiver [Fragment]
     */
    fun Fragment.popUpScreen(cls: Class<out AppCompatActivity>, bundle: Bundle? = null) {
        startActivity(Intent(activity, cls).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            bundle?.let {
                putExtras(bundle)
            }
        })
    }


    /**
     * Fetch parcelable
     *
     * @param clazz The data class expected
     * @return this function fetch data from intent parcelable extra
     */
    fun <T> AppCompatActivity.fetchParcelable(clazz: Class<out T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(clazz.simpleName, clazz)
        } else {
            intent.getParcelableExtra(clazz.simpleName)
        }
    }


    /**
     * Show keyboard
     *
     * @param show the visibility for keyboard
     */
    fun AppCompatEditText.showKeyboard(show: Boolean = true) {
        val imm =
            getSystemService(context, InputMethodManager::class.java) as InputMethodManager
        when {
            requestFocus() && show -> {
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

            }
            !show -> {
                imm.hideSoftInputFromWindow(windowToken, 0)
            }
            else -> {
                // do nothing
            }
        }
    }

    fun checkMainThread(msg: String? = "") {
        val isMainThread = Looper.myLooper() == Looper.getMainLooper()
        println("checkMainThread $msg, isMainThread: $isMainThread")
    }

}