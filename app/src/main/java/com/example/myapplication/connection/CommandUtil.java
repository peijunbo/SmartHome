package com.example.myapplication.connection;

import androidx.annotation.NonNull;

public class CommandUtil {
    public static final String MACHINE_FULL_OPEN = "full_open";
    public static final String MACHINE_FULL_CLOSE = "full_close";
    public static final String MACHINE_LIGHT = "灯";
    public static final String MACHINE_CURTAIN = "窗帘";
    public static final String MACHINE_DOOR = "门";
    public static final String MACHINE_FAN = "风扇";
    public static final String MACHINE_AIR_CONDITIONER = "空调";
    public static final String MACHINE_TV = "电视";
    public static final String MACHINE_CONTROL_ALL = "全部";
    public static final String LIGHT_NAME_MAIN = "主灯";
    public static final String LIGHT_NAME_VICE = "副灯";
    public static final String LIGHT_NAME_AMBIENT = "氛围灯";
    public static final int L_ID_ALL = 0;
    public static final int L_ID_MAIN = 1;
    public static final int L_ID_VICE = 2;
    public static final int L_ID_AMBIENT = 3;

    /**
     * 空调制冷模式
     */
    public static final int FAA_MODE_COOLING = 0;
    /**
     * 空调通风模式
     */
    public static final int FAA_MODE_VENTILATION = 1;
    /**
     * 空调制热模式
     */
    public static final int FAA_MODE_HEATING = 2;
    /**
     * 默认模式，对应空调制冷
     */
    public static final int FAA_MODE_DEFAULT = FAA_MODE_COOLING;

    public static final int WIND_SPEED_SLOW = 0;
    public static final int WIND_SPEED_MID = 1;
    public static final int WIND_SPEED_FAST = 2;
    public static final int WIND_SPEED_DEFAULT = WIND_SPEED_MID;
    public static final String TV_CMD_TURN_UP_CHANNEL = "01";
    public static final String TV_CMD_TURN_DOWN_CHANNEL = "00";
    public static final String TV_CMD_TURN_UP_VOLUME = "11";
    public static final String TV_CMD_TURN_DOWN_VOLUME = "10";

    public static final String ROOM_LIVING = "客厅";
    public static final String ROOM_MASTER_BEDROOM = "主卧";
    public static final String ROOM_SECOND_BEDROOM = "次卧";
    public static final String ROOM_GUEST_BEDROOM = "客卧";
    public static final String ROOM_MAIN_BATHROOM = "主卫生间";
    public static final String ROOM_MASTER_BATHROOM = "主卧卫生间";
    public static final String ROOM_KITCHEN = "厨房";
    public static final String ROOM_ALL = "全部";
    /**
     * 无效L_id,表示不是灯
     */
    public static final int INVALID_LIGHT_ID = -1;
    /**
     * 无效FFA_mode
     */
    public static final int INVALID_FAA_MODE = -1;
    /**
     * 无效电视模式
     */
    public static final String INVALID_TV_MODE = "xx";
    /**
     * 无效比率
     */
    public static final String INVALID_RATE = "xx";
    /**
     * 无效风速
     */
    public static final int INVALID_WIND_SPEED = -1;

    /**
     * @param machine   设备名
     * @param room      房间名
     * @param L_id      灯的id（0,1,2）
     * @param active    是否开关（true,false）
     * @param rate      灯的亮度（0~100），空调温度（16~30），
     * @param FAA_mode  风扇或空调模式（0,1,2）
     * @param windSpeed 风速(0,1,2)
     * @param Tv_mode   电视模式（00,01,10,11）
     * @return 七位的标志位
     */
    public static String buildCommand(
            @NonNull String machine,
            @NonNull String room,
            int L_id,
            boolean active,
            @NonNull String rate,
            int FAA_mode,
            int windSpeed,
            @NonNull String Tv_mode
    ) {
        String flag = "";
        // 根据设备，设置命令的前两位（控制全开关，以及设备）
        switch (machine) {
            case MACHINE_FULL_OPEN:
                flag = "2xxxxxx";
                return flag;
            case MACHINE_FULL_CLOSE:
                flag = "3xxxxxx";
                return flag;
            case MACHINE_LIGHT:
                flag += "1L";
                break;
            case MACHINE_CURTAIN:
                flag += "0C";
                break;
            case MACHINE_DOOR:
                flag += "0D";
                break;
            case MACHINE_FAN:
                flag += "0F";
                break;
            case MACHINE_AIR_CONDITIONER:
                flag += "0A";
                break;
            case MACHINE_TV:
                flag += "0G";
                break;
            default:
                flag = "error1";
                return flag;    // 当所有参数都不符合，则将参数置为错误
        }

        // 根据房间,以及是否是空调，设置第三位
        if (machine.equals(MACHINE_AIR_CONDITIONER))
        // 如果是空调则第三位为设置空调模式
        {
            switch (FAA_mode)    // 根据空调和风扇的标志位
            {
                case 0:
                    flag += "a";
                    break;    // 当设置空调模式为为制冷，传入a
                case 1:
                    flag += "c";
                    break;    // 当设置空调模式为为通风，传入c
                case 2:
                    flag += "b";
                    break;    // 当设置空调模式为为制热，传入b
                default:
                    flag = "error2";
                    return flag;    // 当所有参数都不符合，则将参数置为错误
            }
        } else {
            switch (room) {    // 根据房间设置标志
                case ROOM_LIVING:
                    flag += "1";
                    break;
                case ROOM_MASTER_BEDROOM:
                    flag += "2";
                    break;
                case ROOM_SECOND_BEDROOM:
                    flag += "3";
                    break;
                case ROOM_GUEST_BEDROOM:
                    flag += "4";
                    break;
                case ROOM_MAIN_BATHROOM:
                    flag += "5";
                    break;
                case ROOM_MASTER_BATHROOM:
                    flag += "6";
                    break;
                case ROOM_KITCHEN:
                    flag += "7";
                    break;
                default:
                    flag = "error3";
                    return flag;    // 当所有参数都不符合，则将参数置为错误
            }
        }

        // 根据是否是风扇，以及是否具有多个灯是有设置第四位
        if (machine.equals(MACHINE_AIR_CONDITIONER))
        // 如果是风扇则第四位为设置风扇模式
        {
            switch (windSpeed)    // 根据空调和风扇的标志位
            {
                case 0:
                    flag += "a";
                    break;    // 当设置风扇模式为为低风，传入a
                case 1:
                    flag += "b";
                    break;    // 当设置风扇模式为为中风，传入b
                case 2:
                    flag += "c";
                    break;    // 当设置风扇模式为为高热，传入c
                default:
                    flag = "error4";
                    return flag;    // 当所有参数都不符合，则将参数置为错误
            }
        } else {
            switch (L_id) {
                case -1:
                    flag += "x";
                    break;    // 说明既不是风扇也不是灯
                case 0:
                    flag += "0";
                    break;
                case 1:
                    flag += "1";
                    break;
                case 2:
                    flag += "2";
                    break;
                case 3:
                    flag += "3";
                    break;
                default:
                    flag = "error5";
                    return flag;
            }
        }

        // 根据是否开关，设置第五位标志
        if (active)    // 开true为1
            flag += "1";
        else    // 关false为0
            flag += "0";

        // 根据灯或者温度是否存在，Tv_mode是否存在，设置第六位标志
        switch (machine) {
            case MACHINE_TV:
                flag += Tv_mode;
                break;
            case MACHINE_LIGHT:
            case MACHINE_AIR_CONDITIONER:
                flag += rate;
                break;
            default:
                flag += "xx";
                break;
        }
        return flag;
    }
}