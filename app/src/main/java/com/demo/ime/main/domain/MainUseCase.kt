package com.demo.ime.main.domain

import androidx.lifecycle.LiveData
import com.demo.ime.common.Result
import com.demo.ime.main.data.models.PinBlock

interface MainUseCase {
    suspend fun generatePinBlock(pin: String): LiveData<Result<PinBlock>>
}