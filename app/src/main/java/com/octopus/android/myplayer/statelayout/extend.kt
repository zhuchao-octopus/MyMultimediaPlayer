package com.octopus.android.myplayer.statelayout

import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.state.SuccessState


fun MultiStateContainer.showSuccess() {
    show<SuccessState>()
}

fun MultiStateContainer.showError(callBack: (() -> Unit)? = null) {
    show<ErrorState> { it.callback = callBack }
}

fun MultiStateContainer.showEmpty() {
    show<EmptyState>()
}

fun MultiStateContainer.showLoading() {
    show<LoadingState>()
}