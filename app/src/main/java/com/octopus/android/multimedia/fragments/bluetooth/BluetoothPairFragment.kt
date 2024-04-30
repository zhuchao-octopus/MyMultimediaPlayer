package com.octopus.android.multimedia.fragments.bluetooth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.util.setOnDebouncedItemClick
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentBluetoothPairBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding

/**
 * 蓝牙配对页面
 * */
class BluetoothPairFragment : BaseFragment(R.layout.fragment_bluetooth_pair) {
    private val binding: FragmentBluetoothPairBinding by viewBinding()
    private val bluetoothViewModel: BluetoothViewModel by activityViewModel()

    private val adapter = PairAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.isStateViewEnable = true
        adapter.setStateViewLayout(requireContext(), R.layout.layout_empty)

        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleView.adapter = adapter

        binding.ivSearch.setOnClickListenerWithInterval {
            bluetoothViewModel.searchBtDevice()
        }

        binding.ivDisconnect.setOnClickListenerWithInterval {
            //如果当前选中项,是已连接状态,则断开连接(注:目前趣智达设备,只能同时连接一个蓝牙设备,所以这里不需要mac地址参数)
            adapter.selectedItem?.deviceMacAddress?.apply {
                withState(bluetoothViewModel) {
                    if (it.connectedDeviceMac.equals(this)) {
                        bluetoothViewModel.disconnectDevice()
                    }
                }

            }
        }

        binding.ivConnect.setOnClickListenerWithInterval {
            adapter.selectedItem?.deviceMacAddress?.apply { bluetoothViewModel.connectDevice(this) }
        }

        binding.ivDelete.setOnClickListenerWithInterval {
            adapter.selectedItem?.deviceMacAddress?.apply { bluetoothViewModel.deleteDevice(this) }
        }
    }

    override fun invalidate() = withState(bluetoothViewModel) {
        adapter.connectedMacAddress = it.connectedDeviceMac
        if (it.displayDeviceList is Success) {
            adapter.submitList(it.displayDeviceList.invoke())
        }

    }

}

//配对列表适配器
class PairAdapter : BaseQuickAdapter<BTDevice, QuickViewHolder>() {

    var selectedItem: BTDevice? = null

    var connectedMacAddress: String? = null


    init {
        // item 去除点击抖动的扩展方法
        setOnDebouncedItemClick(time = 200) { adapter, view, position ->
            selectedItem = getItem(position)
            notifyDataSetChanged()
        }
    }


    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: BTDevice?) {
        holder.setText(R.id.textView, item?.deviceName)

        if (item?.pairState == true) {
            if (item?.deviceMacAddress.equals(connectedMacAddress)) {
                holder.setVisible(R.id.imageView, true)
                holder.setImageResource(R.id.imageView, R.mipmap.bt_pair_connect)
            } else {
                holder.setVisible(R.id.imageView, true)
                holder.setImageResource(R.id.imageView, R.mipmap.bt_pair_disconnect)
            }
        } else {
            holder.setVisible(R.id.imageView, false)
        }


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
        return QuickViewHolder(R.layout.item_pair_list, parent)
    }

}
