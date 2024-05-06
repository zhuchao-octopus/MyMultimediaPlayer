package com.octopus.android.multimedia.fragments.fm

import com.car.api.ApiRadio

class AMBand(
    override var channel: Int = 522,
    override val minChannel: Int = 522,
    override val maxChannel: Int = 1620,
    override val step: Int = 9,
    override val unit: String = "KHz",
    override val supportRDS: Boolean = false,
    override val supportST: Boolean = false,
    override val searchChannels: IntArray = intArrayOf(522, 522, 522, 522, 522, 522)
) : BaseBand() {
    override fun getDisplayChannel(channel: Int): String {
        return channel.toString()
    }

    override fun getDisplayName(): String {
        return "AM ${searchPageIndex + 1}"
    }

    override fun updateSearchChannel(index: Int, channel: Int) {
        val realIndex = index - ApiRadio.CHANNEL_AM_INDEX_BEGIN
        if (realIndex >= 0 && realIndex < searchChannels.size) {
            searchChannels[realIndex] = selfChannelValue(channel)
        }
    }
}