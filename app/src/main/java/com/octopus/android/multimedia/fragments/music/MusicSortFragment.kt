package com.octopus.android.multimedia.fragments.music

import android.content.ActivityNotFoundException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VideoOnly
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.asMavericksArgs
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentMusicSortBinding
import com.octopus.android.multimedia.databinding.FragmentVideoSortBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.toastLong
import com.octopus.android.multimedia.utils.viewBinding
import com.zhuchao.android.session.Cabinet
import java.io.File


/**
 * 音乐分类页面
 * */
class MusicSortFragment : BaseFragment(R.layout.fragment_music_sort) {

    private val binding: FragmentMusicSortBinding by viewBinding()
    private val viewModel: MusicSortViewModel by fragmentViewModel()

    override fun invalidate() = withState(viewModel) {
        if (it.index != binding.viewPager.currentItem) {
            binding.viewPager.setCurrentItem(it.index, false)
        }



        binding.ivPlayList.isSelected = it.index == 0
        binding.ivArtists.isSelected = it.index == 1
        binding.ivAlbums.isSelected = it.index == 2
        binding.ivFolder.isSelected = it.index == 3
        binding.ivCollection.isSelected = it.index == 4

        binding.tvPlayList.setTextColor(
            if (it.index == 0) requireContext().getColor(R.color.colorAccent) else requireContext().getColor(
                R.color.white
            )
        )
        binding.tvArtists.setTextColor(
            if (it.index == 1) requireContext().getColor(R.color.colorAccent) else requireContext().getColor(
                R.color.white
            )
        )
        binding.tvAlbums.setTextColor(
            if (it.index == 2) requireContext().getColor(R.color.colorAccent) else requireContext().getColor(
                R.color.white
            )
        )
        binding.tvFolder.setTextColor(
            if (it.index == 3) requireContext().getColor(R.color.colorAccent) else requireContext().getColor(
                R.color.white
            )
        )
        binding.tvCollection.setTextColor(
            if (it.index == 4) requireContext().getColor(R.color.colorAccent) else requireContext().getColor(
                R.color.white
            )
        )


    }


    private val pickMedia =
        registerForActivityResult(PickVisualMedia()) {
            //跳转到播放页面
            it?.apply {
                findNavController().navigate(
                    R.id.action_videoSortFragment_to_videoPlayFragment,
                    asMavericksArgs()
                )
            }

        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPlayList.setOnClickListenerWithInterval {
            viewModel.setIndex(0)
        }
        binding.viewArtists.setOnClickListenerWithInterval {
            viewModel.setIndex(1)
        }
        binding.viewAlbums.setOnClickListenerWithInterval {
            viewModel.setIndex(2)
        }


        binding.viewFolder.setOnClickListenerWithInterval {
            viewModel.setIndex(3)

//            try {
//                pickMedia.launch(PickVisualMediaRequest(VideoOnly))
//            } catch (e: ActivityNotFoundException) {
//                toastLong("当前设备不支持选择视频")
//            } catch (e: Throwable) {
//                Log.e("VideoSortFragment", e.toString())
//            }

        }

        binding.viewCollection.setOnClickListenerWithInterval {
            viewModel.setIndex(4)
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setIndex(position)
            }
        })

        //配置viewpager
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 5
            }

            override fun createFragment(position: Int): Fragment {
                if (position == 0) {
                    return MusicPlayListFragment()
                } else if (position == 1) {
                    return MusicArtistsFragment()
                } else if (position == 2) {
                    return MusicAlbumsFragment()
                } else if (position == 3) {
                    return MusicFolderFragment()
                } else if (position == 4) {
                    return MusicCollectionFragment()
                }
                return Fragment()
            }
        }

        binding.ivList.setOnClickListenerWithInterval {
            findNavController().navigate(R.id.action_musicSortFragment_to_musicPlayFragment)
        }

        if (savedInstanceState == null) {
            //默认选中第一个
            binding.ivPlayList.isSelected = true
            binding.tvPlayList.setTextColor(requireContext().getColor(R.color.colorAccent))
        }
    }


}

class MusicSortViewModel(initialState: MusicSortState) : MavericksViewModel<MusicSortState>(
    initialState
) {
    fun setIndex(index: Int) = setState {
        copy(index = index)
    }
}


data class MusicSortState(
    val index: Int = 0, //当前选中项
) : MavericksState



