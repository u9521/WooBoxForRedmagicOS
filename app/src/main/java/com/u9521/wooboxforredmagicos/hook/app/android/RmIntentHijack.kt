package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Deoptimizer
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object RmIntentHijack : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("remove_intent_hijack") {
        val resolveIHClazz = getDefaultCL().loadClass("com.android.server.pm.ResolveIntentHelper")
        val isCTSMe = resolveIHClazz.getDeclaredMethod("isCtsTesting")
        val isCTSHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                Log.i("ResolveIntentHelper: mock is cts")
                param!!.result = true
            }
        }
        XposedBridge.hookMethod(isCTSMe, isCTSHooker)

        Deoptimizer.deoptimizeMethod(resolveIHClazz, "chooseBestActivity")
    }
}