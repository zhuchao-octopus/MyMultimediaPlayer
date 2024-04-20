package com.octopus.android.multimedia.fragments.bluetooth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
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

/**
 * 蓝牙拨号页面
 * */
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
            //TODO 拨打电话
        }
        binding.viewHung.setOnClickListenerWithInterval {
            //TODO 挂断电话
        }
        binding.viewVoice.setOnClickListenerWithInterval {
            //TODO 声音
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