package com.demo.ime.main.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.demo.ime.R
import com.demo.ime.common.BaseFragment
import com.demo.ime.common.extensions.afterTextChanged
import com.demo.ime.common.extensions.noCrash
import com.demo.ime.main.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.pinBlockTextView
import kotlinx.android.synthetic.main.fragment_main.pinEditText
import kotlinx.android.synthetic.main.fragment_main.textInputLayout

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun layoutId(): Int {
        return R.layout.fragment_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            noCrash {
                setViews()
                observeOnViewModel()
            }
        }
    }

    private fun observeOnViewModel() = with(viewModel) {
        lifecycle.addObserver(this)

        pinBlock.observe(viewLifecycleOwner) {
            //do nothing
        }

        pinValue.observe(viewLifecycleOwner) {
            pinBlockTextView.text = it
        }
    }

    private fun setViews() {
        pinEditText.afterTextChanged { pin ->
            if (pin.length in 4..12) {
                viewModel.generatePinBlock(pin)
                textInputLayout.error = ""
            } else {
                pinBlockTextView.text = ""
                textInputLayout.error = getString(R.string.invalid_pin_value)
            }
        }
    }

}