package com.octopus.android.multimedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MavericksView
import com.octopus.android.multimedia.utils.autoCalcBaseOnWidth
import com.octopus.android.multimedia.utils.autoCalcSizeInDp
import com.zhuchao.android.session.Cabinet
import me.jessyan.autosize.AutoSize

abstract class BaseFragment(@LayoutRes containerLayoutId: Int = 0) : Fragment(containerLayoutId),
    MavericksView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AutoSize.autoConvertDensity(
            requireActivity(),
            requireActivity().application.autoCalcSizeInDp(),
            requireActivity().application.autoCalcBaseOnWidth()
        )

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Cabinet.getEventBus().registerEventObserver(this);
    }

    override fun onDestroy() {
        super.onDestroy()
        Cabinet.getEventBus().unRegisterEventObserver(this);
    }


    override fun invalidate() {

    }

}