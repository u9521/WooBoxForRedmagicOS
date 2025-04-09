package com.u9521.wooboxforredmagicos.hook.app.systemui.notification

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RemoveFinishedCharging : HookRegister() {
    override fun init() = hasEnable("remove_finished_charging") {
        findMethod("com.oplusos.systemui.notification.power.OplusPowerNotificationWarnings") {
            name == "showChargeErrorDialog" && parameterTypes[0] == Int::class.java
        }.hookBefore {
            if (it.args[0] as Int == 7) it.result = null
        }
    }
}