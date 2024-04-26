package com.octopus.android.multimedia.fragments.bluetooth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.asMavericksArgs
import com.airbnb.mvrx.withState
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentBluetoothSettingBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.fragments.InputFragment
import com.octopus.android.multimedia.fragments.InputFragmentParams
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.toastLong
import com.octopus.android.multimedia.utils.viewBinding

class BluetoothSettingFragment : BaseFragment(R.layout.fragment_bluetooth_setting) {

    private val binding: FragmentBluetoothSettingBinding by viewBinding()
    private val bluetoothViewModel: BluetoothViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivEditDeviceName.setOnClickListenerWithInterval {
            findNavController().navigate(
                R.id.action_global_inputFragment,
                InputFragmentParams(
                    TYPE_DEVICE_NAME,
                    binding.tvDeviceName.text.toString()
                ).asMavericksArgs()
            )
        }
        binding.ivEditPinCode.setOnClickListenerWithInterval {
            findNavController().navigate(
                R.id.action_global_inputFragment,
                InputFragmentParams(
                    TYPE_PIN_CODE,
                    binding.tvPinCode.text.toString()
                ).asMavericksArgs()
            )
        }

        //监听返回值
        findNavController().currentBackStackEntry?.apply {
            savedStateHandle?.getLiveData<String>(TYPE_DEVICE_NAME)?.observe(this) {
                bluetoothViewModel.setDeviceName(it)
            }
            savedStateHandle?.getLiveData<String>(TYPE_PIN_CODE)?.observe(this) {
                bluetoothViewModel.setPinCode(it)
            }
        }

    }

    override fun invalidate() = withState(bluetoothViewModel) {
        binding.tvDeviceName.text = it.deviceName
        binding.tvPinCode.text = it.pinCode
    }

    companion object {
        const val TYPE_DEVICE_NAME = "deviceName"
        const val TYPE_PIN_CODE = "pinCode"
    }
}