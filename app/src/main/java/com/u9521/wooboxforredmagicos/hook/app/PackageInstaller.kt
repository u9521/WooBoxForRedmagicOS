package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.packageinstaller.HidePurifySwitch
import com.u9521.wooboxforredmagicos.hook.app.packageinstaller.SkipApkScan
import com.u9521.wooboxforredmagicos.hook.app.packageinstaller.HideStoreHint
import com.u9521.wooboxforredmagicos.hook.app.packageinstaller.UseCtsActivity
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PackageInstaller : AppRegister() {
    override val packageName: List<String> = listOf("com.android.packageinstaller")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)
        autoInitHooks(
            lpparam,
            SkipApkScan,//跳过Apk扫描
            HidePurifySwitch,//隐藏净化开关和提示
            HideStoreHint,//隐藏商店安装按钮
            UseCtsActivity//使用cts测试时的安装界面
        )
    }
}