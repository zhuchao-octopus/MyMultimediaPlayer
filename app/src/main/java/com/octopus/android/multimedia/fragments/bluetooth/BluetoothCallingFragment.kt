package com.octopus.android.multimedia.fragments.bluetooth

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.car.api.ApiBt
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentBluetoothMusicBinding
import com.octopus.android.multimedia.databinding.FragmentCallingBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.showDeviceNotConnect
import com.octopus.android.multimedia.utils.showLoading
import com.octopus.android.multimedia.utils.showSuccess
import com.octopus.android.multimedia.utils.viewBinding

/**
 * 蓝牙通话中页面
 * 该页面由services启动
 * 该页面会通过viewModel监听通话结束状态,自动关闭
 *
 * */
class BluetoothCallingFragment : BaseFragment(R.layout.fragment_calling) {
    private val binding: FragmentCallingBinding by viewBinding()
    private val bluetoothViewModel: BluetoothViewModel by activityViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //监听拨号状态,如果结束通话,则回到上一页

    }


    override fun invalidate() = withState(bluetoothViewModel) {
        //根据当前拨号状态,显示按钮

    }
}