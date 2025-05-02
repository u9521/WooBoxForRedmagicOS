package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.alarmclock.RemoveClockWidgetRedone
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object AlarmClock : AppRegister() {
    override val packageName: List<String> = listOf("com.coloros.alarmclock")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"
    override val loadDexkit: Boolean
        get() = false
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)
        autoInitHooks(
            lpparam,
            RemoveClockWidgetRedone, //移除桌面时钟组件红一
        )
    }
}