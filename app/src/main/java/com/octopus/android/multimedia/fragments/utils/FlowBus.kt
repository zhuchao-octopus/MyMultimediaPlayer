package com.octopus.android.multimedia.fragments.utils

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.collections.mutableMapOf
import kotlin.collections.set

/**
 * 基于 kotlin flow 实现的消息总线
 */
object FlowBus {
    private const val TAG = "FlowBus"
    private val busMap = mutableMapOf<String, EventBus<*>>()
    private val busStickMap = mutableMapOf<String, StickEventBus<*>>()

    @Synchronized
    fun <T> with(key: String): EventBus<T> {
        var eventBus = busMap[key]
        if (eventBus == null) {
            eventBus = EventBus<T>(key)
            busMap[key] = eventBus
        }
        return eventBus as EventBus<T>
    }

    @Synchronized
    fun <T> withStick(key: String): StickEventBus<T> {
        var eventBus = busStickMap[key]
        if (eventBus == null) {
            eventBus = StickEventBus<T>(key)
            busStickMap[key] = eventBus
        }
        return eventBus as StickEventBus<T>
    }

    //真正实现类
    open class EventBus<T>(private val key: String) : DefaultLifecycleObserver {

        //私有对象用于发送消息
        private val _events: MutableSharedFlow<T> by lazy {
            obtainEvent()
        }

        //暴露的公有对象用于接收消息
        private val events = _events.asSharedFlow()

        open fun obtainEvent(): MutableSharedFlow<T> =
            MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)

        //主线程接收数据
        fun register(lifecycleOwner: LifecycleOwner, action: (t: T) -> Unit) {
            lifecycleOwner.lifecycle.addObserver(this)
            lifecycleOwner.lifecycleScope.launch {
                events.collect {
                    try {
                        action(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e(TAG, "FlowBus - Error:$e")
                    }
                }
            }
        }

        //协程中发送数据
        suspend fun post(event: T) {
            _events.emit(event)
        }

        //主线程发送数据
        fun post(scope: CoroutineScope, event: T) {
            scope.launch {
                _events.emit(event)
            }
        }

        //自动销毁
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)

            Log.w(TAG, "FlowBus - 自动onDestroy")
            val subscriptCount = _events.subscriptionCount.value
            if (subscriptCount <= 0)
                busMap.remove(key)
        }
    }

    class StickEventBus<T>(key: String) : EventBus<T>(key) {
        override fun obtainEvent(): MutableSharedFlow<T> =
            MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
    }

}
