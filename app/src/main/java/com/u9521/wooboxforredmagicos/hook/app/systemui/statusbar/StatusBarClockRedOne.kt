package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar


import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XposedHelpers

object StatusBarClockRedOne : HookRegister() {
    override fun init() = hasEnable("remove_dropdown_status_bar_clock_redone"){
        return@hasEnable
//        findMethod("com.oplusos.systemui.ext.BaseClockExt"){
//            name == "setTextWithRedOneStyle" && paramCount == 2
//        }.hookBefore {
//            XposedHelpers.setBooleanField(it.thisObject,"mIsDateTimePanel",false)
//        }
    }
}