package com.u9521.wooboxforredmagicos.hook.app.android

import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Deoptimizer
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RmIntentHijack : HookRegister() {
    override fun init() = hasEnable("remove_intent_hijack") {
        MethodFinder.fromClass("com.android.server.pm.ResolveIntentHelper")
            .filterByName("isCtsTesting").first().createBeforeHook { it.result = true }
        Deoptimizer.deoptimizeMethod(
            ClassUtils.loadClass("com.android.server.pm.ResolveIntentHelper"), "chooseBestActivity"
        )
    }
}