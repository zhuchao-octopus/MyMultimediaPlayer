package com.octopus.android.multimedia.fragments

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
import com.octopus.android.multimedia.databinding.FragmentVideoSortBinding
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.toastLong
import com.octopus.android.multimedia.utils.viewBinding
import com.zhuchao.android.session.Cabinet
import java.io.File


/**
 * 视频分类页面
 * */
class VideoSortFragment : BaseFragment(R.layout.fragment_video_sort) {

    private val binding: FragmentVideoSortBinding by viewBinding()
    private val viewModel: VideoSortViewModel by fragmentViewModel()

    override fun invalidate() = withState(viewModel) {
        if (it.index != binding.viewPager.currentItem) {
            binding.viewPager.currentItem = it.index
        }

        when (it.index) {
            0 -> {
                binding.ivPlayList.isSelected = true
                binding.ivSD.isSelected = false
                binding.ivUsb.isSelected = false
                binding.ivFolder.isSelected = false

                binding.tvPlayList.setTextColor(requireContext().getColor(R.color.colorAccent))
                binding.tvSD.setTextColor(requireContext().getColor(R.color.white))
                binding.tvUSB.setTextColor(requireContext().getColor(R.color.white))
                binding.tvFolder.setTextColor(requireContext().getColor(R.color.white))
            }

            1 -> {
                binding.ivPlayList.isSelected = false
                binding.ivSD.isSelected = true
                binding.ivUsb.isSelected = false
                binding.ivFolder.isSelected = false

                binding.tvPlayList.setTextColor(requireContext().getColor(R.color.white))
                binding.tvSD.setTextColor(requireContext().getColor(R.color.colorAccent))
                binding.tvUSB.setTextColor(requireContext().getColor(R.color.white))
                binding.tvFolder.setTextColor(requireContext().getColor(R.color.white))
            }

            2 -> {
                binding.ivPlayList.isSelected = false
                binding.ivSD.isSelected = false
                binding.ivUsb.isSelected = true
                binding.ivFolder.isSelected = false

                binding.tvPlayList.setTextColor(requireContext().getColor(R.color.white))
                binding.tvSD.setTextColor(requireContext().getColor(R.color.white))
                binding.tvUSB.setTextColor(requireContext().getColor(R.color.colorAccent))
                binding.tvFolder.setTextColor(requireContext().getColor(R.color.white))
            }

            3 -> {
                binding.ivPlayList.isSelected = false
                binding.ivSD.isSelected = false
                binding.ivUsb.isSelected = false
                binding.ivFolder.isSelected = true

                binding.tvPlayList.setTextColor(requireContext().getColor(R.color.white))
                binding.tvSD.setTextColor(requireContext().getColor(R.color.white))
                binding.tvUSB.setTextColor(requireContext().getColor(R.color.white))
                binding.tvFolder.setTextColor(requireContext().getColor(R.color.colorAccent))
            }

            else -> {
            }
        }


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
        binding.viewSD.setOnClickListenerWithInterval {
            viewModel.setIndex(1)
        }
        binding.viewUsb.setOnClickListenerWithInterval {
            viewModel.setIndex(2)
            Cabinet.getPlayManager().localUSBMediaVideos.saveToFile(
                this.context,
                "localUSBMediaVideos"
            )
        }


        binding.viewFolder.setOnClickListenerWithInterval {
            //viewModel.setIndex(3)

            try {
                pickMedia.launch(PickVisualMediaRequest(VideoOnly))
            } catch (e: ActivityNotFoundException) {
                toastLong("当前设备不支持选择视频")
            } catch (e: Throwable) {
                Log.e("VideoSortFragment", e.toString())
            }

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
                return 3
            }

            override fun createFragment(position: Int): Fragment {
                if (position == 0) {
                    return PlayListFragment()
                } else if (position == 1) {
                    return SDFragment()
                } else if (position == 2) {
                    return USBFragment()
                } else if (position == 3) {
                    return FolderFragment()
                }
                return Fragment()
            }
        }

        if (savedInstanceState == null) {
            //默认选中第一个
            binding.ivPlayList.isSelected = true
            binding.tvPlayList.setTextColor(requireContext().getColor(R.color.colorAccent))
        }
    }


}

class VideoSortViewModel(initialState: VideoSortState) : MavericksViewModel<VideoSortState>(
    initialState
) {
    fun setIndex(index: Int) = setState {
        copy(index = index)
    }
}


data class VideoSortState(
    val index: Int = 0, //当前选中项
) : MavericksState



