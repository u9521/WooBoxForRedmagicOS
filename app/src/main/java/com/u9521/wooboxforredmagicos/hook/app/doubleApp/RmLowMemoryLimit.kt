package com.u9521.wooboxforredmagicos.hook.app.doubleApp

import android.annotation.SuppressLint
import android.content.Context
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object RmLowMemoryLimit : HookRegister() {

    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("rm_double_low_memory_limit") {
        val showLimited = getDefaultCL().loadClass("com.zte.cn.doubleapp.common.Utils")
            .getDeclaredMethod("showLimitedApps", Context::class.java)
        val showLimitedHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = false
            }
        }
        XposedBridge.hookMethod(showLimited, showLimitedHooker)
    }
}