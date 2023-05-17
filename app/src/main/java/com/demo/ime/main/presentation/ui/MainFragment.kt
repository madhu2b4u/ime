package com.demo.ime.main.presentation.ui

import android.os.Bundle
import com.demo.ime.R
import com.demo.ime.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_main
    }

}