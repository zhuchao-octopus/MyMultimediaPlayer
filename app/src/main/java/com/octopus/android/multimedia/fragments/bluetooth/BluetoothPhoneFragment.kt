package com.octopus.android.multimedia.fragments.bluetooth

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import com.octopus.android.multimedia.databinding.FragmentBluetoothPhoneBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.showDeviceNotConnect
import com.octopus.android.multimedia.utils.showEmpty
import com.octopus.android.multimedia.utils.showLoading
import com.octopus.android.multimedia.utils.showSuccess
import com.octopus.android.multimedia.utils.viewBinding


class BluetoothPhoneFragment : BaseFragment(R.layout.fragment_bluetooth_phone) {

    private val binding: FragmentBluetoothPhoneBinding by viewBinding()

    private val bluetoothViewModel: BluetoothViewModel by activityViewModel()

    private val viewModel: BluetoothPhoneViewModel by fragmentViewModel()

    private val adapter: PhoneAdapter = PhoneAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setStateViewLayout(requireContext(), R.layout.layout_empty)
        adapter.isStateViewEnable = true
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleView.adapter = adapter

        binding.ivDownload.setOnClickListenerWithInterval {
            hideSoftInput(it)
            bluetoothViewModel.fetchContacts()
            viewModel.setSelectedItem(null)

        }
        binding.ivDelete.setOnClickListenerWithInterval {
            hideSoftInput(it)

            adapter.selectedItem?.also {
                //显示确认对话框
                AlertDialog.Builder(requireContext()).apply {
                    setMessage("确认要将\n名称:${it.name},号码:${it.number}\n从电话簿中删除吗?")
                    setPositiveButton("删除") { dialog: DialogInterface, _: Int ->
                        bluetoothViewModel.deleteContacts(it)
                        viewModel.setSelectedItem(null)
                        dialog.dismiss()
                    }
                    setNegativeButton("取消") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            }
        }


        binding.etSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER) {
                hideSoftInput(v)
            }
            false
        }
        // item 去除点击抖动的扩展方法
        adapter.setOnDebouncedItemClick(time = 200) { a, view, position ->
            val item = adapter.getItem(position)
            viewModel.setSelectedItem(item)
        }




        binding.root.setOnTouchListener { v, event ->
            hideSoftInput(v)
            false
        }
        binding.recycleView.setOnTouchListener { v, event ->
            hideSoftInput(v)
            false
        }


        binding.etSearch.addTextChangedListener(watcher)

        binding.multiStateContainer.showLoading()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.etSearch.removeTextChangedListener(watcher)
    }


    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            bluetoothViewModel.setSearchKey(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }

    override fun invalidate() =
        withState(viewModel, bluetoothViewModel) { bluetoothPhoneState, bluetoothState ->
            adapter.selectedItem = bluetoothPhoneState.selectedItem

            if (bluetoothState.searchKey.isNullOrEmpty()) {
                adapter.submitList(bluetoothState.contactsList)
            } else {
                adapter.submitList(bluetoothState.searchContactsList.invoke())
            }


            if (bluetoothState.btState == ApiBt.PHONE_STATE_DISCONNECTED) {
                binding.multiStateContainer.showDeviceNotConnect()
            } else {
                binding.multiStateContainer.showSuccess()
            }

        }

    //隐藏软键盘
    private fun hideSoftInput(v: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }

}

//联系人列表适配器
class PhoneAdapter : BaseQuickAdapter<Contacts, QuickViewHolder>() {

    var selectedItem: Contacts? = null

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Contacts?) {
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

data class BluetoothPhoneState(
    val selectedItem: Contacts? = null//选中项
) : MavericksState

class BluetoothPhoneViewModel(initialState: BluetoothPhoneState) :
    MavericksViewModel<BluetoothPhoneState>(
        initialState
    ) {
    fun setSelectedItem(contacts: Contacts?) = withState {
        if (it.selectedItem == contacts) {
            setState { copy(selectedItem = null) }
        } else {
            setState { copy(selectedItem = contacts) }
        }
    }
}