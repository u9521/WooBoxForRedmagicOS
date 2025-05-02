package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.launcher.UnlockSelfStartQuantity
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object SecurityCenter : AppRegister() {
    override val packageName: List<String> = listOf("com.oplus.safecenter")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"
    override val loadDexkit: Boolean = false
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)
        autoInitHooks(
            lpparam,
            UnlockSelfStartQuantity, //解除自启动上限
        )
    }
}