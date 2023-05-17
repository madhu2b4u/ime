package com.demo.ime.common

open class Event<out T>(private val content: T) {
    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
