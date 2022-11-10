package io.github.lokarzz.speedtest.extensions

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow

object CoroutineExtension {


    /**
     * Delay block
     *
     * @param timeMillis the value for delay in millisecond
     * @param block the function to be called
     * @receiver [LifecycleOwner]
     * @return this function return a Job
     */
    inline fun LifecycleOwner.delayBlock(timeMillis: Long = 0, crossinline block: () -> Unit): Job {
        return lifecycleScope.delayBlock(timeMillis, block)
    }


    /**
     * Delay block
     *
     * @param timeMillis the value for delay in millisecond
     * @param block the function to be called
     * @receiver [ViewModel]
     * @return this function return a Job
     */
    inline fun ViewModel.delayBlock(timeMillis: Long = 0, crossinline block: () -> Unit): Job {
        return viewModelScope.delayBlock(timeMillis, block)
    }


    /**
     * Delay block
     *
     * @param timeMillis the value for delay in millisecond
     * @param block the function to be called
     * @receiver [CoroutineScope]
     * @return this function return a Job
     */
    inline fun CoroutineScope.delayBlock(timeMillis: Long = 0, crossinline block: () -> Unit): Job {
        return launch {
            triggerDelay(timeMillis, block)
        }
    }


    /**
     * Trigger delay
     *
     * @param timeMillis
     * @param block
     */
    suspend inline fun triggerDelay(timeMillis: Long = 0, block: () -> Unit) {
        withContext(Dispatchers.Default) {
            delay(timeMillis)
        }
        block()
    }


    /**
     * Observe ui state
     *
     * @param T the type of a member in this function
     * @param stateFlow the value to be collected from
     * @param block the function to be called
     * @receiver [LifecycleOwner]
     */
    inline fun <T> LifecycleOwner.observeUiState(
        stateFlow: StateFlow<T>,
        crossinline block: (T) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                stateFlow.collect {
                    block(it)
                }
            }
        }
    }


    /**
     * Throttle last
     *
     * @param T the type of a member in this function
     * @property coroutineScope the scope to be used from
     * @constructor Create empty Throttle last
     */
    class ThrottleLast<T>(private val coroutineScope: CoroutineScope) {
        private var job: Job? = null
        var intervalMillis: Long = 500
        private var block: ((T) -> Unit)? = null

        /**
         * Interval
         *
         * @param intervalMillis in millis
         * @return this function return itself
         */
        fun interval(intervalMillis: Long): ThrottleLast<T> {
            this.intervalMillis = intervalMillis
            return this
        }

        /**
         * Subscribe
         *
         * @param block the function to be called from
         * @return this function return itself
         */
        fun subscribe(block: (T) -> Unit): ThrottleLast<T> {
            this.block = block
            return this
        }

        /**
         * Emit value
         *
         * @param value the value to be emitted from block function
         */
        fun emitValue(value: T) {
            job?.cancel(null)
            job = coroutineScope.launch {
                withContext(Dispatchers.Default) {
                    delay(intervalMillis)
                }
                block?.let { it(value) }
            }
        }
    }
}