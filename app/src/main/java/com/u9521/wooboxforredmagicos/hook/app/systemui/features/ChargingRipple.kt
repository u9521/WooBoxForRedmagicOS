package com.u9521.wooboxforredmagicos.hook.app.systemui.features

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object ChargingRipple : HookRegister() {
    override fun init() = hasEnable("enable_charging_ripple") {
        findMethod("com.android.systemui.statusbar.FeatureFlags") {
            name == "isChargingRippleEnabled"
        }.hookReturnConstant(true)
    }
}