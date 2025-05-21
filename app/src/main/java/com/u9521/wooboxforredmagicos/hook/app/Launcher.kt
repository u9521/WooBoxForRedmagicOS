package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.launcher.ForceSupportResizeActivity
import com.u9521.wooboxforredmagicos.hook.app.launcher.RMzvoiceUninstalDialog
import com.u9521.wooboxforredmagicos.hook.app.launcher.UnhideBlackListApps
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Launcher : AppRegister() {
    override val packageName: List<String> =
        listOf("com.zte.mifavor.launcher", "com.zte.mifavor.launcher.resource")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox-Launcher"
    override val loadDexkit: Boolean = true

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        autoInitHooks(
            lpparam,
            RMzvoiceUninstalDialog,//移除智慧语音已卸载弹窗
            ForceSupportResizeActivity,//强制活动可调大小，在最近任务显示小窗
            UnhideBlackListApps,//取消隐藏原生的文件，DTS音效等APP
        )
    }
}