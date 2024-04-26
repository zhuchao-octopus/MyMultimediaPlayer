package com.octopus.android.multimedia.fragments.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.activityViewModel
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

        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleView.adapter = adapter

        adapter.submitList(listOf(BTDevice("name1", 0), BTDevice("name2", 1)))

        binding.ivSearch.setOnClickListenerWithInterval {
//            startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
//                putExtra(
//                    BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
//                    300
//                )
//            })

            bluetoothViewModel.queryPairList()
            bluetoothViewModel.discover()
        }

        binding.ivDisconnect.setOnClickListenerWithInterval {
            bluetoothViewModel
        }

    }

    override fun invalidate() {
        super.invalidate()


    }

}

//配对列表适配器
class PairAdapter : BaseQuickAdapter<BTDevice, QuickViewHolder>() {

     var selectedItem: BTDevice? = null


    init {
        // item 去除点击抖动的扩展方法
        setOnDebouncedItemClick(time = 200) { adapter, view, position ->
            selectedItem = getItem(position)
            notifyDataSetChanged()
        }
    }


    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: BTDevice?) {
        holder.setText(R.id.textView, item?.deviceName)
        when (item?.pairState) {
            -1 -> {
                holder.setVisible(R.id.imageView, false)
            }

            0 -> {
                holder.setVisible(R.id.imageView, true)
                holder.setImageResource(R.id.imageView, R.mipmap.bt_pair_disconnect)
            }

            1 -> {
                holder.setVisible(R.id.imageView, true)
                holder.setImageResource(R.id.imageView, R.mipmap.bt_pair_connect)
            }
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
