package com.octopus.android.multimedia.fragments.fm


abstract class BaseBand {
    abstract var channel: Int //当前频道值
    abstract val minChannel: Int//最小频道
    abstract val maxChannel: Int//最大频道
    abstract val step: Int//步进
    abstract val unit: String//单位名称
    abstract val supportRDS: Boolean//是否支持RDS功能
    abstract val supportST: Boolean
    abstract val searchChannels: IntArray//搜索频道列表
    private val searchPageSize: Int = 6//每页显示的搜索结果数量
    var searchPageIndex: Int = 0//当前显示搜索结果的页码
    private var selectedSearchChannelIndex: Int = -1//当前选中的搜索结果索引,-1表示未选中


    //获取显示频道字符
    abstract fun getDisplayChannel(channel: Int): String

    //根据类型和页码,获取显示名称,如FM1
    abstract fun getDisplayName(): String

    //更新搜索结果,这里需要注意index的计算,需要参考趣智达sdk,sdk返回了100条数据,这里只需要根据我们需要的条数,指定规则
    abstract fun updateSearchChannel(index: Int, channel: Int)

    //判断搜索结果是否为最后一页
    private fun isLastSearchPage(): Boolean {
        if (searchPageSize == 0 && searchChannels == null)
            return true

        val surplus = if (searchChannels.size % searchPageSize == 0) 0 else 1;
        return searchPageIndex == (searchChannels.size / searchPageSize) - 1 + surplus
    }

    /**
     * 显示搜索结果的下一页
     * @return true :表示有且会移动到下一页,,false表示没有最后一页,且移动到第一页
     * */

    fun nextSearchPage(): Boolean {
        if (isLastSearchPage()) {
            searchPageIndex = 0
            return false
        }
        searchPageIndex++
        return true
    }

    /**
     * 设置选中项索引,这里需要处理当前页码逻辑
     * */
    fun setSelectedSearchIndex(index: Int) {
        selectedSearchChannelIndex = searchPageIndex * searchPageSize + index
    }

    fun getSelectedSearchIndex(): Int {
        return selectedSearchChannelIndex
    }

    /**
     * 获取当前选择的频道值
     * */
     fun getSelectedSearchChannel(): Int {
        val index = getSelectedSearchIndex()
        if (index < searchChannels.size)
            return searchChannels[index]
        return minChannel
    }

    fun selfChannelValue(value: Int): Int {
        if (value == 0)
            return minChannel
        return value
    }

}



