package com.demo.ime.main.data.repository


import androidx.lifecycle.liveData
import com.demo.ime.common.Result
import com.demo.ime.main.data.models.PinBlock
import javax.inject.Inject
import kotlin.experimental.or

class MainRepositoryImpl @Inject constructor() : MainRepository {
    override suspend fun generatePinBlock(pin: String) = liveData {
        emit(Result.loading())
        try {
            val pan = "1111222233334444"
            val pinBlock = calculatePinBlock(pin, pan)
            emit(Result.success(PinBlock(pinBlock)))

        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "", null))
        }
    }

    private fun calculatePinBlock(pin: String, pan: String): String {
        val paddedPin = padPin(pin)
        val pinBlock = StringBuilder()
        for (i in 0 until 16) {
            val pinDigit = if (i < paddedPin.length) paddedPin[i] else 'F'
            val panDigit = if (i < pan.length) pan[i] else '0'
            val pinByte = Character.digit(pinDigit, 16).toByte()
            val panByte = Character.digit(panDigit, 16).toByte()
            val pinBlockByte = setHiNibbleValue(pinByte) or setLowNibbleValue(panByte)
            pinBlock.append(String.format("%02X", pinBlockByte))
        }
        return pinBlock.toString()
    }

    private fun setHiNibbleValue(value: Byte): Byte = (0xF0 and (value.toInt() shl 4)).toByte()

    private fun setLowNibbleValue(value: Byte): Byte = (0x0F and value.toInt()).toByte()

    private fun padPin(pin: String): String {
        val pinLength = pin.length
        return if (pinLength < 16) {
            pin + "F".repeat(16 - pinLength)
        } else {
            pin.substring(0, 16)
        }
    }
}