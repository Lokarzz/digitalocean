package io.github.lokarzz.speedtest.extensions


object ListExtension {

    /**
     * Add or replace
     *
     * @param T
     * @param value
     * @param selector
     * @receiver
     * @return
     */
    inline fun <T> MutableList<T>.addOrReplace(
        value: T,
        selector: (T) -> Boolean
    ): List<T> {
        when (val index = indexOfFirst { selector(it) }) {
            -1 -> {
                this.add(value)
            }
            else -> {
                this[index] = value
            }
        }
        return this
    }

    inline fun <T> List<T>.ifNotEmpty(block: (List<T>) -> Unit) {
        if (isNotEmpty()) block(this)
    }
}