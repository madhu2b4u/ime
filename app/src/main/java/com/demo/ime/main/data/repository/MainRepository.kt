package com.demo.ime.main.data.repository

import androidx.lifecycle.LiveData
import com.demo.ime.common.Result
import com.demo.ime.main.data.models.PinBlock


interface MainRepository {

    /**
    Algorithm to generate an ISO-3 (Format 3) PIN Block:

    1.Prepare a PIN:
    Obtain the length of the PIN (L).
    For each digit in the PIN:
    -Generate a random value (R) between 10 and 15.
    -Append the digit (P) and R to the PIN.

    2.Prepare PAN:
    Take the 12 right most digits of the primary account number (excluding the check digit).

    3.XOR both values:
    For each corresponding pair of digits in the PIN and PAN, perform an XOR operation.
    Store the result in a new string.

    4.Concatenate the PIN Block:
    Append '3' as the first nibble (which identifies the block format).
    Append the XOR result obtained in step 3 to the PIN Block.

    5.Return the PIN Block.

     */
    suspend fun generatePinBlock(pin: String): LiveData<Result<PinBlock>>

    /**
    Algorithm to decode an ISO-3 (Format 3) PIN Block and retrieve the PIN

    1.Convert the PIN Block from a hexadecimal string to a byte array.

    2.Obtain the PAN bytes by taking the 12 rightmost digits of the PAN (Primary Account Number) and converting them to a byte array.

    3.Initialize an empty StringBuilder to store the decoded PIN.

    4.Iterate through each byte in the PIN Block:
        If the index is even (starting from 0), perform the XOR operation between the byte in the PIN Block and the corresponding byte in the PAN. Get the high nibble value of the XOR result.
        If the index is odd, perform the XOR operation between the byte in the PIN Block and the corresponding byte in the PAN. Get the low nibble value of the XOR result.
        Append the obtained nibble value to the StringBuilder.

    5.Determine the length of the PIN based on the second character in the StringBuilder.

    6.Return the PIN substring starting from the third character if its length matches the expected length, otherwise return null.

     */
    suspend fun generatePinFromPanAndPinBlock(pinBlock: String): LiveData<Result<String>>
}


