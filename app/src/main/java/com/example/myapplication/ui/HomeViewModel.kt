package com.example.myapplication.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MainActivity
import com.example.myapplication.SmartHomeApp
import com.example.myapplication.connection.CommandUtil
import com.example.myapplication.connection.TCP_Init
import com.example.myapplication.ui.data.ControllerType
import com.example.myapplication.ui.data.TestDataUtil
import com.example.myapplication.ui.data.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.PrintStream
import java.net.Socket
import java.util.*
import java.util.stream.Collectors

class HomeViewModel : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
        private const val IP = "106.15.1.39"
    }


    private val uiStates = MutableLiveData<MutableList<UiState>?>(mutableListOf())
    private val tcpInit: TCP_Init = TCP_Init(IP, 8085)
    private var client: Socket? = null

    init {
        uiStates.value?.addAll(listOf(*TestDataUtil.uiStates)) // 初始化测试数据
    }


    fun getUiStates(): List<UiState>? {
        return uiStates.value
    }

    fun findUiState(id: Int): UiState? = uiStates.value?.find { it.id == id }

    /**
     * 依据控制器类型筛选UiStates
     * @param type 控制器类型
     * @return 筛选后的UiStates
     */
    fun getFilteredUiStates(type: ControllerType): List<UiState> {
        val uiStateStream = Objects.requireNonNull<List<UiState>?>(uiStates.value)
            .stream().filter { uiState -> uiState.controllerType == type }
        return uiStateStream.collect(Collectors.toList())
    }

    /**
     * 依据房间名筛选UiStates
     * @param roomName 房间名
     * @return 筛选后的UiStates
     */
    fun getFilteredUiStates(roomName: String): List<UiState> {
        val uiStateStream = Objects.requireNonNull<List<UiState>?>(uiStates.value)
            .stream().filter { uiState ->
                val uiRoomName = uiState.roomName
                if (uiRoomName != null) {
                    uiRoomName == roomName
                } else {
                    false
                }
            }
        return uiStateStream.collect(Collectors.toList())
    }

    /**
     * 获取所有UiState的房间名，经过去重
     * @return
     */
    val roomNames: List<String?>
        get() {
            val roomNames: MutableSet<String?> = HashSet()
            if (uiStates.value == null) {
                return ArrayList(roomNames)
            }
            roomNames.addAll(
                uiStates.value!!
                    .stream()
                    .map { uiState: UiState -> uiState.roomName }
                    .collect(Collectors.toList()))

            return roomNames.toList()
        }


    fun turnUpTemperature(uiStateID: Int) {
        uiStates.value?.let {
            findUiState(uiStateID)?.let {
                turnUpTemperature(it)
            }
        }
    }

    fun turnUpTemperature(uiState: UiState) {
        uiState.turnUpTemperature()
        sendData(uiState.toCommand())
    }

    fun turnDownTemperature(uiStateID: Int) {
        uiStates.value?.let {
            findUiState(uiStateID)?.let {
                turnDownTemperature(it)
            }
        }
    }

    fun turnDownTemperature(uiState: UiState) {
        uiState.turnDownTemperature()
        sendData(uiState.toCommand())
    }

    fun processRateResult(uiState: UiState) {
        // uiState的更改已经通过dataBinding实现，这里只需要发送数据
        sendData(uiState.toCommand())
    }

    fun switchActivated(uiState: UiState) {
        uiState.switchActivated()
        sendData(uiState.toCommand())
        Toast.makeText(SmartHomeApp.context, uiState.toCommand(), Toast.LENGTH_SHORT).show()
    }

    /**
     * 主界面的小switcher的点击处理
     */
    fun processSwitch(uiState: UiState) {
        when (uiState.machineName) {
            CommandUtil.MACHINE_CONTROL_ALL -> {
                uiStates.value?.forEach {
                    if (it.machineName == CommandUtil.MACHINE_LIGHT) {
                        it.activated.value = uiState.activated.value ?: false
                    }
                }
            }
            CommandUtil.MACHINE_LIGHT -> {
                if (uiState.lightID == CommandUtil.L_ID_ALL) {
                    //找到同一个房间所有的灯并操作
                    uiStates.value?.forEach {
                        if (it.room == uiState.room && it.machineName == uiState.machineName) {
                            it.activated.value = uiState.activated.value ?: false
                        }
                    }
                }
            }
            else -> {}
        }
        sendData(uiState.toCommand())
    }

    fun processTVCommand(uiState: UiState, position: Int) {
        val cmd: String = when (position) {
            0 -> uiState.toCommand(Tv_mode = CommandUtil.TV_CMD_TURN_UP_VOLUME)
            1 -> uiState.toCommand(Tv_mode = CommandUtil.TV_CMD_TURN_UP_CHANNEL)
            2 -> uiState.toCommand(Tv_mode = CommandUtil.TV_CMD_TURN_DOWN_VOLUME)
            3 -> uiState.toCommand(Tv_mode = CommandUtil.TV_CMD_TURN_DOWN_CHANNEL)
            else -> uiState.toCommand()
        }
        sendData(cmd)
    }

    /**
     * 改变空调模式
     */
    fun changeFAAMode(uiState: UiState) {
        uiState.changeFAAMode()
        sendData(uiState.toCommand())
    }

    /**
     * 改变空调风速
     */
    fun changeWindSpeed(uiState: UiState) {
        uiState.changeWindSpeed()
        sendData(uiState.toCommand())
    }


    private fun sendData(command: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "sendData: $client")
            if (client == null) {
                try {
                    client = tcpInit.connect
                    Log.d(TAG, "sendData: $client")
                } catch (e: IOException) {
                    Log.d(TAG, "sendData: get connect error")
                    viewModelScope.launch(Dispatchers.Main) {
                        Toast.makeText(SmartHomeApp.context, "连接失败，请检查网络状况", Toast.LENGTH_SHORT)
                            .show()
                    }
                    return@launch
                }
            }
            client?.let {
                if (!it.isConnected) {
                    try {
                        client = tcpInit.connect
                    } catch (e: IOException) {
                        Log.d(TAG, "sendData: get connect error")
                        viewModelScope.launch(Dispatchers.Main) {
                            Toast.makeText(SmartHomeApp.context, "连接失败，请检查网络状况", Toast.LENGTH_SHORT)
                                .show()
                        }
                        return@launch
                    }
                }
            }
            client?.let {
                if (it.isConnected) {
                    val out = PrintStream(it.getOutputStream())
                    out.print(command)
                    out.flush()
                    Log.d(TAG, "sendData: 成功发送")
                } else {
                    Log.d(TAG, "sendData: 未连接")
                }
            }
            client ?: { Log.d(TAG, "sendData: 无socket")}
        }
    }
}