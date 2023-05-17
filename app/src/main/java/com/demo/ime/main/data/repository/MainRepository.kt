package com.demo.ime.main.data.repository

import androidx.lifecycle.LiveData
import com.demo.ime.common.Result
import com.demo.ime.main.data.models.PinBlock


interface MainRepository {
    suspend fun generatePinBlock(pin: String): LiveData<Result<PinBlock>>
}