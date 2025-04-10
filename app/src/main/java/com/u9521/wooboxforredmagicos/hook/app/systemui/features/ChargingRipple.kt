package com.u9521.wooboxforredmagicos.hook.app.systemui.features

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object ChargingRipple : HookRegister() {
    override fun init() = hasEnable("enable_charging_ripple") {
        MethodFinder.fromClass("com.android.systemui.statusbar.FeatureFlags")
            .filterByName("isChargingRippleEnabled")
            .first().createBeforeHook { it.result = true }
    }
}