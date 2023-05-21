package com.demo.ime.common.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun noCrash(enableLog: Boolean = true, func: () -> Unit): String? {
    return try {
        func()
        null
    } catch (e: Exception) {
        if (enableLog) {
            // Print a formatted error message instead of stack trace
            println("An error occurred: ${e.javaClass.simpleName} - ${e.message}")
        }
        e.message
    }
}

fun Byte.setHiNibbleValue(): Byte = (0xF0 and (this.toInt() shl 4)).toByte()
fun Byte.setLowNibbleValue(): Byte = (0x0F and this.toInt()).toByte()

fun Byte.getHiNibbleValue(): Byte = ((0xF0 and this.toInt()) ushr 4).toByte()
fun Byte.getLowNibbleValue(): Byte = (0x0F and this.toInt()).toByte()


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}