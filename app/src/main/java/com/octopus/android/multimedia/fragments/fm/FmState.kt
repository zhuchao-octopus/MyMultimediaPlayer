package com.octopus.android.multimedia.fragments.fm

import com.airbnb.mvrx.MavericksState

/**
 * 收音机状态
 * */
data class FmState(
    val channelProgress: Int = 0, //频道进度
    val searching: Boolean = false,//搜索中
    val ptyMode: String? = null,//pty类型,名称取值范围 @Link{ApiRadio.PTY_DISPLAY}
    val afEnable: Boolean = false,//af开启状态
    val taEnable: Boolean = false,//ta开启状态
    val rdsEnable: Boolean = true,//rds功能开启状态,fm模式可以使用,am模式下无法使用
    val stEnable: Boolean = false,//是否开立体声道显示
    val stState: Boolean = false,//当前立体声状态,取值范围0-1,0:表示非立体声,1:表示立体声,参考demo MainRootView类中的 ApiRadio.stero(DataRadio.getmRadioStState() == 1 ? 0 : 1);
    val fmBand: BaseBand = FMBand(),
    val amBand: BaseBand = AMBand(),
    val currentBand: BaseBand = fmBand,
    val afVisible: Boolean = true,//af文本闪烁状态
    val count: Int = 0

) : MavericksState