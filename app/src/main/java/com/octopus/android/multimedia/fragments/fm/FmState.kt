package com.octopus.android.multimedia.fragments.fm

import androidx.annotation.StringDef
import com.airbnb.mvrx.MavericksState

/**
 * 收音机状态
 * */
data class FmState(
    val channelProgress: Int = 0, //频道进度
    val searching: Boolean = false,//搜索中

    @RDSMode
    val rdsMode: String = RDS_MODE_PTY,//rds模式
    val ptyMode: String? = null,//pty类型,名称取值范围 @Link{ApiRadio.PTY_DISPLAY}
    val afEnable: Boolean = false,//af开启状态,默认false
    val rdsEnable: Boolean = true,//rds功能开启状态,fm模式可以使用,am模式下无法使用
    val stEnable: Boolean = false,//是否开立体声道显示
    val bandManager: BandManager = BandManager()

) : MavericksState {
    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @StringDef(RDS_MODE_AF, RDS_MODE_PTY, RDS_MODE_TA)
        annotation class RDSMode

        const val RDS_MODE_AF = "AF"
        const val RDS_MODE_PTY = "PTY"
        const val RDS_MODE_TA = "TA"
    }

    //获取当前调频模式
    fun getCurrentBlandMode(): Band {
        return bandManager.currentBand()
    }
}