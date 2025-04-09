package com.u9521.wooboxforredmagicos.hook.app.alarmclock

import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.ClassUtils.setStaticObject
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RemoveClockWidgetRedone : HookRegister(){
    override fun init() = hasEnable("remove_clock_widget_redone"){
        return@hasEnable
        val list: String =
            when (XSPUtils.getString("AlarmClockCommit", "null")) {
                "7ce00ef" -> "Sb"
                "c3d4fc6" -> "Sc"
                else -> "Sb"
            }
        val clazz =loadClass("com.coloros.widget.smallweather.OnePlusWidget")
        setStaticObject(clazz,list, "")
    }
}