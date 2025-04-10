package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object HideVpnIcon : HookRegister() {
    override fun init() = hasEnable("hide_vpn_icon") {
        MethodFinder.fromClass("com.android.systemui.statusbar.policy.SecurityControllerImpl")
            .filterByName("isVpnEnabled").filterEmptyParam().first().createBeforeHook {
                it.result = false
            }
    }
}