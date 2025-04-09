package com.u9521.wooboxforredmagicos.hook.app.alarmclock

import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.github.kyuubiran.ezxhelper.utils.putStaticObject
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RemoveClockWidgetRedone : HookRegister(){
    override fun init() = hasEnable("remove_clock_widget_redone"){
        val list: String =
            when (XSPUtils.getString("AlarmClockCommit", "null")) {
                "7ce00ef" -> "Sb"
                "c3d4fc6" -> "Sc"
                else -> "Sb"
            }
        loadClass("com.coloros.widget.smallweather.OnePlusWidget").putStaticObject(list, "")
    }
}