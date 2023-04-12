package com.example.myapplication.ui.data

import com.example.myapplication.connection.CommandUtil

object TestDataUtil {
    val uiStates: Array<UiState>
        get() = arrayOf(
            // 客厅
            UiState(
                0, CommandUtil.ROOM_LIVING, CommandUtil.MACHINE_DOOR, "大门",
                ControllerType.SWITCH
            ),
            UiState(
                1, CommandUtil.ROOM_LIVING, CommandUtil.MACHINE_CURTAIN, "窗帘",
                ControllerType.SWITCH
            ),
            UiState(
                2, CommandUtil.ROOM_LIVING, CommandUtil.MACHINE_LIGHT, "主灯",
                ControllerType.LIGHT, lightID = CommandUtil.L_ID_MAIN
            ),
            UiState(
                3, CommandUtil.ROOM_LIVING, CommandUtil.MACHINE_LIGHT, "副灯",
                ControllerType.LIGHT, lightID = CommandUtil.L_ID_VICE
            ),
            UiState(
                4, CommandUtil.ROOM_LIVING, CommandUtil.MACHINE_LIGHT, "氛围灯",
                ControllerType.LIGHT, lightID = CommandUtil.L_ID_AMBIENT
            ),
            UiState(
                5, CommandUtil.ROOM_LIVING, CommandUtil.MACHINE_TV, "电视",
                ControllerType.TV
            ),
            // 主卧
            UiState(
                6, CommandUtil.ROOM_MASTER_BEDROOM, CommandUtil.MACHINE_LIGHT, "主灯",
                ControllerType.LIGHT, lightID = CommandUtil.L_ID_MAIN
            ),
            UiState(
                7, CommandUtil.ROOM_MASTER_BEDROOM, CommandUtil.MACHINE_LIGHT, "副灯",
                ControllerType.LIGHT, lightID = CommandUtil.L_ID_VICE
            ),
            UiState(
                8, CommandUtil.ROOM_MASTER_BEDROOM, CommandUtil.MACHINE_LIGHT, "氛围灯",
                ControllerType.LIGHT, lightID = CommandUtil.L_ID_AMBIENT
            ),
            UiState(
                9, CommandUtil.ROOM_MASTER_BEDROOM, CommandUtil.MACHINE_AIR_CONDITIONER, "空调",
                ControllerType.AIR_CONDITIONER
            ),
            // 次卧
            UiState(
                10, CommandUtil.ROOM_SECOND_BEDROOM, CommandUtil.MACHINE_LIGHT, "主灯",
                ControllerType.LIGHT, lightID = CommandUtil.L_ID_MAIN
            ),
            UiState(
                11, CommandUtil.ROOM_SECOND_BEDROOM, CommandUtil.MACHINE_LIGHT, "副灯",
                ControllerType.LIGHT, lightID = CommandUtil.L_ID_VICE
            ),
            // 客卧
            UiState(
                12, CommandUtil.ROOM_GUEST_BEDROOM, CommandUtil.MACHINE_LIGHT, "灯",
                ControllerType.LIGHT, lightID = CommandUtil.L_ID_ALL
            ),
            // 主卫生间
            UiState(
                13, CommandUtil.ROOM_MAIN_BATHROOM, CommandUtil.MACHINE_LIGHT, "灯",
                ControllerType.LIGHT, lightID = CommandUtil.L_ID_ALL
            ),
            // 主卧卫生间
            UiState(
                14, CommandUtil.ROOM_MASTER_BATHROOM, CommandUtil.MACHINE_LIGHT, "灯",
                ControllerType.LIGHT, lightID = CommandUtil.L_ID_ALL
            ),
            // 厨房
            UiState(
                15, CommandUtil.ROOM_KITCHEN, CommandUtil.MACHINE_LIGHT, "灯",
                ControllerType.SWITCH, lightID = CommandUtil.L_ID_ALL
            ),
            UiState(
                16, CommandUtil.ROOM_KITCHEN, CommandUtil.MACHINE_FAN, "排气扇",
                ControllerType.SWITCH
            ),
            // 总控
            UiState(17, CommandUtil.ROOM_ALL, CommandUtil.MACHINE_CONTROL_ALL, "灯",
                ControllerType.SWITCH, roomName = "总控"
            ),
            UiState(
                18, CommandUtil.ROOM_LIVING, CommandUtil.MACHINE_LIGHT, "客厅灯",
                ControllerType.SWITCH, roomName = "总控",lightID = CommandUtil.L_ID_ALL
            ),
            UiState(
                19, CommandUtil.ROOM_MASTER_BEDROOM, CommandUtil.MACHINE_LIGHT, "主卧灯",
                ControllerType.SWITCH, roomName = "总控", lightID = CommandUtil.L_ID_ALL
            ),
            UiState(
                20, CommandUtil.ROOM_SECOND_BEDROOM, CommandUtil.MACHINE_LIGHT, "次卧灯",
                ControllerType.SWITCH, roomName = "总控", lightID = CommandUtil.L_ID_ALL
            ),
        )
}