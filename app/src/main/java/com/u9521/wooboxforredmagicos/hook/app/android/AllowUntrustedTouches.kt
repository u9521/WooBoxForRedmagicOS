package com.u9521.wooboxforredmagicos.hook.app.android

import android.content.Context
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object AllowUntrustedTouches : HookRegister() {
    override fun init() = hasEnable("allow_untrusted_touches") {
        val hooks = MethodFinder.fromClass("android.hardware.input.InputManager")
            .filterByName("getBlockUntrustedTouchesMode")
            .filterByParamTypes { paramTypes -> paramTypes[0] == Context::class.java }
            .first().createBeforeHook(block = { it.result = 0 })
    }
}