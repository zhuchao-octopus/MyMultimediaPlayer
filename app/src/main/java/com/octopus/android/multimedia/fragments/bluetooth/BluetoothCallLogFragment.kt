package com.octopus.android.multimedia.fragments.bluetooth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentBluetoothCallLogBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding

/**
 * 蓝牙通话记录页面
 * */
class BluetoothCallLogFragment : BaseFragment(R.layout.fragment_bluetooth_call_log) {
    private val binding: FragmentBluetoothCallLogBinding by viewBinding()
    private val bluetoothViewModel: BluetoothViewModel by activityViewModel()
    private val viewModel: BluetoothCallLogViewModel by fragmentViewModel()
    private val adapter = CallLogAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleView.adapter = adapter

        adapter.isStateViewEnable = true
        adapter.setStateViewLayout(requireContext(), R.layout.layout_empty)

        binding.ivInCallLog.setOnClickListenerWithInterval {
            viewModel.setIndex(0)
            bluetoothViewModel.fetchInCallLog()
        }
        binding.ivOutCallLog.setOnClickListenerWithInterval {
            viewModel.setIndex(1)
            bluetoothViewModel.fetchOutCallLog()
        }
        binding.ivMissCallLog.setOnClickListenerWithInterval {
            viewModel.setIndex(2)
            bluetoothViewModel.fetchMissCallLog()
        }
    }

    override fun invalidate() = withState(
        viewModel,
        bluetoothViewModel
    ) { bluetoothCallLogState: BluetoothCallLogState, bluetoothState: BluetoothState ->
        when (bluetoothCallLogState.index) {
            0 ->
                adapter.submitList(bluetoothState.inCallLogList)

            1 ->
                adapter.submitList(bluetoothState.outCallLogList)

            2 ->
                adapter.submitList(bluetoothState.missCallLogList)
        }

        binding.ivInCallLog.isSelected = bluetoothCallLogState.index == 0
        binding.ivOutCallLog.isSelected = bluetoothCallLogState.index == 1
        binding.ivMissCallLog.isSelected = bluetoothCallLogState.index == 2
    }
}

data class BluetoothCallLogState(
    val index: Int = 0,//默认选中项,0:呼入,1:呼出:2:未接
) : MavericksState

class BluetoothCallLogViewModel(initialState: BluetoothCallLogState) :
    MavericksViewModel<BluetoothCallLogState>(
        initialState
    ) {
    fun setIndex(value: Int) {
        setState { copy(index = value) }
    }

}

//视频列表适配器
class CallLogAdapter : BaseQuickAdapter<Contacts, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Contacts?) {
        holder.setText(R.id.textView1, item?.name)
        holder.setText(R.id.textView2, item?.number)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_phone_list, parent)
    }

}
