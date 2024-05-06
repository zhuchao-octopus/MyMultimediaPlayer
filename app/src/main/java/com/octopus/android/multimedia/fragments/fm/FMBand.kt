package com.octopus.android.multimedia.fragments.fm

import com.car.api.ApiRadio

class FMBand(
    override var channel: Int = 8750,
    override val minChannel: Int = 8750,
    override val maxChannel: Int = 10800,
    override val step: Int = 10,
    override val unit: String = "MHz",
    override val supportRDS: Boolean = true,
    override val supportST: Boolean = true,
    override val searchChannels: IntArray = intArrayOf(
        8750, 8750, 8750, 8750, 8750, 8750,
        8750, 8750, 8750, 8750, 8750, 8750,
        8750, 8750, 8750, 8750, 8750, 8750
    )
) : BaseBand() {
    override fun getDisplayChannel(channel: Int): String {
        return String.format("%.2f", (channel / 100f)) //保留两位小数
    }

    override fun getDisplayName(): String {
        return "FM ${searchPageIndex + 1}"
    }

    override fun updateSearchChannel(index: Int, channel: Int) {
        val realIndex = index - ApiRadio.CHANNEL_FM_INDEX_BEGIN
        if (realIndex >= 0 && realIndex < searchChannels.size) {
            searchChannels[realIndex] = channel
        }
    }
}