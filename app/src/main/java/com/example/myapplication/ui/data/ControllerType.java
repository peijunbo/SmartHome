package com.example.myapplication.ui.data;

public enum ControllerType {
    /**
     * Controller that only has two status, on and off.
     */
    SWITCH("开关"),
    /**
     * Controller that can change the brightness of a light.
     */
    LIGHT("灯"),
    /**
     * Controller that can change the mode, temperature and wind-speed of air conditioner.
     */
    AIR_CONDITIONER("空调"),
    /**
     * Controller that can change volume and channels.
     */
    TV("电视");
    public final String nameInCh;
    ControllerType() {
        this("");
    }
    ControllerType(String nameInCh) {
        this.nameInCh = nameInCh;
    }
}
