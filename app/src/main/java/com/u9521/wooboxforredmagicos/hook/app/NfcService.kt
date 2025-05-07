package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.nfcService.ConfirmLaunchAPP
import com.u9521.wooboxforredmagicos.hook.app.nfcService.MuteNfcSound
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object NfcService : AppRegister() {
    override val packageName: List<String> = listOf("com.android.nfc")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"
    override val loadDexkit: Boolean = false
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)
        autoInitHooks(
            lpparam,
            MuteNfcSound, //移除NFC提示音
            ConfirmLaunchAPP, //启动应用前确认
        )
    }
}