package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.launcher.*
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Launcher : AppRegister() {
    override val packageName: List<String> = listOf("com.zte.mifavor.launcher")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)
        autoInitHooks(
            lpparam,
            RMzvoiceUninstalDialog,//移除智慧语音已卸载弹窗
            ForceSupportResizeActivity,//强制活动可调大小，在最近任务显示小窗
        )
    }
}