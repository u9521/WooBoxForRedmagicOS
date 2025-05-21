package com.u9521.wooboxforredmagicos.hook.app.launcher

import android.annotation.SuppressLint
import android.content.Context
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object ForceSupportResizeActivity : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("launcher_force_support_resize_activity") {
        val taskclazz = getDefaultCL().loadClass("com.android.systemui.shared.recents.model.Task")
        val cASRMe =
            getDefaultCL().loadClass("com.android.quickstep.util.MiniWindowSplitScreenUtils")
                .getDeclaredMethod("checkActivitySupportResize", Context::class.java, taskclazz)
        val aSRHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = true
            }
        }
        XposedBridge.hookMethod(cASRMe, aSRHooker)
    }
}