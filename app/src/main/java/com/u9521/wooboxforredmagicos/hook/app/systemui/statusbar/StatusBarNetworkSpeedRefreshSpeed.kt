package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar


import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object StatusBarNetworkSpeedRefreshSpeed : HookRegister() {
    override fun init() = hasEnable("status_bar_network_speed_refresh_speed") {
        return@hasEnable
//        findMethod("com.oplusos.systemui.statusbar.controller.NetworkSpeedController") {
//            name == "postUpdateNetworkSpeedDelay" && parameterTypes[0] == Long::class.java
//        }.hookBefore {
//            it.args[0] = 1000L
//        }
    }
}