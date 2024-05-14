package com.octopus.android.multimedia.fragments.music

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.withState
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentGroupListBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.showEmpty
import com.octopus.android.multimedia.utils.showError
import com.octopus.android.multimedia.utils.showLoading
import com.octopus.android.multimedia.utils.showSuccess
import com.octopus.android.multimedia.utils.viewBinding


/**
 * 支持展示二级数据列表的fragment
 * 1.点击一级列表,展示对应二级列表数据
 * 2.点击二级列表第一项,则回到一级列表
 * 3.点击二级列表非第一项,则播放文件
 * */
abstract class MediaFolderFragment : BaseFragment(R.layout.fragment_group_list) {

    private val binding: FragmentGroupListBinding by viewBinding()

    abstract val viewModel: MediaGroupViewModel

    private val mediaAdapter = MediaAdapter()

    private val folderAdapter = FolderAdapter()

   // private val database by lazy { MediaRoomDatabase.getDatabase(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        lifecycleScope.launch {
//            database.provideUserCollectionDao()
//                .insert(UserCollection(path = "test1", time = System.currentTimeMillis()))
//        }

        //设置点击事件
        mediaAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<MediaFolder, *>, view: View, i: Int ->
            val item = mediaAdapter.getItem(i)

            if (i == 0) {
                viewModel.setKey(null)
                return@setOnItemClickListener
            }

            //跳转到播放页面
//            findNavController().navigate(
//                R.id.action_videoSortFragment_to_videoPlayFragment,
//                item?.path?.asMavericksArgs()
//            )


            if (item?.path != null)
                viewModel.updatePlayListAndPlay(item.path)


//            if (item != null) {
//                lifecycleScope.launch {
//                    database.provideUserCollectionDao()
//                        .insert(UserCollection(path = item.path, time = System.currentTimeMillis()))
//                }
//            }

        }

        //配置recycleView

        binding.rvMedia.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMedia.adapter = mediaAdapter


        //设置点击事件
        folderAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<MediaFolder, *>, view: View, i: Int ->
            val item = folderAdapter.getItem(i)
            viewModel.setKey(item?.path)
        }


        //配置recycleView
        binding.rvFolder.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFolder.adapter = folderAdapter


        //默认显示加载中
        binding.multiStateContainer.showLoading()
    }


    override fun invalidate() = withState(viewModel) {

        if (it.key.isNullOrEmpty()) {
            binding.rvFolder.visibility = View.VISIBLE
            binding.rvMedia.visibility = View.INVISIBLE
            if (it.folderList is Success) {
                if (it.folderList.invoke().isNullOrEmpty()) {
                    //显示无数据
                    binding.multiStateContainer.showEmpty()
                } else {
                    //显示数据
                    binding.multiStateContainer.showSuccess()
                    folderAdapter.submitList(it.folderList.invoke())
                }
            } else if (it.folderList is Fail) {
                //显示错误
                binding.multiStateContainer.showError {
                    viewModel.fetchFolderDataSelf()
                }
            } else {
                //显示加载中
                binding.multiStateContainer.showLoading()
            }
        } else {
            binding.rvFolder.visibility = View.INVISIBLE
            binding.rvMedia.visibility = View.VISIBLE
            if (it.mediaList is Success) {
                if (it.mediaList.invoke().isNullOrEmpty()) {
                    //显示无数据
                    binding.multiStateContainer.showEmpty()
                } else {
                    //显示数据
                    binding.multiStateContainer.showSuccess()
                    mediaAdapter.submitList(it.mediaList.invoke())
                }
            } else if (it.mediaList is Fail) {
                //显示错误
                binding.multiStateContainer.showError {
                    viewModel.fetchMediaDataSelf()
                }
            } else {
                //显示加载中
                binding.multiStateContainer.showLoading()
            }
        }

    }
}

//视频列表ViewModel
abstract class MediaGroupViewModel(initialState: MediaFolderState) :
    MavericksViewModel<MediaFolderState>(
        initialState
    ) {

    init {
        //查询文件夹列表
        fetchFolderDataSelf()

        //当key发生改变后,立即查询媒体列表
        onEach(MediaFolderState::key) {
            it?.apply { fetchMediaDataSelf() }
        }
    }


    //更新播放列表,并且播放
    abstract fun updatePlayListAndPlay(url: String)

    //加载文件夹数据
    abstract fun fetchFolderDataSync(): List<MediaFolder>?

    //加载媒体数据
    abstract fun fetchMediaDataSync(key: String): List<MediaFolder>?

    fun setKey(key: String?) = setState {
        copy(key = key)
    }

    fun play() {

    }


    fun fetchMediaDataSelf() = withState {
        if (!it.key.isNullOrEmpty()) {
            suspend {
                val all = listOf(MediaFolder())
                val result = fetchMediaDataSync(it.key)
                if (result != null) {
                    all + result
                } else {
                    all
                }
            }.execute { result -> copy(mediaList = result) }
        }
    }

    fun fetchFolderDataSelf() {
        suspend { fetchFolderDataSync() }.execute { copy(folderList = it) }
    }
}

//视频列表页面状态
data class MediaFolderState(
    val folderList: Async<List<MediaFolder>?> = Uninitialized,//文件夹列表
    val mediaList: Async<List<MediaFolder>?> = Uninitialized,//媒体列表
    val key: String? = null,//当前查询key,如果为null则显示文件夹列表,如果有则显示媒体列表
) : MavericksState

data class MediaFolder(
    val name: String = "",//显示名称
    val path: String = "",//查询key
    //val fileCount: Long = 0
)


//文件夹适配器
class FolderAdapter : BaseQuickAdapter<MediaFolder, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: MediaFolder?) {
        holder.setText(R.id.textView, item?.name)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_folder_list, parent)
    }

}

///媒体适配器
class MediaAdapter : BaseQuickAdapter<MediaFolder, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: MediaFolder?) {
        if (position == 0) {
            holder.setText(R.id.textView, "..")
        } else {
            holder.setText(R.id.textView, item?.name)
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

