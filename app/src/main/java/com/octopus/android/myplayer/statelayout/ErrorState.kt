package com.octopus.android.myplayer.statelayout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.octopus.android.myplayer.R
import com.octopus.android.myplayer.utils.setOnClickListenerWithInterval
import com.zy.multistatepage.MultiState
import com.zy.multistatepage.MultiStateContainer

class ErrorState : MultiState() {

    var callback: (() -> Unit)? = null
    override fun onCreateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        return inflater.inflate(R.layout.layout_error, container, false)
    }

    override fun onViewCreated(view: View) {
        view?.setOnClickListenerWithInterval {
            callback?.invoke()
        }
    }
}