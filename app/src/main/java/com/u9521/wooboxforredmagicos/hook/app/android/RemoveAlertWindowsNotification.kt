package com.u9521.wooboxforredmagicos.hook.app.android

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RemoveAlertWindowsNotification : HookRegister() {
    override fun init() {
        hasEnable("remove_alert_windows_notification") {
            MethodFinder.fromClass("com.android.server.wm.AlertWindowNotification")
                .filterByName("onPostNotification").first().createBeforeHook{
                it.result = null
                }
        }
    }
}