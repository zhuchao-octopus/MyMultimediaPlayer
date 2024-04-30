package com.octopus.android.multimedia.fragments.fm

/**
 * 调频模式
 * */
data class Band(
    val type: String,
    val name: String,//模式名称,例如FM,FM1
    val channel: Int, //当前频道值
    val minChannel: Int,//最小频道
    val maxChannel: Int,//最大频道
    val step: Int,//步进
    val unit: String,//单位名称
    val supportRDS: Boolean,//是否支持RDS功能
    val supportST: Boolean,//是否支持立体声
    val searchChannels: List<Int>,//搜索频道列表
    val selectedSearchChannelIndex: Int?//当前选中的搜索结果
) {
    companion object {

        fun display(type: String, value: Int): String {
            if (type.equals("AM", true)) {
                return value.toString()
            } else if (type.equals("FM", true)) {
                return String.format("%.2f", (value / 100f)) //保留两位小数
            }
            return ""
        }

        //am 模式
        fun obtainAm(): Band {
            return Band(
                "AM",
                "AM",
                522,
                522,
                1620,
                9,
                "KHz",
                supportRDS = false,
                supportST = false,
                searchChannels = listOf(522, 522, 522, 756, 999, 981),
                selectedSearchChannelIndex = null
            )
        }

        // fm 模式
        fun obtainFm(): Band {
            return Band(
                "FM",
                "FM",
                8750,
                8750,
                10800,
                10,
                "MHz",
                supportRDS = true,
                supportST = true,
                searchChannels = listOf(8750, 8750, 8750, 8750, 8750, 8980),
                selectedSearchChannelIndex = null
            )
        }
    }
}