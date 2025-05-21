package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object RemoveAlertWindowsNotification : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        hasEnable("remove_alert_windows_notification") {
            val onPN = getDefaultCL().loadClass("com.android.server.wm.AlertWindowNotification")
                .getDeclaredMethod("onPostNotification")
            val onPNHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    param!!.result = null
                }
            }
            XposedBridge.hookMethod(onPN, onPNHooker)
        }
    }
}