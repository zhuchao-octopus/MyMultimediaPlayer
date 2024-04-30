package com.octopus.android.multimedia.fragments.fm

/**
 * 调频管理器,支持多种调频模式混合在一个列表中
 * */
data class BandManager(
    val bandList: MutableList<Band> = mutableListOf(Band.obtainFm(), Band.obtainAm()),//fm/am 列表
    val index: Int = 0 //当前选中项索引
) {
    //切换fm/am
    fun toggleBand(): BandManager {
        var index = index + 1
        if (index >= bandList.size) {
            index = 0
        }
        return copy(index = index)
    }

    //设置当前频道
    fun setChannel(channel: Int): BandManager {
        //拷贝并且修改数组
        val list = bandList.toMutableList()
        val item = list[index]
        val copyItem = item.copy(channel = channel)
        list.removeAt(index)
        list.add(index, copyItem)

        return copy(bandList = list)
    }

    //设置当前选中项
    fun setSelected(selectedIndex: Int?): BandManager {
        //拷贝并且修改数组
        val list = bandList.toMutableList()
        val item = list[index]
        val copyItem = if (selectedIndex != null) item.copy(
            selectedSearchChannelIndex = selectedIndex,
            channel = item.searchChannels[selectedIndex]
        ) else item.copy(
            selectedSearchChannelIndex = null,
        )
        list.removeAt(index)
        list.add(index, copyItem)

        return copy(bandList = list)
    }


    //获取当前调频对象
    fun currentBand(): Band {
        return bandList[index]
    }
}