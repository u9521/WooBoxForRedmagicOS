package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar


import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object StatusBarClock : HookRegister() {
    override fun init() = hasEnable("dropdown_status_bar_clock_display_seconds"){
        return@hasEnable
//        findMethod("com.android.systemui.statusbar.policy.Clock"){
//            name == "setShowSecondsAndUpdate" && parameterTypes[0] == Boolean::class.java
//        }.hookBefore {
//            it.args[0] = true
//        }
    }
}