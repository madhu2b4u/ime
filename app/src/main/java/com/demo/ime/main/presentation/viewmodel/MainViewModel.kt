package com.demo.ime.main.presentation.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.demo.ime.common.BaseViewModel
import com.demo.ime.common.Result
import com.demo.ime.common.SingleLiveEvent
import com.demo.ime.common.Status
import com.demo.ime.main.data.models.PinBlock
import com.demo.ime.main.domain.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mUseCase: MainUseCase
) : BaseViewModel(), LifecycleObserver {
    val pinBlock: LiveData<Result<PinBlock>> get() = _pinBlock
    private val _pinBlock = MediatorLiveData<Result<PinBlock>>()

    val pinValue: LiveData<String> get() = _pinValue
    private val _pinValue = SingleLiveEvent<String>()

    fun generatePinBlock(pin: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _pinBlock.addSource(mUseCase.generatePinBlock(pin)) {
                    handleResult(it)
                }
            }
        }
    }

    private fun handleResult(it: Result<PinBlock>) {
        when (it.status) {
            Status.LOADING -> {
                //empty
            }

            Status.ERROR -> {
                //empty
            }

            Status.SUCCESS -> it.data?.let { pinBlock ->
                _pinValue.value = pinBlock.value
            }
        }
    }
}