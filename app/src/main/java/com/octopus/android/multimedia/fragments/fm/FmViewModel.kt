package com.octopus.android.multimedia.fragments.fm

import android.os.Bundle
import android.util.Log
import com.airbnb.mvrx.MavericksViewModel
import com.car.api.ApiKey
import com.car.api.ApiKit
import com.car.api.ApiMain
import com.car.api.ApiRadio
import com.car.api.CarService
import com.car.ipc.Connection
import com.car.ipc.ICallback
import com.car.ipc.IRemote
import com.zhuchao.android.session.MApplication
import java.util.Timer
import java.util.TimerTask


/**
 * 收音机viewModel
 * 负责与radio sdk进行交互
 * */
class FmViewModel(initialState: FmState) : MavericksViewModel<FmState>(initialState),
    Connection.OnCallbackListener {

    private var connection: Connection? = Connection(this)

    private var afTimer: Timer? = null


    init {
        //与fm服务建立连接
        connection!!.connect(MApplication.getAppContext())


        //打开默认频道
        switchChannel()
    }

    override fun onCleared() {
        super.onCleared()

        //关闭定时器
        cancelAFTimer()

        //关闭收音机
        CarService.me().cmd(ApiRadio.CMD_POWER, ApiKit.OFF)

        //与fm服务断开连接
        connection!!.disconnect(MApplication.getAppContext())

    }

    //向后搜索
    fun nextSearch() {
        setState {
            currentBand.setSelectedSearchIndex(-1)
            copy(count = count + 1)
        }
        ApiRadio.seekUp()

    }

    //向后搜索
    fun prevSearch() {
        setState {
            currentBand.setSelectedSearchIndex(-1)
            copy(count = count + 1)
        }
        ApiRadio.seekDown()
    }

    fun prevChannel() {
        // ApiRadio.prevChannel()

        ApiRadio.freqDown()
    }

    fun nextChannel() {
        //ApiRadio.nextChannel()
        ApiRadio.freqUp()
    }

    fun search() {
        ApiRadio.search(ApiKit.SWITCH)
    }


    //切换调频模式
    fun toggleFmOrAm() = withState {
        if (!it.currentBand.nextSearchPage()) {
            setState {
                copy(
                    currentBand = if (currentBand == fmBand) amBand else fmBand,
                    count = count + 1
                )
            }
        } else {
            setState {
                copy(
                    count = count + 1
                )
            }
        }
        switchChannel()
    }


    //切换st启用状态
    fun toggleStEnable() = withState {
        ApiRadio.stero(if (it.stEnable) 0 else 1)
    }

    fun setRdsTPY(index: Int) {
        ApiRadio.rdsPtyEnable(index)
    }

    fun setRdsSeek(index: Int) {
        //ApiRadio.rdsPtyEnable(1)
        ApiRadio.rdsPtySeek(index)
    }

    fun toggleRdsTa() = withState {
        ApiRadio.rdsTaEnable(if (it.taEnable) 0 else 1)
    }

    fun toggleRdsAf() = withState {
        if (it.afEnable) {
            //关闭af功能
            ApiRadio.rdsAfEnable(0)
        } else {
            //开启af功能
            ApiRadio.rdsAfEnable(1)
        }

    }

    private fun startAFTimer() {
        cancelAFTimer()
        afTimer = Timer()
        afTimer?.schedule(object : TimerTask() {
            override fun run() {
                toggleAfVisible()
            }
        }, 0L, 1000L)
    }

    private fun cancelAFTimer() {
        if (afTimer != null) {
            afTimer?.cancel()
            afTimer = null
        }
    }

    //切换af文本显示状态
    fun toggleAfVisible() = setState {
        copy(afVisible = !afVisible)
    }


    private fun switchChannel() = withState {

        //rds
//        if (it.currentBand.supportRDS) {
//            ApiRadio.rdsEnable(1)
//        } else {
//            ApiRadio.rdsEnable(0)
//        }

        //修改调频模式
        if (it.currentBand is FMBand) {
            ApiRadio.band(ApiRadio.BAND_FM_INDEX_BEGIN)
        } else if (it.currentBand is AMBand) {
            ApiRadio.band(ApiRadio.BAND_AM_INDEX_BEGIN)
        }
        //修改频道
        ApiRadio.freq(ApiRadio.FREQ_DIRECT, it.currentBand.channel)
    }

    fun selectSearchChannel(index: Int) = setState {
        //改变当前选中调频模式中的搜索选中状态

        currentBand.setSelectedSearchIndex(index)

        ApiRadio.freq(ApiRadio.FREQ_DIRECT, currentBand.getSelectedSearchChannel())
        copy(count = count + 1)
    }


    //根据进度设置当前频道
    fun setChannelByProgress2(progress: Int) = setState {
        val temp = progress % currentBand.step //与最小步进取余
        var channel: Int
        if (temp == 0) {
            channel = progress
        } else {
            val result = progress - temp
            if (result > currentBand.maxChannel) {
                channel = currentBand.maxChannel
            } else if (result < currentBand.minChannel) {
                channel = currentBand.minChannel
            } else {
                channel = result
            }
        }

        //currentBand.channel = channel

        ApiRadio.freq(ApiRadio.FREQ_DIRECT, channel)

        copy(count = count + 1)
    }

    override fun onConnected(remote: IRemote?, callback: ICallback?) {
        remote?.register(
            arrayOf(
                ApiRadio.UPDATE_FREQ_INFO,
                ApiRadio.UPDATE_BAND,
                ApiRadio.UPDATE_FREQ,
                ApiRadio.UPDATE_CHANNEL,
                ApiRadio.UPDATE_CHANNEL_FREQ,
                ApiMain.UPDATE_ACC_STATE,
                ApiMain.UPDATE_APP_ID,
                ApiRadio.UPDATE_POWER,
                ApiRadio.UPDATE_PTY_ID,
                ApiRadio.UPDATE_RDS_TP,
                ApiRadio.UPDATE_RDS_TA,
                ApiRadio.UPDATE_RDS_TEXT,
                ApiRadio.UPDATE_STEREO,
                ApiRadio.UPDATE_RDS_AF_ENABLE,
                ApiRadio.UPDATE_RDS_TA_ENABLE,
                ApiRadio.UPDATE_RDS_TA,
                ApiRadio.UPDATE_RDS_ENABLE,
                ApiRadio.UPDATE_LOC,
                ApiRadio.UPDATE_RDS_CHANNEL_TEXT,
                ApiKey.UPDATE_KEY_EVENT
            ), callback, true
        )
    }

    override fun onUpdate(params: Bundle?) {
        params?.apply {
            Log.i("fm onUpdate", "\n")
            for (key in keySet()) {
                Log.i("fm onUpdate", "key:$key,value:${get(key)}")
            }
            Log.i("fm onUpdate", "\n")


            when (getString("id")) {
                ApiRadio.UPDATE_FREQ -> {
                    val value = params.getInt("value")
                    setState {
                        currentBand.channel = value
                        copy(count = count + 1)
                    }
                }

                //st功能启用状态
                ApiRadio.UPDATE_STEREO -> {
                    val value = params.getInt("value")
                    setState { copy(stEnable = value == 1) }
                }

                ApiRadio.UPDATE_RDS_AF_ENABLE -> {
                    val value = params.getInt("value")
                    if (value == 1) {
                        //开启定时器
                        startAFTimer()
                    } else {
                        //关闭定时器
                        cancelAFTimer()
                    }
                    setState { copy(afEnable = value == 1) }
                }

                ApiRadio.UPDATE_RDS_TA_ENABLE -> {
                    val value = params.getInt("value")
                    setState { copy(taEnable = value == 1) }
                }

                //更新搜索频道
                ApiRadio.UPDATE_CHANNEL_FREQ -> {
                    val channel = params.getInt("channel")
                    val freq = params.getInt("freq")

                    withState {
                        it.fmBand.updateSearchChannel(channel, freq)
                        it.amBand.updateSearchChannel(channel, freq)
                        setState { copy(count = count + 1) }
                    }

                }

//                ApiRadio.UPDATE_BAND -> {
//                    val value = params.getInt("value")
//                    setState { copy(taEnable = value == 1) }
//                }

            }
        }
    }

    override fun isUpdateOnUIThread(): Boolean {
        return false
    }
}