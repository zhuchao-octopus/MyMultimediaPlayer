package com.octopus.android.myplayer.statelayout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.octopus.android.myplayer.R
import com.zy.multistatepage.MultiState
import com.zy.multistatepage.MultiStateContainer

class EmptyState: MultiState() {
    override fun onCreateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        return inflater.inflate(R.layout.layout_empty, container, false)
    }

    override fun onViewCreated(view: View) {

    }
}