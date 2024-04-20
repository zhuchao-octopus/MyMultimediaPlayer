package com.octopus.android.multimedia.fragments.bluetooth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentBluetoothDialBinding
import com.octopus.android.multimedia.databinding.FragmentBluetoothHomeBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.fragments.VideoSortViewModel
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.toastLong
import com.octopus.android.multimedia.utils.viewBinding

class BluetoothDialFragment : BaseFragment(R.layout.fragment_bluetooth_dial) {

    private val binding: FragmentBluetoothDialBinding by viewBinding()
    private val viewModel: BluetoothDialViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //找到所有拨号按钮,设置点击事件
        for (i in 0..binding.viewDial.childCount) {
            val viewGroup = binding.viewDial.getChildAt(i)
            if (viewGroup is LinearLayout) {
                for (i in 0..viewGroup.childCount) {
                    val textView = viewGroup.getChildAt(i)
                    if (textView is TextView) {
                        textView.isClickable = true
                        textView.setOnClickListenerWithInterval { view ->
                            if (view is TextView) {
                                viewModel.addNumberText(view.text.toString())
                            }
                        }
                    }
                }
            }
        }

    }


    override fun invalidate() = withState(viewModel) {
        binding.tvPhoneNumber.text = it.phoneNumber
    }

}

data class BluetoothDialState(val phoneNumber: String? = null) : MavericksState

class BluetoothDialViewModel(initialState: BluetoothDialState) :
    MavericksViewModel<BluetoothDialState>(
        initialState
    ) {
    fun addNumberText(text: String) = setState {
        copy(phoneNumber = phoneNumber ?: "" + text)
    }
}