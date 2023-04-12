package com.example.myapplication.ui.data

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.connection.CommandUtil


class UiState @JvmOverloads constructor(
    val id: Int,
    val room: String,
    val machineName: String,
    controllerName: String,
    val controllerType: ControllerType = ControllerType.TV,
    val roomName: String = room,
    temperature: Int = UI_DEFAULT_TEMPERATURE,
    rate: Int = UI_DEFAULT_RATE,
    var lightID: Int = CommandUtil.INVALID_LIGHT_ID,
    faaMode: Int = CommandUtil.FAA_MODE_DEFAULT,
    windSpeed: Int = CommandUtil.WIND_SPEED_DEFAULT
) {
    /**
     * 灯亮度百分比0~100
     */
    val rate = MutableLiveData(0)

    /**
     * 空调温度16~30
     */
    val temperature = MutableLiveData(0)

    /**
     * 控制器名称
     */
    val controllerName = MutableLiveData("")

    val faaMode = MutableLiveData(CommandUtil.FAA_MODE_DEFAULT)
    val windSpeed = MutableLiveData(CommandUtil.WIND_SPEED_DEFAULT)
    val activated = MutableLiveData(false)


    init {
        this.controllerName.value = controllerName
        this.temperature.value = temperature
        this.rate.value = rate
        this.faaMode.value = faaMode
        this.windSpeed.value = windSpeed
    }

    fun setControllerName(controllerName: String) {
        if (controllerName != this.controllerName.value) {
            this.controllerName.value = controllerName
        }
    }

    fun setActivated(activated: Boolean) {
        if (activated != (java.lang.Boolean.TRUE == this.activated.value)) {
            this.activated.value = activated
        }
    }

    fun setRate(rate: Int) {
        if (rate > UI_MAX_RATE) {
            this.rate.setValue(UI_MAX_RATE)
        } else this.rate.setValue(Math.max(rate, UI_MIN_RATE))
    }

    fun setTemperature(temperature: Int) {
        if (temperature > MAX_TEMPERATURE) {
            this.temperature.setValue(temperature)
        } else this.temperature.setValue(Math.max(temperature, MIN_TEMPERATURE))
    }

    fun turnUpTemperature() {
        var curTemperature = UI_DEFAULT_TEMPERATURE
        if (temperature.value != null) {
            curTemperature = temperature.value!!
        }
        setTemperature(curTemperature + 1)
    }

    fun turnDownTemperature() {
        var curTemperature = UI_DEFAULT_TEMPERATURE
        if (temperature.value != null) {
            curTemperature = temperature.value!!
        }
        setTemperature(curTemperature - 1)
    }

    fun switchActivated() {
        activated.value = activated.value != true
    }

    fun changeFAAMode() {
        faaMode.value = when (faaMode.value) {
            CommandUtil.FAA_MODE_COOLING -> CommandUtil.FAA_MODE_HEATING
            CommandUtil.FAA_MODE_HEATING -> CommandUtil.FAA_MODE_VENTILATION
            CommandUtil.FAA_MODE_VENTILATION -> CommandUtil.FAA_MODE_COOLING
            else -> CommandUtil.FAA_MODE_COOLING
        }
    }

    fun changeWindSpeed() {
        windSpeed.value = when (windSpeed.value) {
            CommandUtil.WIND_SPEED_SLOW -> CommandUtil.WIND_SPEED_MID
            CommandUtil.WIND_SPEED_MID -> CommandUtil.WIND_SPEED_FAST
            CommandUtil.WIND_SPEED_FAST -> CommandUtil.WIND_SPEED_SLOW
            else -> CommandUtil.WIND_SPEED_SLOW
        }
    }


    fun toCommand(
        active: Boolean = activated.value ?: false,
        Tv_mode: String = CommandUtil.INVALID_TV_MODE
    ): String {
        val rateString: String = when (machineName) {
            CommandUtil.MACHINE_AIR_CONDITIONER -> String.format(
                "%02d",
                temperature.value ?: UI_DEFAULT_TEMPERATURE
            )
            CommandUtil.MACHINE_LIGHT -> String.format("%02d", rate.value ?: UI_DEFAULT_RATE)
            else -> CommandUtil.INVALID_RATE
        }
        // 对机器名进行特殊处理
        var cmdMachine = machineName
        if (machineName == CommandUtil.MACHINE_CONTROL_ALL) {
            cmdMachine =
                if (active) CommandUtil.MACHINE_FULL_OPEN else CommandUtil.MACHINE_FULL_CLOSE
        }
        return CommandUtil.buildCommand(
            cmdMachine,
            room,
            lightID,
            active,
            rateString,
            faaMode.value ?: CommandUtil.INVALID_FAA_MODE,
            windSpeed.value ?: CommandUtil.INVALID_WIND_SPEED,
            Tv_mode
        )
    }

    override fun toString(): String {
        return "UiState(id=$id, room='$room', machineName='$machineName', controllerType=$controllerType, roomName='$roomName', lightID=$lightID, rate=$rate, temperature=$temperature, controllerName=$controllerName, faaMode=$faaMode, windSpeed=$windSpeed, activated=$activated)"
    }

    companion object {
        const val UI_MIN_RATE = 0
        const val UI_MAX_RATE = 99
        const val UI_DEFAULT_RATE = 80
        const val MAX_TEMPERATURE = 30
        const val MIN_TEMPERATURE = 16
        const val UI_DEFAULT_TEMPERATURE = 26

        @JvmStatic
        fun modeStringOf(mode: Int) = when (mode) {
            CommandUtil.FAA_MODE_COOLING -> "制冷"
            CommandUtil.FAA_MODE_HEATING -> "制热"
            CommandUtil.FAA_MODE_VENTILATION -> "通风"
            else -> "制冷"
        }

        @JvmStatic
        fun windSpeedStringOf(windSpeed: Int) = when (windSpeed) {
            CommandUtil.WIND_SPEED_SLOW -> "风速:低"
            CommandUtil.WIND_SPEED_MID -> "风速:中"
            CommandUtil.WIND_SPEED_FAST -> "风速:高"
            else -> "风速:低"
        }
    }
}