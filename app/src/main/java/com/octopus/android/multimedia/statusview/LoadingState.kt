package com.octopus.android.multimedia.statusview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.octopus.android.multimedia.R
import com.zy.multistatepage.MultiState
import com.zy.multistatepage.MultiStateContainer

class LoadingState : MultiState() {
    override fun onCreateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        return inflater.inflate(R.layout.layout_loading, container, false)
    }

    override fun onViewCreated(view: View) {

    }
}