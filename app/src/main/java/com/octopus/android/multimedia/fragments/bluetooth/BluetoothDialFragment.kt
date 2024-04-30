package com.octopus.android.multimedia.fragments.bluetooth

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.car.api.ApiBt
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentBluetoothDialBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.showDeviceNotConnect
import com.octopus.android.multimedia.utils.showLoading
import com.octopus.android.multimedia.utils.showSuccess
import com.octopus.android.multimedia.utils.viewBinding

/**
 * 蓝牙拨号页面
 * */
class BluetoothDialFragment : BaseFragment(R.layout.fragment_bluetooth_dial) {

    private val binding: FragmentBluetoothDialBinding by viewBinding()
    private val viewModel: BluetoothDialViewModel by fragmentViewModel()
    private val bluetoothViewModel: BluetoothViewModel by activityViewModel()

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
                        textView.setOnClickListener { view ->
                            if (view is TextView) {
                                viewModel.addNumberText(view.text.toString())
                            }
                        }
                    }
                }
            }
        }

        binding.viewDelete.setOnClickListener {
            viewModel.deleteLastText()
        }
        binding.viewDelete.setOnLongClickListener {
            viewModel.deleteAllText()
            true
        }

        binding.viewCall.setOnClickListenerWithInterval {
            if (binding.tvPhoneNumber.text.isNullOrEmpty()) {
                return@setOnClickListenerWithInterval
            }
            bluetoothViewModel.callNumber(binding.tvPhoneNumber.text.toString())
        }
        binding.viewHung.setOnClickListenerWithInterval {
            bluetoothViewModel.hang()
        }
        binding.viewVoice.setOnClickListenerWithInterval {
            //TODO 声音
        }

        binding.tvPhoneNumber.movementMethod = ScrollingMovementMethod.getInstance()



        binding.multiStateContainer.showLoading()

    }



    override fun invalidate()= withState(viewModel,bluetoothViewModel){ bluetoothDialState: BluetoothDialState, bluetoothState: BluetoothState ->
        binding.tvPhoneNumber.text = bluetoothDialState.phoneNumber

        if (bluetoothState.btState == ApiBt.PHONE_STATE_DISCONNECTED) {
            binding.multiStateContainer.showDeviceNotConnect()
        } else {
            binding.multiStateContainer.showSuccess()
        }
    }

}

data class BluetoothDialState(val phoneNumber: String? = null) : MavericksState

class BluetoothDialViewModel(initialState: BluetoothDialState) :
    MavericksViewModel<BluetoothDialState>(
        initialState
    ) {

    /**
     * 添加字符到末尾
     * */
    fun addNumberText(text: String) = setState {
        copy(phoneNumber = (phoneNumber ?: "") + text)
    }

    /**
     * 删除末尾字符
     * */
    fun deleteLastText() = setState {
        if (phoneNumber.isNullOrEmpty()) {
            copy()
        } else {
            copy(phoneNumber = phoneNumber.substring(0, phoneNumber.length - 1))
        }
    }

    /**
     * 删除所有字符
     * */
    fun deleteAllText() = setState {
        copy(phoneNumber = "")
    }


}