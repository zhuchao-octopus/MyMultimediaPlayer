package com.octopus.android.multimedia.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.asMavericksArgs
import com.airbnb.mvrx.withState
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentVideoListBinding
import com.octopus.android.multimedia.utils.showEmpty
import com.octopus.android.multimedia.utils.showError
import com.octopus.android.multimedia.utils.showLoading
import com.octopus.android.multimedia.utils.showSuccess
import com.octopus.android.multimedia.utils.viewBinding
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.video.OMedia


//视频列表页面
abstract class VideoListFragment : BaseFragment(R.layout.fragment_video_list) {

    private val binding: FragmentVideoListBinding by viewBinding()

    abstract val viewModel: VideoListViewModel

    private val adapter = VideoListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //设置点击事件
        adapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<OMedia, *>, view: View, i: Int ->
            val item = adapter.getItem(i)
            Cabinet.getPlayManager().localUSBMediaVideos.saveToFile("localUSBMediaVideos")
            //跳转到播放页面
            findNavController().navigate(
                R.id.action_videoSortFragment_to_videoPlayFragment,
                item?.asMavericksArgs()
            )
        }

        //配置recycleView
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleView.adapter = adapter

        //默认显示加载中
        binding.multiStateContainer.showLoading()
    }


    override fun invalidate() = withState(viewModel) {
        if (it.list is Success) {
            if (it.list.invoke().isNullOrEmpty()) {
                //显示无数据
                binding.multiStateContainer.showEmpty()
            } else {
                //显示数据
                binding.multiStateContainer.showSuccess()
                adapter.submitList(it.list.invoke())
            }
        } else if (it.list is Fail) {
            //显示错误
            binding.multiStateContainer.showError {
                viewModel.fetchData()
            }
        } else {
            //显示加载中
            binding.multiStateContainer.showLoading()
        }
    }
}

//视频列表ViewModel
abstract class VideoListViewModel(initialState: VideoListState) :
    MavericksViewModel<VideoListState>(
        initialState
    ) {

    init {
        fetchData()
    }

    //加载列表数据
    abstract fun fetchData()
}

//视频列表页面状态
data class VideoListState(val list: Async<List<OMedia>?> = Uninitialized) : MavericksState

//视频列表适配器
class VideoListAdapter : BaseQuickAdapter<OMedia, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: OMedia?) {
        holder.setText(R.id.textView, item?.pathName)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_video_list, parent)
    }

}



