package com.octopus.android.multimedia.fragments.bluetooth

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.telecom.Call
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.car.api.ApiBt
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.util.setOnDebouncedItemClick
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentBluetoothCallLogBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.showDeviceNotConnect
import com.octopus.android.multimedia.utils.showEmpty
import com.octopus.android.multimedia.utils.showLoading
import com.octopus.android.multimedia.utils.showSuccess
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
            viewModel.setSelectedItem(null)
            // bluetoothViewModel.fetchInCallLog()
        }
        binding.ivOutCallLog.setOnClickListenerWithInterval {
            viewModel.setIndex(1)
            viewModel.setSelectedItem(null)
            // bluetoothViewModel.fetchOutCallLog()
        }
        binding.ivMissCallLog.setOnClickListenerWithInterval {
            viewModel.setIndex(2)
            viewModel.setSelectedItem(null)
            // bluetoothViewModel.fetchMissCallLog()
        }
        binding.ivDelete.setOnClickListener {

            adapter.selectedItem?.also {
                //显示确认对话框
                AlertDialog.Builder(requireContext()).apply {
                    setMessage("确认要将\n名称:${it.name},号码:${it.number}\n从电话簿中删除吗?")
                    setPositiveButton("删除") { dialog: DialogInterface, _: Int ->

                        val callLog = it
                        withState(viewModel) {
                            if (it.index == 0) {
                                bluetoothViewModel.deleteOutCallLog(callLog)
                            } else if (it.index == 1) {
                                bluetoothViewModel.deleteInCallLog(callLog)
                            } else if (it.index == 2) {
                                bluetoothViewModel.deleteMissCallLog(callLog)
                            }

                            viewModel.setSelectedItem(null)
                            dialog.dismiss()
                        }


                    }
                    setNegativeButton("取消") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            }
        }


        // item 去除点击抖动的扩展方法
        adapter.setOnDebouncedItemClick(time = 200) { a, view, position ->
            val item = adapter.getItem(position)
            viewModel.setSelectedItem(item)
        }

        //默认显示加载中
        binding.multiStateContainer.showLoading()
    }

    override fun invalidate() = withState(
        viewModel,
        bluetoothViewModel
    ) { bluetoothCallLogState: BluetoothCallLogState, bluetoothState: BluetoothState ->
        when (bluetoothCallLogState.index) {
            0 ->
                adapter.submitList(bluetoothState.outCallLogList)

            1 ->
                adapter.submitList(bluetoothState.inCallLogList)

            2 ->
                adapter.submitList(bluetoothState.missCallLogList)
        }

        binding.ivInCallLog.isSelected = bluetoothCallLogState.index == 0
        binding.ivOutCallLog.isSelected = bluetoothCallLogState.index == 1
        binding.ivMissCallLog.isSelected = bluetoothCallLogState.index == 2

        if (bluetoothState.btState == ApiBt.PHONE_STATE_DISCONNECTED) {
            binding.multiStateContainer.showDeviceNotConnect()
        } else {
            binding.multiStateContainer.showSuccess()
        }

        adapter.selectedItem = bluetoothCallLogState.selectedItem
    }
}

data class BluetoothCallLogState(
    val index: Int = 0,//默认选中项,0:呼入,1:呼出:2:未接
    val selectedItem: CallLog? = null//选中项
) : MavericksState

class BluetoothCallLogViewModel(initialState: BluetoothCallLogState) :
    MavericksViewModel<BluetoothCallLogState>(
        initialState
    ) {
    fun setIndex(value: Int) {
        setState { copy(index = value) }
    }

    fun setSelectedItem(callLog: CallLog?) {
        setState { copy(selectedItem = callLog) }
    }

}

//视频列表适配器
class CallLogAdapter : BaseQuickAdapter<CallLog, QuickViewHolder>() {

    var selectedItem: CallLog? = null

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: CallLog?) {
        holder.setText(R.id.textView1, item?.name)
        holder.setText(R.id.textView2, item?.number)

        if (selectedItem != null && selectedItem == item) {
            holder.getView<View>(R.id.itemBg).setBackgroundResource(R.drawable.common_item_selected)
        } else {
            holder.getView<View>(R.id.itemBg).setBackgroundResource(R.mipmap.video_item_bg)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_phone_list, parent)
    }

}
