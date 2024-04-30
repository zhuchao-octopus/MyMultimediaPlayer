package com.octopus.android.multimedia.fragments.bluetooth

import android.os.Bundle
import android.util.Log
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.car.api.ApiBt
import com.car.api.ApiMain
import com.car.api.ApiVa
import com.car.api.CarService
import com.car.ipc.Connection
import com.car.ipc.Connection.OnCallbackListener
import com.car.ipc.ICallback
import com.car.ipc.IRemote
import com.zhuchao.android.session.MApplication


data class CallLog(
    val type: String?,//类型
    val name: String?,//名称
    val number: String?,//号码
    val time: Long?,//通话时间
    val deleteKey: Int = 0//删除key
)

/**
 * 联系人
 * */
data class Contacts(
    val name: String?,//名称
    val number: String?//号码
)

/**
 * 蓝牙设备
 * */
data class BTDevice(
    val deviceName: String? = null,//设备名称
    val phoneMacAddress: String? = null, //设备mac地址
    val pairState: Int? = null,//配对状态,null未配对,0:未连接:1已连接
)

/**
 * 蓝牙相关状态
 * */
data class BluetoothState(
    val btState: Int = ApiBt.PHONE_STATE_DISCONNECTED, //蓝牙连接状态
    val contactsList: List<Contacts> = emptyList(), //联系人列表
    val contactsLoadTask: Async<Unit> = Uninitialized,//联系人加载状态
    val musicState: Int = ApiBt.PLAYSTATE_PAUSE,//音乐状态
    val musicTitle: String? = null,//音乐标题
    val musicArtist: String? = null,//音乐作者
    val outCallLogList: List<CallLog> = emptyList(),//呼出通话记录列表
    val inCallLogList: List<CallLog> = emptyList(),//呼入通话记录列表
    val missCallLogList: List<CallLog> = emptyList(),//未接通话记录列表
    val pinCode: String? = null,//pinCode
    val deviceName: String? = null,//设备名称
    val searchKey: String? = null,//搜索关键字
    val searchContactsList: Async<List<Contacts>> = Uninitialized,//搜索联系人结果
) : MavericksState

/**
 * 蓝牙ViewModel,与蓝牙服务具体的交互逻辑都在这里
 * */
class BluetoothViewModel(initialState: BluetoothState) :
    MavericksViewModel<BluetoothState>(initialState), OnCallbackListener {
    private var connection: Connection? = Connection(this)

    init {
        //创建远程IPC连接
        connection?.connect(MApplication.getAppContext())
        //页面中,没有开关蓝牙的入口,这里默认开启
        //openBluetooth()


    }

    override fun onCleared() {
        super.onCleared()
        //断开IPC连接
        connection?.disconnect(MApplication.getAppContext())
    }

    //删除单个联系人
    fun deleteContacts(contacts: Contacts) {
        val params = Bundle()
        params.putString("name", contacts.name)
        params.putString("number", contacts.number)
        CarService.me().cmd("bt.deleteContact", params)
        setState { copy(contactsList = contactsList - contacts) }
    }

    //获取蓝牙联系人，下载电话本
    fun fetchContacts() {
        setState { copy(contactsList = emptyList()) }
        CarService.me().cmd(ApiBt.CMD_DOWNLOAD_BOOK)
        searchContacts()

    }

    //打电话
    fun callNumber(number: String) {
        CarService.me().cmd(ApiBt.CMD_DIAL, number)
    }

    //接听
    fun pickup() {
        CarService.me().cmd(ApiBt.CMD_KEY, ApiBt.KEY_DIAL)
    }

    //挂断
    fun hang() {
        CarService.me().cmd(ApiBt.CMD_KEY, ApiBt.KEY_HANG)
    }

    //播放/暂停音乐
    fun playPauseMusic() {
        ApiBt.btavPlayPause()
    }

    //下一首
    fun nextMusic() {
        ApiBt.btavNext()
    }

    private fun chooseMusicChannel() {
        //如果需要播放器出声音，就需要切播放器
        ApiMain.appId(ApiMain.APP_ID_AUDIO_PLAYER, ApiMain.APP_ID_AUDIO_PLAYER)
    }

    //上一首
    fun prevMusic() {
        ApiBt.btavPrev()
    }

    //打开蓝牙
    fun openBluetooth() {
        //打开蓝牙/打开电话/打开蓝牙电话
        ApiVa.cmd(ApiVa.VA_CMD_BLUETOOTH, 1)
    }

    //获取呼出通话记录
    fun fetchOutCallLog() {
        // CarService.me().cmd(ApiBt.CMD_OUT_CALL_LOG)
        // ApiBt.outCallLog()
    }

    //获取呼入通话记录
    fun fetchInCallLog() {
        //  CarService.me().cmd(ApiBt.CMD_IN_CALL_LOG)
        //  CarService.me().cmd(ApiBt.CMD_DOWNLOAD_CALLLOG)

        // ApiBt.requestCallLog()

    }

    //获取未接通话记录
    fun fetchMissCallLog() {
        CarService.me().cmd(ApiBt.CMD_MISS_CALL_LOG)
    }

    //删除所有通话记录
    fun deleteAllCallLog() {
        CarService.me().cmd(ApiBt.CMD_DELETE_ALL_CALLLOG)
    }

    fun deleteInCallLog(callLog: CallLog) {
        if (callLog.time == null) {
            Log.e("deleteInCallLog", "delete fail! time is null")
            return
        }
        ApiBt.deleteOneCallLog(callLog.deleteKey, callLog.number, callLog.time)
        setState { copy(inCallLogList = inCallLogList - callLog) }
    }

    fun deleteOutCallLog(callLog: CallLog) {
        if (callLog.time == null) {
            Log.e("deleteOutCallLog", "delete fail! time is null")
            return
        }
        ApiBt.deleteOneCallLog(callLog.deleteKey, callLog.number, callLog.time)
        setState { copy(outCallLogList = outCallLogList - callLog) }
    }

    fun deleteMissCallLog(callLog: CallLog) {
        if (callLog.time == null) {
            Log.e("deleteMissCallLog", "delete fail! time is null")
            return
        }
        ApiBt.deleteOneCallLog(callLog.deleteKey, callLog.number, callLog.time)
        setState { copy(missCallLogList = missCallLogList - callLog) }
    }

    //设置PinCode
    fun setPinCode(value: String) {
        ApiBt.pinCode(value)
    }

    //设置设备名称
    fun setDeviceName(value: String) {
        ApiBt.localName(value)
    }

    //搜索蓝牙设备
    fun discover() {
        ApiBt.discover(300)
    }

    //查下已配对设备列表
    fun queryPairList() {
        ApiBt.queryPair()
    }

    fun connectDevice(deviceName: String) {
        ApiBt.connectDevice(deviceName)
    }


    /**
     * 设置搜索关键字
     * */
    fun setSearchKey(value: String) {
        setState { copy(searchKey = value) }
        searchContacts()
    }

    /**
     * 搜索联系人
     * */
    private fun searchContacts() = withState { state ->
        //如果搜索关键字为空,则清空搜索结果列表
        if (state.searchKey.isNullOrEmpty() || state.contactsList.isNullOrEmpty()) {
            setState { copy(searchContactsList = Uninitialized) }
            return@withState
        }
        suspend {
            //根据关键字匹配名称和号码
            state.contactsList.filter { item ->
                item.name?.contains(state.searchKey) ?: false
                        || item.number?.contains(state.searchKey) ?: false
            }

        }.execute { copy(searchContactsList = it) }
    }

    override fun onConnected(remote: IRemote?, callback: ICallback?) {
        remote?.register(
            arrayOf(
                //注册想要监听是数据，true代表马上返回需要的值
                ApiBt.UPDATE_PHONE_STATE,  //蓝牙状态
                ApiBt.UPDATE_PHONE_NUMBER,  //蓝牙名称和号码
                ApiBt.UPDATE_BOOK,  //电话本
                ApiBt.UPDATE_PBAP_STATE, //电话本下载状态
                ApiBt.UPDATE_PLAY_STATE,//当前播放音乐状态
                ApiBt.UPDATE_ID3_TITLE,//当前播放音乐标题
                ApiBt.UPDATE_ID3_ARTIST,//当前播放音乐作者

                ApiBt.UPDATE_IN_CALL_LOG_LIST,
                ApiBt.UPDATE_MISS_CALL_LOG_LIST,
                ApiBt.UPDATE_OUT_CALL_LOG_LIST,

                ApiBt.UPDATE_CALL_LOG_STATE,


                ApiBt.UPDATE_ALL_CALL_LOG,//全部通话记录
                ApiBt.UPDATE_IN_CALL_LOG,//接听通话记录列表
                ApiBt.UPDATE_OUT_CALL_LOG,//呼出通话记录列表
                ApiBt.UPDATE_MISS_CALL_LOG,//未接通话记录列表

                ApiBt.UPDATE_PIN_CODE,//pinCode
                ApiBt.UPDATE_LOCAL_NAME,//设备名称
                ApiBt.UPDATE_SEARCH_LIST,//搜索列表
                ApiBt.UPDATE_PAIR_LIST,//已配对设备
            ), callback, true
        )
    }

    override fun onUpdate(params: Bundle?) {
        params?.apply {
            Log.i("onUpdate", "\n")
            for (key in keySet()) {
                Log.i("onUpdate", "key:$key,value:${get(key)}")
            }
            Log.i("onUpdate", "\n")

            when (getString("id")) {
                ApiBt.UPDATE_PHONE_STATE -> //蓝牙连接状态
                {
                    val state = params.getInt("value")

                    //断开连接后,清空数据
                    if (state == ApiBt.PHONE_STATE_DISCONNECTED) {
                        setState {
                            copy(
                                btState = state,
                                contactsList = emptyList(),
                                inCallLogList = emptyList(),
                                outCallLogList = emptyList(),
                                missCallLogList = emptyList(),
                                searchContactsList = Uninitialized,
                                contactsLoadTask = Uninitialized
                            )
                        }
                    } else if (state == ApiBt.PHONE_STATE_CONNECTED) {
                        setState { copy(btState = state) }
                        // ApiBt.requestCallLog()//连接成功后,获取通话记录

                        //联系人信息会自动获取,这里暂时屏蔽
                        //fetchContacts()//连接成功后,获取联系人信息

                    } else {
                        setState { copy(btState = state) }
                    }
                }


                ApiBt.UPDATE_BOOK -> {       //电话本信息
                    val name = params.getString("name");
                    val number = params.getString("number")
                    if (!name.isNullOrEmpty() && !number.isNullOrEmpty()) {
                        val contacts = Contacts(name, number)
                        setState { copy(contactsList = contactsList + contacts) }
                    }
                }

                ApiBt.UPDATE_PBAP_STATE -> {     //电话本下载状态
                    //电话本下载状态
                    val state = params.getInt("value")
                    //ApiBt.PBAP_STATE_LOAD 下载中；ApiBt.PBAP_STATE_CONNECTED 下载完成
                    if (state == ApiBt.PBAP_STATE_LOAD) {
                        setState { copy(contactsLoadTask = Loading()) }
                    } else if (state == ApiBt.PBAP_STATE_CONNECTED) {
                        setState { copy(contactsLoadTask = Success(Unit)) }
                        searchContacts()
                    }
                }

                ApiBt.UPDATE_PLAY_STATE -> {    //播放状态
                    val value = params.getInt("value")
                    setState { copy(musicState = value) }
                }

                ApiBt.UPDATE_ID3_TITLE -> {    //音乐标题
                    val value = params.getString("value")
                    setState { copy(musicTitle = value) }
                }

                ApiBt.UPDATE_ID3_ARTIST -> {    //音乐作者
                    val value = params.getString("value")
                    setState { copy(musicArtist = value) }
                }


                ApiBt.UPDATE_PIN_CODE -> {  //pinCode
                    val value = params.getString("value")
                    setState { copy(pinCode = value) }
                }

                ApiBt.UPDATE_LOCAL_NAME -> {    //本地名称
                    val value = params.getString("value")
                    setState { copy(deviceName = value) }
                }

                ApiBt.UPDATE_SEARCH_LIST -> { //搜索列表

                }

                ApiBt.UPDATE_PAIR_LIST -> { //已配对列表

                }

                ApiBt.UPDATE_ALL_CALL_LOG -> {    //全部通话记录
                    val name = params.getString("name")
                    val number = params.getString("number")
                    val time = params.getLong("time")
                    val type = params.getString("type")

                    val callLog = CallLog(type, name, number, time)

                    if (ApiBt.CMD_IN_CALL_LOG.equals(type, true)) {
                        setState { copy(inCallLogList = inCallLogList + callLog) }
                    } else if (ApiBt.CMD_OUT_CALL_LOG.equals(type, true)) {
                        setState { copy(outCallLogList = outCallLogList + callLog) }
                    } else if (ApiBt.CMD_MISS_CALL_LOG.equals(type, true)) {
                        setState { copy(missCallLogList = missCallLogList + callLog) }
                    }

                }
            }
        }
    }

    override fun isUpdateOnUIThread(): Boolean {
        return false
    }
}