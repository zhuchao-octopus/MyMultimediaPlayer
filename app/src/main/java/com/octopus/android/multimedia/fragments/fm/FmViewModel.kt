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


class FmViewModel(initialState: FmState) : MavericksViewModel<FmState>(initialState),
    Connection.OnCallbackListener {

    private var connection: Connection? = Connection(this)

    init {
        //与fm服务建立连接
        connection!!.connect(MApplication.getAppContext())

        //打开收音机
        ApiMain.appId(ApiMain.APP_ID_RADIO, ApiMain.APP_ID_RADIO)
        CarService.me().cmd(ApiRadio.CMD_POWER, ApiKit.ON)

        //打开默认频道
        switchChannel()
    }

    override fun onCleared() {
        super.onCleared()

        //与fm服务断开连接
        connection!!.disconnect(MApplication.getAppContext())
    }

    //向后搜索
    fun nextSearch() {
        setState {
            copy(bandManager = bandManager.setSelected(null))
        }
        ApiRadio.seekUp()

    }

    //向后搜索
    fun prevSearch() {
        setState {
            copy(bandManager = bandManager.setSelected(null))
        }
        ApiRadio.seekDown()
    }

    fun prevChannel() {
        ApiRadio.prevChannel()
    }

    fun nextChannel() {
        ApiRadio.nextChannel()
    }

    fun search() {
        ApiRadio.search(ApiKit.SWITCH)
    }


    fun test() {

        //测试修改数组
//        setState {
//            Log.d("test", "修改前hashcode:${bandList.hashCode()}")
//            val list = bandList.map { it.clone() }.toMutableList()
//
//            if (bandIndex < list.size) {
//                list[bandIndex].searchChannels = listOf(8888, 8888, 8888, 8888, 8888, 8888)
//
//            }
//
//            Log.d("test", "修改后hashcode:${list.hashCode()}")
//
//
//            copy(bandList = list)
//
//        }
    }

    //切换调频模式
    fun toggleFmOrAm() = setState {
        //改变当前调频模式索引,向后循环模式
        copy(bandManager = bandManager.toggleBand()).apply {
            switchChannel()
        }
    }

    fun setRdsTPY(index:Int){
        ApiRadio.rdsPtyEnable(index)
    }

    private fun switchChannel() = withState {

        //rds
        CarService.me()
            .cmd(ApiRadio.CMD_RDS_ENABLE, if (it.getCurrentBlandMode().supportRDS) 1 else 0)

        //修改调频模式
        if (it.getCurrentBlandMode().type.equals("FM", true)) {
            ApiRadio.band(ApiRadio.BAND_FM_INDEX_BEGIN)
        } else if (it.getCurrentBlandMode().type.equals("AM", true)) {
            ApiRadio.band(ApiRadio.BAND_AM_INDEX_BEGIN)
        }
        //修改频道
        ApiRadio.freq(ApiRadio.FREQ_DIRECT, it.getCurrentBlandMode().channel)
    }

    fun selectSearchChannel(index: Int) = setState {
        //改变当前选中调频模式中的搜索选中状态

        copy(bandManager = bandManager.setSelected(index)).apply {
            //设置收音机频率
            ApiRadio.freq(ApiRadio.FREQ_DIRECT, getCurrentBlandMode().channel)
        }

    }


    //根据进度设置当前频道
    fun setChannelByProgress2(progress: Int) = setState {
        val temp = progress % getCurrentBlandMode().step //与最小步进取余
        var channel: Int
        if (temp == 0) {
            channel = progress
        } else {
            val result = progress - temp
            if (result > getCurrentBlandMode().maxChannel) {
                channel = getCurrentBlandMode().maxChannel
            } else if (result < getCurrentBlandMode().minChannel) {
                channel = getCurrentBlandMode().minChannel
            } else {
                channel = result
            }
        }

        copy(bandManager = bandManager.setChannel(channel)).apply {
            //设置收音机频率
            ApiRadio.freq(ApiRadio.FREQ_DIRECT, getCurrentBlandMode().channel)
        }

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
                        copy(bandManager = bandManager.setChannel(value))
                    }
                }
            }
        }
    }

    override fun isUpdateOnUIThread(): Boolean {
        return false
    }
}