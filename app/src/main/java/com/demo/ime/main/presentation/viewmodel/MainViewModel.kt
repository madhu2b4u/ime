package com.demo.ime.main.presentation.viewmodel

import androidx.lifecycle.LifecycleObserver
import com.demo.ime.common.BaseViewModel
import com.demo.ime.main.domain.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mUseCase: MainUseCase
) : BaseViewModel(), LifecycleObserver {

}