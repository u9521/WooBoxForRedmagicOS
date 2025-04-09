package com.u9521.wooboxforredmagicos.hook.app.systemui.lockscreen

import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RemoveRedOne : HookRegister() {
    override fun init() = hasEnable("remove_red_one") {
        return@hasEnable
//        loadClass("com.oplusos.systemui.keyguard.clock.RedTextClock").putStaticObject("NUMBER_ONE", "")
    }
}