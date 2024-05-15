package com.octopus.android.multimedia.fragments.music

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
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.asMavericksArgs
import com.airbnb.mvrx.withState
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentMusicListBinding
import com.octopus.android.multimedia.databinding.FragmentVideoListBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.showEmpty
import com.octopus.android.multimedia.utils.showError
import com.octopus.android.multimedia.utils.showLoading
import com.octopus.android.multimedia.utils.showSuccess
import com.octopus.android.multimedia.utils.viewBinding
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.session.TPlayManager
import com.zhuchao.android.video.OMedia


//音乐列表页面
abstract class MusicListFragment : BaseFragment(R.layout.fragment_music_list) {

    private val binding: FragmentMusicListBinding by viewBinding()

    private val musicPlayViewModel: MusicPlayViewModel by activityViewModel()

    abstract val viewModel: VideoListViewModel

    private val adapter = MusicListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //设置点击事件
        adapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<OMedia, *>, view: View, i: Int ->
            val item = adapter.getItem(i)

            //跳转到播放页面
//            findNavController().navigate(
//                R.id.action_musicSortFragment_to_musicPlayFragment,
//                item?.pathName?.asMavericksArgs()
//            )


            if (item?.pathName != null) {
                viewModel.updatePlayList()
                TPlayManager.getInstance(context).startPlay(item.pathName)
            }

        }

        musicPlayViewModel.onEach(MusicPlayState::path) {
            adapter.currentPlayingPath = it
            adapter.notifyDataSetChanged()
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
abstract class VideoListViewModel(initialState: MusicListState) :
    MavericksViewModel<MusicListState>(
        initialState
    ) {

    init {
        fetchData()
    }

    //加载列表数据
    abstract fun fetchData()

    //更新播放列表
    abstract fun updatePlayList()
}

//视频列表页面状态
data class MusicListState(val list: Async<List<OMedia>?> = Uninitialized) : MavericksState

//视频列表适配器
class MusicListAdapter : BaseQuickAdapter<OMedia, QuickViewHolder>() {

    var currentPlayingPath: String? = null
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: OMedia?) {
        holder.setText(R.id.textView, item?.name)

        if ((!currentPlayingPath.isNullOrEmpty()) && currentPlayingPath == item?.pathName) {
            //显示播放中
            holder.setVisible(R.id.ivPlaying, true)
        } else {
            holder.setVisible(R.id.ivPlaying, false)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_music_list, parent)
    }

}



