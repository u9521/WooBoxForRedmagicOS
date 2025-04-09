package com.u9521.wooboxforredmagicos.hook.app.systemui.notification

import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RemoveDevModeIsOn : HookRegister() {
    override fun init() = hasEnable("remove_dev_mode_is_on") {
        return@hasEnable
//        findMethod("com.oplusos.systemui.statusbar.policy.SystemPromptController") {
//            name == "updateDeveloperMode"
//        }.hookReturnConstant(null)
    }
}