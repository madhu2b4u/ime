package com.demo.ime.main.data.repository

import androidx.lifecycle.liveData
import com.demo.ime.common.Result
import com.demo.ime.common.extensions.getHiNibbleValue
import com.demo.ime.common.extensions.getLowNibbleValue
import com.demo.ime.common.extensions.setHiNibbleValue
import com.demo.ime.common.extensions.setLowNibbleValue
import com.demo.ime.main.data.models.PinBlock
import java.util.Locale
import javax.inject.Inject
import kotlin.experimental.xor

private const val PAN_NUMBER = "43219876543210987"
private const val CODE_LENGTH = 16
private const val BLOCK_LENGTH = 8

class MainRepositoryImpl @Inject constructor() : MainRepository {
    override suspend fun generatePinBlock(pin: String) = liveData {
        emit(Result.loading())
        try {
            val pinBlock = preparePinBlock(pin)
            emit(Result.success(PinBlock(pinBlock)))

        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "", null))
        }
    }

    override suspend fun generatePinFromPanAndPinBlock(pinBlock: String) = liveData {
        emit(Result.loading())
        try {
            val pin = decodePin(pinBlock)
            emit(Result.success(pin))

        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "", null))
        }
    }

    /**
     * preparePinBlock
     * Prepares the PIN block by encoding the PIN using the ISO-3 Format
     * It takes the PIN as input, converts it into a byte array, and performs the necessary encoding steps.
     * The resulting PIN block is returned as a hexadecimal string.
     */
    private fun preparePinBlock(pin: String): String {
        val preppedPIN = preparePIN(pin)
        val preppedPAN = preparePAN()
        val pinBlock = getPinBlock(preppedPIN, preppedPAN)
        return pinBlock.toHexadecimalString()
    }

    /**
     * preparePIN
     * Prepares the PIN by converting it into a byte array according to the ISO-3 Format
     * The PIN is represented as a series of bytes with the following structure:
     *   - Byte 0: ISO-3 Format code
     *   - Byte 1: PIN length.
     *   - Bytes 2 to (PIN length + 1): Digits of the PIN, converted from characters to bytes.
     *   - Bytes (PIN length + 2) to 15: Random padding bytes generated using a secure random number generator.
     */

    private fun preparePIN(pin: String): ByteArray {
        val isoCode = 0x03
        val preparedPIN = ByteArray(CODE_LENGTH)
        preparedPIN[0] = isoCode.toByte()
        preparedPIN[1] = pin.length.toByte()

        pin.forEachIndexed { index, c ->
            preparedPIN[index + 2] = Character.getNumericValue(c).toByte()
        }

        for (i in pin.length + 2 until CODE_LENGTH) {
            preparedPIN[i] = (0..0xF).random().toByte()
        }
        return preparedPIN
    }

    /**
     * preparePAN
     * Prepares the PAN by converting it into a byte array.
     * It takes the last 12 digits of the PAN_NUMBER constant and places them in the prepared PAN byte array starting from index 4.
     */
    private fun preparePAN(): ByteArray {
        val preparedPAN = ByteArray(CODE_LENGTH)
        val shortenPAN = PAN_NUMBER.takeLast(12)

        shortenPAN.forEachIndexed { index, c ->
            preparedPAN[index + 4] = Character.getNumericValue(c).toByte()
        }
        return preparedPAN
    }

    /**
     * getPinBlock
     * Constructs the PIN block by performing XOR operations between the prepared PIN and PAN.
     * It follows the ISO-3 Format for PIN block construction.
     * Returns the resulting PIN block as a byte array.
     */
    private fun getPinBlock(pin: ByteArray, pan: ByteArray): ByteArray {
        val pinBlock = ByteArray(BLOCK_LENGTH)

        pin.forEachIndexed { index, pinItem ->
            val xorResult = pinItem xor pan[index]
            val blockIndex = index / 2

            if (index % 2 == 0) {
                pinBlock[blockIndex] = xorResult.setHiNibbleValue() xor pinBlock[blockIndex]
            } else {
                pinBlock[blockIndex] = xorResult.setLowNibbleValue() xor pinBlock[blockIndex]
            }
        }

        return pinBlock
    }

    /**
     * decodePin
     * Decodes the PIN from the provided PIN block.
     * It reverses the encoding process by performing XOR operations between the PIN block and PAN bytes.
     * The decoded PIN is returned as a string.
     *
     * @param pinBlock The PIN block to decode, represented as a hexadecimal string.
     * @return The decoded PIN if decoding is successful, or null otherwise.
     */
    private fun decodePin(pinBlock: String): String? {
        val pinBlockBytes = pinBlock.hexadecimalToByteArray()
        val panBytes = PAN_NUMBER.takeLast(12).hexadecimalToByteArray()

        val pin = StringBuilder()

        for (i in pinBlockBytes.indices) {
            val blockByte = pinBlockBytes[i]
            val panByte = panBytes[i]

            if (i % 2 == 0) {
                val pinNibble = (blockByte xor panByte).getHiNibbleValue()
                pin.append(pinNibble)
            } else {
                val pinNibble = (blockByte xor panByte).getLowNibbleValue()
                pin.append(pinNibble)
            }
        }

        val pinLength = pin[1].code
        return if (pin.length == pinLength + 2) pin.substring(2) else null
    }
}

/**
 * toHexadecimalString
 * Converts a byte array to a hexadecimal string representation.
 * Each byte is converted to a two-character hexadecimal value.
 * The resulting string contains the concatenated hexadecimal values.
 *
 * @return The hexadecimal string representation of the byte array.
 */
private fun ByteArray.toHexadecimalString(): String {
    val retString = StringBuilder()
    this.forEach {
        retString.append("%02x".format(it))
    }
    return retString.toString().uppercase(Locale.getDefault())
}

/**
 * hexadecimalToByteArray
 * Converts a hexadecimal string to a byte array.
 * Each pair of characters in the string represents a hexadecimal value.
 * The resulting byte array contains the converted values.
 *
 * @return The byte array representing the hexadecimal string.
 */
private fun String.hexadecimalToByteArray(): ByteArray {
    val hexString = if (length % 2 == 0) this else "0$this"
    val byteArray = ByteArray(hexString.length / 2)

    for (i in hexString.indices step 2) {
        val hexByte = hexString.substring(i, i + 2)
        byteArray[i / 2] = hexByte.toInt(16).toByte()
    }
    return byteArray
}